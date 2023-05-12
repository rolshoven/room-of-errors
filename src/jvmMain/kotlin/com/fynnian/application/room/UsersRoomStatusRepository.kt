package com.fynnian.application.room

import com.benasher44.uuid.Uuid
import com.fynnian.application.APIException
import com.fynnian.application.common.Repository
import com.fynnian.application.common.room.UsersRoomStatus
import com.fynnian.application.config.DataSource
import com.fynnian.application.jooq.tables.records.UsersRoomStatusRecord
import com.fynnian.application.jooq.tables.references.ROOMS
import com.fynnian.application.jooq.tables.references.USERS
import com.fynnian.application.jooq.tables.references.USERS_ROOM_STATUS
import kotlinx.datetime.Instant
import com.fynnian.application.common.room.UsersRoomParticipationStatus as UsersRoomParticipationStatusDomain
import com.fynnian.application.jooq.enums.UsersRoomParticipationStatus as UsersRoomParticipationStatusJooq

class UsersRoomStatusRepository(dataSource: DataSource) : Repository(dataSource) {

  fun getUsersRoomStatus(userId: Uuid, roomCode: String) = internalGetUsersRoomStatus(userId, roomCode).toDomain()
  private fun internalGetUsersRoomStatus(userId: Uuid, roomCode: String): UsersRoomStatusRecord {
    return jooq {

      selectFrom(ROOMS).where(ROOMS.CODE.eq(roomCode))
        .fetch()
        .firstOrNull()
        ?: throw APIException.RoomNotFound(roomCode)

      selectFrom(USERS).where(USERS.ID.eq(userId)).fetch()
        .firstOrNull()
        ?: throw APIException.UserNotFound(userId)

      selectFrom(USERS_ROOM_STATUS)
        .where(USERS_ROOM_STATUS.USER_ID.eq(userId).and(USERS_ROOM_STATUS.ROOM_CODE.eq(roomCode)))
        .fetchOne()
        ?: createUsersRoomStatus(userId, roomCode)
    }
  }

  private fun createUsersRoomStatus(userId: Uuid, roomCode: String): UsersRoomStatusRecord {
    return jooq {
      insertInto(USERS_ROOM_STATUS)
        .set(USERS_ROOM_STATUS.USER_ID, userId)
        .set(USERS_ROOM_STATUS.ROOM_CODE, roomCode)
        .set(USERS_ROOM_STATUS.PARTICIPATION_STATUS, UsersRoomParticipationStatusJooq.not_started)
        .returning()
        .fetchOne()
        ?: throw APIException.ServerError("Could not create new user status entry")
    }
  }

  fun startRoom(userId: Uuid, roomCode: String): UsersRoomStatus {
    return jooq {
      val record = internalGetUsersRoomStatus(userId, roomCode)
        .apply {
          participationStatus = UsersRoomParticipationStatusJooq.started
          startedAt = nowAtCHOffsetDateTime()
        }
      update(record)
    }
  }

  fun finishRoom(userId: Uuid, roomCode: String): UsersRoomStatus {
    return jooq {
      val record = internalGetUsersRoomStatus(userId, roomCode)
        .apply {
          participationStatus = UsersRoomParticipationStatusJooq.finished
          if (startedAt == null) startedAt = nowAtCHOffsetDateTime()
          if (finishedAt == null) finishedAt = nowAtCHOffsetDateTime()
        }
      update(record)
    }
  }

  fun closeRoom(userId: Uuid, roomCode: String): UsersRoomStatus {
    return jooq {
      val record = internalGetUsersRoomStatus(userId, roomCode)
        .apply {
          participationStatus = UsersRoomParticipationStatusJooq.finished
          if (startedAt == null) startedAt = nowAtCHOffsetDateTime()
          if (finishedAt == null) finishedAt = nowAtCHOffsetDateTime()
          if (closedAt == null) closedAt = nowAtCHOffsetDateTime()
        }
      update(record)
    }
  }
  private fun update(record: UsersRoomStatusRecord) = jooq {
    update(USERS_ROOM_STATUS)
      .set(record)
      .where(USERS_ROOM_STATUS.USER_ID.eq(record.userId).and(USERS_ROOM_STATUS.ROOM_CODE.eq(record.roomCode)))
      .returning()
      .fetchOne()
      ?.toDomain()
      ?: throw APIException.ServerError("Can't change status of room ${record.roomCode} for user ${record.userId}")
  }

  fun getUserStatesOfRoom(code: String): Map<Uuid, UsersRoomStatus> {
    return jooq {
      selectFrom(USERS_ROOM_STATUS)
        .where(USERS_ROOM_STATUS.ROOM_CODE.eq(code))
        .fetch { it.userId!! to it.toDomain() }
        .toMap()
    }
  }

}

fun UsersRoomParticipationStatusJooq.toDomain() = UsersRoomParticipationStatusDomain.valueOf(literal.uppercase())
fun UsersRoomStatusRecord.toDomain() = UsersRoomStatus(
  userId = userId!!,
  roomCode = roomCode!!,
  participationStatus = participationStatus!!.toDomain(),
  startedAt = startedAt?.let { Instant.fromEpochSeconds(it.toEpochSecond()) },
  finishedAt = finishedAt?.let { Instant.fromEpochSeconds(it.toEpochSecond()) },
  closedAt = closedAt?.let { Instant.fromEpochSeconds(it.toEpochSecond()) }
)