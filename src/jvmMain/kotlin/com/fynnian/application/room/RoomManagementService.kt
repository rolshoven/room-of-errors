package com.fynnian.application.room

import com.benasher44.uuid.uuid4
import com.fynnian.application.APIException
import com.fynnian.application.common.URLS
import com.fynnian.application.common.URLS.replaceParam
import com.fynnian.application.common.processMultipartForm
import com.fynnian.application.common.room.Room
import com.fynnian.application.common.room.RoomStatementVariant
import com.fynnian.application.common.room.RoomInteractionInfo
import com.fynnian.application.config.Content
import io.ktor.http.*
import io.ktor.server.application.*
import java.io.File

class RoomManagementService(
  private val contentConfig: Content,
  private val roomRepository: RoomRepository
) {

  suspend fun handleRoomInteractionInfoCall(
    code: String,
    variant: RoomStatementVariant,
    call: ApplicationCall
  ): Room {
    // check if room exists
    roomRepository.getRoom(code)

    // ToDo: delete old video if exist

    val fields = call.processMultipartForm(
      setOf(ContentType.Video.MP4)
    ) { stream, type ->

      val fileName = "${uuid4()}.${type.contentSubtype}"

      File("${contentConfig.videoUploadDir}/$fileName")
        .absoluteFile
        .writeBytes(stream.readBytes())

      return@processMultipartForm fileName
    }

    val interactionInfo = RoomInteractionInfo(
      text = fields[RoomInteractionInfo.TEXT_FORM_PARAM], // optional text
      videoTitle = fields[RoomInteractionInfo.VIDEO_TITLE_FORM_PARAM],
      videoURl = fields[RoomInteractionInfo.VIDEO_FORM_PARAM]?.let {
        URLS.STATIC_VIDEOS_VIDEO.replaceParam(URLS.VIDEO_NAME_PARAM(it))
      }
    ).also {
      if (it.videoURl != null && it.videoTitle == null) throw APIException.BadRequest("Video provided but title is missing")
    }

    return roomRepository.upsertRoomInteractionInfo(code, interactionInfo, variant)
  }
}