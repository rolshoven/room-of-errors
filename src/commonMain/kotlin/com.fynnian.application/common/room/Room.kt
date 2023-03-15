package com.fynnian.application.common.room

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import com.fynnian.application.common.UuidSerializer
import kotlinx.serialization.Serializable

@Serializable
data class RoomCreation(
  val code: String,
  val title: String,
)

@Serializable
data class RoomPatch(
  val code: String,
  val title: String,
  val description: String?,
  val question: String?,
  val timeLimitMinutes: Int?,
  val singleDeviceRoom: Boolean?
)

@Serializable
data class Room(
  val code: String,
  val roomStatus: RoomStatus,
  val title: String,
  val description: String?,
  val question: String?,
  val timeLimitMinutes: Int?,
  val singleDeviceRoom: Boolean,
  val intro: RoomInteractionInfo,
  val outro: RoomInteractionInfo,
  val images: List<RoomImage>
) {
  companion object {
    fun genRoomCode(): String {
      return uuid4().toString().split("-")[0]
    }

    const val titleMaxChars = 100
  }
}

@Serializable
data class RoomInteractionInfo(
  val text: String?,
  val videoTitle: String?,
  val videoURl: String?
) {
  fun getFileName() = videoURl?.substringAfterLast("/")

  companion object {
    const val TEXT_FORM_PARAM = "text"
    const val VIDEO_TITLE_FORM_PARAM = "title"
    const val VIDEO_FORM_PARAM = "video"
  }
}

enum class RoomStatementVariant {
  INTRO,
  OUTRO
}


@Serializable
data class RoomManagementDetail(
  val code: String,
  val roomStatus: RoomStatus,
  val title: String,
  val description: String?,
  val question: String?,
  val timeLimitMinutes: Int?,
  val singleDeviceRoom: Boolean,
  val intro: RoomInteractionInfo,
  val outro: RoomInteractionInfo,
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
) {

  fun getFileName() = url.substringAfterLast("/")

  companion object {
    const val TITLE_FORM_PARAM = "title"
    const val FILE_FORM_PARAM = "image"
  }
}

enum class RoomStatus {
  NOT_READY,
  OPEN,
  CLOSED
}