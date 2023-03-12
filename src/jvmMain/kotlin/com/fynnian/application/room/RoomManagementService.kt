package com.fynnian.application.room

import com.benasher44.uuid.uuid4
import com.fynnian.application.APIException
import com.fynnian.application.common.URLS
import com.fynnian.application.common.URLS.replaceParam
import com.fynnian.application.common.processMultipartForm
import com.fynnian.application.common.room.RoomInteractionInfo
import com.fynnian.application.common.room.RoomManagementDetail
import com.fynnian.application.common.room.RoomStatementVariant
import com.fynnian.application.config.Content
import io.ktor.http.*
import io.ktor.server.application.*
import org.slf4j.LoggerFactory
import java.io.File

class RoomManagementService(
  private val contentConfig: Content,
  private val roomRepository: RoomRepository
) {

  suspend fun handleRoomInteractionInfoCall(
    code: String,
    variant: RoomStatementVariant,
    call: ApplicationCall
  ): RoomManagementDetail {
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


  fun deleteRoom(code: String) {
    val room = roomRepository.getRoom(code)

    roomRepository.deleteRoom(code)

    val filesToDelete: MutableList<File> =
      room.images.mapTo(mutableListOf()) { File("${contentConfig.imageUploadDir}/${it.getFileName()}") }

    room.intro.getFileName()
      ?.let { fileName -> filesToDelete.plus(File("${contentConfig.videoUploadDir}/${fileName}")) }
    room.outro.getFileName()
      ?.let { fileName -> filesToDelete.plus(File("${contentConfig.videoUploadDir}/${fileName}")) }

    filesToDelete.forEach {
      if (!it.exists()) {
        log.error("while deleting the room $code, could not delete file $it it doesn't exist")
        return@forEach
      }
      if (it.delete().not()) {
        log.error("while deleting the room $code, could not delete file $it")
        return@forEach
      }
      log.debug("deleted file $it of room $code")
    }
  }

  companion object {
    @JvmStatic
    private val log = LoggerFactory.getLogger(RoomManagementService::class.java)
  }
}