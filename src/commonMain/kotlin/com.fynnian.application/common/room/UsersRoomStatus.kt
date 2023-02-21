package com.fynnian.application.common.room

import com.benasher44.uuid.Uuid
import com.fynnian.application.common.UuidSerializer
import kotlinx.serialization.Serializable

@Serializable
data class UsersRoomStatus(
  @Serializable(with = UuidSerializer::class)
  val userId: Uuid,
  val roomCode: String,
  val participationStatus: UsersRoomParticipationStatus
)

enum class UsersRoomParticipationStatus {
  NOT_STARTED,
  STARTED,
  FINISHED
}