package com.fynnian.application.room

import com.benasher44.uuid.Uuid
import com.fynnian.application.APIException
import com.fynnian.application.common.Repository
import com.fynnian.application.common.room.RoomGroupInformation
import com.fynnian.application.config.DataSource
import com.fynnian.application.jooq.tables.records.RoomGroupInformationRecord
import com.fynnian.application.jooq.tables.references.ROOM_GROUP_INFORMATION

class GroupRepository(dataSource: DataSource) : Repository(dataSource) {

  fun getGroupInformation(code: String, userId: Uuid): RoomGroupInformation {
    return jooq {
      selectFrom(ROOM_GROUP_INFORMATION)
        .where(ROOM_GROUP_INFORMATION.ROOM_CODE.eq(code))
        .and(ROOM_GROUP_INFORMATION.USER_ID.eq(userId))
        .fetch { it.toDomain() }
        .firstOrNull()
        ?: throw APIException.NotFound("No group information for room $code and userId $userId found.")
    }
  }

  fun createGroupInformation(data: RoomGroupInformation): RoomGroupInformation {
    return jooq {
      insertInto(ROOM_GROUP_INFORMATION)
        .set(data.toRecord())
        .onConflict(ROOM_GROUP_INFORMATION.USER_ID, ROOM_GROUP_INFORMATION.ROOM_CODE)
        .doUpdate()
        .set(data
          .toRecord()
          .also { it.updatedAt = nowAtCHOffsetDateTime() }
        )
        .returning()
        .fetchOne { it.toDomain() }
        ?: throw APIException.ServerError("Could not create group information entry")
    }
  }
}


fun RoomGroupInformationRecord.toDomain() = RoomGroupInformation(
  userId = userId!!,
  roomCode = roomCode!!,
  groupName = groupName!!,
  groupSize = groupSize!!
)

fun RoomGroupInformation.toRecord() = RoomGroupInformationRecord().also {
  it.userId = userId
  it.roomCode = roomCode
  it.groupName = groupName
  it.groupSize = groupSize
}