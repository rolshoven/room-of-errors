package com.fynnian.application.room

import com.benasher44.uuid.Uuid
import com.fynnian.application.APIException
import com.fynnian.application.common.Repository
import com.fynnian.application.common.room.*
import com.fynnian.application.config.DataSource
import com.fynnian.application.jooq.enums.RoomStatus
import com.fynnian.application.jooq.tables.records.RoomImagesRecord
import com.fynnian.application.jooq.tables.records.RoomsRecord
import com.fynnian.application.jooq.tables.references.ANSWERS
import com.fynnian.application.jooq.tables.references.ROOMS
import com.fynnian.application.jooq.tables.references.ROOM_IMAGES
import org.jooq.impl.DSL.count
import org.jooq.impl.DSL.countDistinct
import java.time.OffsetDateTime
import java.util.stream.Collectors.*
import com.fynnian.application.common.room.RoomStatus as RoomStatusDomain
import com.fynnian.application.jooq.enums.RoomStatus as RoomStatusJooq

class RoomRepository(dataSource: DataSource) : Repository(dataSource) {

  fun getRooms(code: String? = null): List<Room> {
    return jooq {
      select(ROOMS.asterisk(), ROOM_IMAGES.asterisk())
        .from(ROOMS)
        .leftJoin(ROOM_IMAGES).on(ROOM_IMAGES.ROOM_CODE.eq(ROOMS.CODE))
        .let {
          if (code != null) it.where(ROOMS.CODE.eq(code))
          else it
        }
        .fetchGroups(ROOMS, ROOM_IMAGES)
        .map { (room, images) ->
          room.toDomain().copy(images = images.filterNot { it.id == null }.map { it.toDomain() })
        }
    }
  }

  fun getRoom(code: String): Room {
    return getRooms(code).firstOrNull() ?: throw APIException.RoomNotFound(code)
  }

  fun getRoomImages(code: String): List<RoomImage> {
    return jooq {
      selectFrom(ROOM_IMAGES)
        .where(ROOM_IMAGES.ROOM_CODE.eq(code))
        .fetch { it.toDomain() }
    }
  }

  fun getRoomsForManagement(code: String? = null): List<RoomDetails> {
    return jooq {

      val participants = countDistinct(ANSWERS.USER_ID).`as`("participants")
      val answers = count(ANSWERS.ID).`as`("answers")

      select(
        ROOMS.asterisk(),
        ROOM_IMAGES.asterisk(),
        participants,
        answers
      )
        .from(ROOMS)
        .leftJoin(ROOM_IMAGES).on(ROOM_IMAGES.ROOM_CODE.eq(ROOMS.CODE))
        .leftJoin(ANSWERS).on(ANSWERS.ROOM_CODE.eq(ROOMS.CODE))
        .let {
          if (code != null) it.where(ROOMS.CODE.eq(code))
          else it
        }
        .groupBy(ROOMS.CODE, ROOM_IMAGES.ID)
        .collect(
          groupingBy(
            { r ->
              val room = r.into(ROOMS)
              RoomDetails(
                code = room.code!!,
                roomStatus = room.status!!.toDomain(),
                title = room.title!!,
                description = room.description,
                question = room.question,
                timeLimitMinutes = room.timeLimitMinutes,
                images = emptyList(),
                participants = r.get(participants),
                answers = r.get(answers)
              )
            },
            filtering(
              { it.get(ROOM_IMAGES.ID) != null },
              mapping({ r -> r.into(ROOM_IMAGES).toDomain() }, toList())
            )
          )
        )
        .map { (room, images) -> room.copy(images = images) }
    }
  }

  fun createRoom(roomCreation: RoomCreation): Room {
    return jooq {
      selectFrom(ROOMS)
        .where(ROOMS.CODE.eq(roomCreation.code))
        .fetchOne()
        ?.also { throw APIException.BadRequest("Room with code ${it.code} already exists.") }

      insertInto(ROOMS)
        .set(ROOMS.CODE, roomCreation.code)
        .set(ROOMS.TITLE, roomCreation.title)
        .set(ROOMS.STATUS, RoomStatus.not_ready)
        .returning()
        .fetchOne()
        ?.toDomain()
        ?: throw APIException.ServerError("Could not create a new room")
    }
  }

  fun patchRoom(room: RoomPatch): Room {
    return jooq {

      update(ROOMS)
        .set(ROOMS.TITLE, room.title)
        .set(ROOMS.DESCRIPTION, room.description)
        .set(ROOMS.QUESTION, room.question)
        .set(ROOMS.TIME_LIMIT_MINUTES, room.timeLimitMinutes)
        .where(ROOMS.CODE.eq(room.code))
        .returning()
        .fetchOne()
        ?: APIException.RoomNotFound(room.code)

      getRoom(room.code)
    }
  }

  fun upsertRoomImage(image: RoomImage, roomCode: String): RoomImage {
    return jooq {
      insertInto(ROOM_IMAGES)
        .set(image.toRecord(roomCode))
        .onConflict(ROOM_IMAGES.ID)
        .doUpdate()
        .set(
          image
            .toRecord(roomCode)
            .also { it.updatedAt = OffsetDateTime.now() } // simply way of setting the updatedAt without trigger
        )
        .returning()
        .map { it.into(ROOM_IMAGES).toDomain() }
        .first()
    }
  }

  fun deleteRoom(code: String) {
    jooq {
      deleteFrom(ROOMS)
        .where(ROOMS.CODE.eq(code))
        .returning()
        .firstOrNull()
        ?: throw APIException.NotFound("Room with code $code not found")
    }
  }

  fun updateStatus(code: String, status: RoomStatus): RoomDetails {
    jooq {
      update(ROOMS)
        .set(ROOMS.STATUS, status)
        .where(ROOMS.CODE.eq(code))
        .returning()
        .fetchOne()
        ?: throw APIException.ServerError("Could not change the room status of room $code to $status")
    }
    return getRoomsForManagement(code).first()
  }

  fun getImage(imageId: Uuid): RoomImage {
    return jooq {
      selectFrom(ROOM_IMAGES)
        .where(ROOM_IMAGES.ID.eq(imageId))
        .fetchOne { it.toDomain() }
        ?: throw APIException.NotFound("There is no image with id $imageId")
    }
  }

  fun deleteImage(imageId: Uuid) {
    jooq {
      delete(ROOM_IMAGES).where(ROOM_IMAGES.ID.eq(imageId)).execute()
    }
  }

  fun upsertRoomInteractionInfo(
    code: String,
    interactionInfo: RoomInteractionInfo,
    variant: RoomStatementVariant
  ): Room {
    jooq {
      update(ROOMS)
        .let {
          when (variant) {
            RoomStatementVariant.INTRO ->
              it.set(ROOMS.INTRO_TEXT, interactionInfo.text)
                .set(ROOMS.INTRO_VIDEO_TITLE, interactionInfo.videoTitle)
                .set(ROOMS.INTRO_VIDEO_URL, interactionInfo.videoURl)

            RoomStatementVariant.OUTRO ->
              it.set(ROOMS.OUTRO_TEXT, interactionInfo.text)
                .set(ROOMS.OUTRO_VIDEO_TITLE, interactionInfo.videoTitle)
                .set(ROOMS.OUTRO_VIDEO_URL, interactionInfo.videoURl)
          }
        }
        .where(ROOMS.CODE.eq(code))
        .returning()
        .fetchOne()
        ?: throw APIException.ServerError("could not update data for $variant")
    }
    return getRoom(code)
  }
}

fun RoomsRecord.toDomain() = Room(
  code = code!!,
  roomStatus = status!!.toDomain(),
  title = title!!,
  description = description,
  question = question,
  timeLimitMinutes = timeLimitMinutes,
  intro = RoomInteractionInfo(introText, introVideoTitle, introVideoUrl),
  outro = RoomInteractionInfo(outroText, outroVideoTitle, outroVideoUrl),
  images = listOf()
)

fun Room.toRecord() = RoomsRecord().also {
  it.code = code
  it.status = roomStatus.toRecord()
  it.title = title
  it.description = description
  it.question = question
  it.timeLimitMinutes = timeLimitMinutes
  it.introText = intro.text
  it.introVideoTitle = intro.videoTitle
  it.introVideoUrl = intro.videoURl
  it.outroText = outro.text
  it.outroVideoTitle = outro.videoTitle
  it.outroVideoUrl = outro.videoURl
}

fun RoomImage.toRecord(roomCode: String) = RoomImagesRecord().also {
  it.id = id
  it.title = title
  it.url = url
  it.file = true // ToDo: currently only file upload, no web url
  it.roomCode = roomCode
}

fun RoomImagesRecord.toDomain() = RoomImage(
  id = id!!,
  title = title!!,
  url = url!!
)

fun RoomStatusJooq.toDomain() = RoomStatusDomain.valueOf(literal.uppercase())
fun RoomStatusDomain.toRecord() = RoomStatusJooq.valueOf(name.lowercase())