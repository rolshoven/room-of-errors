package com.fynnian.application.common.room

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
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
  val startingText: String?,
  val startingVideoTitle: String?,
  val startingVideoUrl: String?,
  val endingText: String?,
  val endingVideoTitle: String?,
  val endingVideoUrl: String?,
  val images: List<RoomImage>
) {
  companion object {
    fun genRoomCode(): String {
      return uuid4().toString().split("-")[0]
    }
  }
}

@Serializable
data class RoomDetails(
  val code: String,
  val roomStatus: RoomStatus,
  val title: String,
  val description: String,
  val question: String,
  val timeLimitMinutes: Int,
  val images: List<RoomImage>,
  val participants: Int,
  val answers: Int
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