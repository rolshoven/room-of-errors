package com.fynnian.application.room

import com.fynnian.application.APIException
import com.fynnian.application.common.Repository
import com.fynnian.application.common.room.Room
import com.fynnian.application.common.room.RoomDetails
import com.fynnian.application.common.room.RoomImage
import com.fynnian.application.config.DataSource
import com.fynnian.application.jooq.Tables.*
import com.fynnian.application.jooq.tables.records.RoomImagesRecord
import com.fynnian.application.jooq.tables.records.RoomsRecord
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
          room.toDomain().copy(images = images.map { it.toDomain() })
        }
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
        .groupBy(ROOMS.CODE)
        .collect(
          groupingBy(
            { r ->
              val room = r.into(ROOMS)
              RoomDetails(
                code = room.code,
                roomStatus = room.status.toDomain(),
                title = room.title,
                description = room.description,
                question = room.question,
                timeLimitMinutes = room.timeLimitMinutes,
                images = emptyList(),
                participants = r.get(participants),
                answers = r.get(answers)
              )
            },
            mapping({ r -> r.into(ROOM_IMAGES) }, toList())
          )
        )
        .map { (room, images) -> room.copy(images = images.map { it.toDomain() }) }
    }
  }

  fun upsertRoom(room: Room): Room {
    return jooq {
      val modifiedRoom = update(ROOMS)
        .set(room.toRecord())
        .where(ROOMS.CODE.eq(room.code))
        .returning()
        .fetchOne()
        ?.map { it.into(ROOMS).toDomain() }
        ?: insertInto(ROOMS)
          .set(room.toRecord())
          .returning()
          .map { it.into(ROOMS).toDomain() }
          .first()

      val images = room.images.map { upsertRoomImage(it, room.code) }

      modifiedRoom.copy(images = images)
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
}

fun RoomsRecord.toDomain() = Room(
  code = code,
  roomStatus = status.toDomain(),
  title = title,
  description = description,
  question = question,
  timeLimitMinutes = timeLimitMinutes,
  startingText = startingText,
  startingVideoTitle = startingVideoTitle,
  startingVideoUrl = startingVideoUrl,
  endingText = endingText,
  endingVideoTitle = endingVideoTitle,
  endingVideoUrl = endingVideoUrl,
  images = listOf()
)

fun Room.toRecord() = RoomsRecord().also {
  it.code = code
  it.status = roomStatus.toRecord()
  it.title = title
  it.description = description
  it.question = question
  it.timeLimitMinutes = timeLimitMinutes
  it.startingText = startingText
  it.startingVideoTitle = startingVideoTitle
  it.startingVideoUrl = startingVideoUrl
  it.endingText = endingText
  it.endingVideoTitle = endingVideoTitle
  it.endingVideoUrl = endingVideoUrl
}

fun RoomImage.toRecord(roomCode: String) = RoomImagesRecord().also {
  it.id = id
  it.title = title
  it.url = url
  it.file = false
  it.roomCode = roomCode
}

fun RoomImagesRecord.toDomain() = RoomImage(
  id = id,
  title = title,
  url = url
)

fun RoomStatusJooq.toDomain() = RoomStatusDomain.valueOf(literal.uppercase())
fun RoomStatusDomain.toRecord() = RoomStatusJooq.valueOf(name.lowercase())