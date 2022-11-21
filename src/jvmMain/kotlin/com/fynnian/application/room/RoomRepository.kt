package com.fynnian.application.room

import com.fynnian.application.APIException
import com.fynnian.application.common.Repository
import com.fynnian.application.common.room.Room
import com.fynnian.application.common.room.RoomImage
import com.fynnian.application.config.DataSource
import com.fynnian.application.jooq.Tables.ROOMS
import com.fynnian.application.jooq.Tables.ROOM_IMAGES
import com.fynnian.application.jooq.tables.records.RoomImagesRecord
import com.fynnian.application.jooq.tables.records.RoomsRecord
import java.time.OffsetDateTime
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
  images = listOf()
)

fun Room.toRecord() = RoomsRecord().also {
  it.code = code
  it.status = roomStatus.toRecord()
  it.title = title
  it.description = description
  it.question = question
  it.timeLimitMinutes = timeLimitMinutes
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