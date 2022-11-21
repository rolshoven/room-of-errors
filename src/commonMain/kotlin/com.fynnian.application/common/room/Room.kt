package com.fynnian.application.common.room

import com.benasher44.uuid.Uuid
import com.fynnian.application.common.UuidSerializer
import kotlinx.serialization.Serializable

@Serializable
data class Room(
  val code: String,
  val roomStatus: RoomStatus,
  val title: String,
  val description: String,
  val question: String,
  val timeLimitMinutes: Int,
  val images: List<RoomImage>
)
@Serializable
data class RoomImage(
  @Serializable(with = UuidSerializer::class)
  val id: Uuid,
  val url: String,
  val title: String,
)
enum class RoomStatus {
  OPEN,
  CLOSED
}