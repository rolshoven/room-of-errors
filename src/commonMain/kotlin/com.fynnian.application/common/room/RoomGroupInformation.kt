package com.fynnian.application.common.room

import com.benasher44.uuid.Uuid
import com.fynnian.application.common.UuidSerializer
import kotlinx.serialization.Serializable

@Serializable
data class RoomGroupInformation(
  @Serializable(with = UuidSerializer::class)
  val userId: Uuid,
  val roomCode: String,
  val groupSize: Int,
  val groupName: String
)
