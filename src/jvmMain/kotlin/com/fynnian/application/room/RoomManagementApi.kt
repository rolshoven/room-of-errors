package com.fynnian.application.room

import com.benasher44.uuid.uuid4
import com.fynnian.application.APIException
import com.fynnian.application.common.*
import com.fynnian.application.common.URLS.replaceParam
import com.fynnian.application.common.room.*
import com.fynnian.application.config.DI
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import java.io.File

// ToDo: Secure endpoints
fun Route.roomManagementApi(dependencies: DI) {
  val log = LoggerFactory.getLogger("RoomManagementApi")
  val codeParam = "code"

  route(URLS.API_ROOMS_MANAGEMENT) {
    // list rooms
    get {

      val status = call.getRoomStatusQueryParam()

      dependencies.roomRepository
        .getRoomsForManagement(status = status)
        .also { call.respond(it) }
    }
  }

  route(URLS.API_ROOMS_MANAGEMENT_BY_ID) {
    // get by id
    get {
      val code = call.getRoomCodeParam(codeParam)
      dependencies.roomRepository
        .getRoomsForManagement(code)
        .firstOrNull()
        ?.also { call.respond(it) }
        ?: throw APIException.NotFound("Room with code $code not found")
    }
    // create room
    post {
      val code = call.getRoomCodeParam()
      val room = call.receive<RoomCreation>()

      call.checkRequestIds(code, room.code)
      dependencies.roomRepository
        .createRoom(room)
        .also { call.respond(it) }
    }

    patch {
      val code = call.getRoomCodeParam()
      val room = call.receive<RoomPatch>()

      call.checkRequestIds(code, room.code)
      dependencies.roomRepository
        .patchRoom(room)
        .also { call.respond(it) }
    }

    // delete
    delete {
      val code = call.getRoomCodeParam(codeParam)

      dependencies.roomManagementService
        .deleteRoom(code)
        .also { call.response.status(HttpStatusCode.OK) }
    }
  }

  route(URLS.API_ROOMS_MANAGEMENT_ROOM_IMAGE) {

    // get images of room
    get {
      val code = call.getRoomCodeParam()

      dependencies.roomRepository
        .getRoomImages(code)
        .also { call.respond(it) }
    }

    // upload new image file
    contentType(ContentType.MultiPart.FormData) {
      post {
        val code = call.getRoomCodeParam()
        val imageId = uuid4()

        val fields = call.processMultipartForm(
          setOf(ContentType.Image.PNG, ContentType.Image.JPEG)
        ) { stream, type ->

          val fileName = "$imageId.${type.contentSubtype}"
          File("${dependencies.config.content.imageUploadDir}/$fileName")
            .absoluteFile
            .outputStream()
            .use { output -> stream.transferTo(output) }

          return@processMultipartForm fileName
        }

        val newImage = RoomImage(
          id = imageId,
          title = fields[RoomImage.TITLE_FORM_PARAM] ?: throw APIException.BadRequest("title is required"),
          url = fields[RoomImage.FILE_FORM_PARAM]
            ?.let { URLS.STATIC_IMAGES_IMAGE.replaceParam(URLS.IMAGE_NAME_PARAM(it)) }
            ?: throw APIException.BadRequest("image file is required.")
        )

        dependencies.roomRepository
          .upsertRoomImage(newImage, code)
          .also { call.respond(it) }
      }
    }
  }

  route(URLS.API_ROOMS_MANAGEMENT_ROOM_IMAGE_BY_ID) {
    delete {
      val imageId = call.getImageIdParam()

      val image = dependencies.roomRepository.getImage(imageId)
      File("${dependencies.config.content.imageUploadDir}/${image.getFileName()}")
        .also {
          if (it.delete().not()) {
            log.error("could not delete file $it")
            throw APIException.ServerError("Error while trying to delete to room image $imageId")
          }
        }
      dependencies.roomRepository.deleteImage(imageId)

      call.response.status(HttpStatusCode.OK)
    }
  }

  post(URLS.API_ROOMS_MANAGEMENT_ROOM_OPEN) {
    val code = call.getRoomCodeParam()

    dependencies.roomRepository.getRooms(code)
      .firstOrNull()
      ?.let {
        val missingData = mutableMapOf<String, I18n.TranslationKey>()
        if (it.images.isEmpty())
          missingData["images"] = I18n.TranslationKey.ROOM_MANAGEMENT_OPEN_ROOM_IMAGE_VALIDATION_ERROR
        if (missingData.isEmpty().not())
          throw APIException.ValidationError("Can not open room, required data is missing.", missingData)
        it
      }
      ?.let { dependencies.roomRepository.updateStatus(it.code, RoomStatus.OPEN.toRecord()) }
      ?.let { call.respond(it) }
      ?: throw APIException.RoomNotFound(code)
  }

  post(URLS.API_ROOMS_MANAGEMENT_ROOM_CLOSE) {
    val code = call.getRoomCodeParam()

    dependencies.roomRepository.getRooms(code)
      .firstOrNull()
      ?.let { dependencies.roomRepository.updateStatus(it.code, RoomStatus.CLOSED.toRecord()) }
      ?.let { call.respond(it) }
      ?: throw APIException.RoomNotFound(code)
  }

  route(URLS.API_ROOMS_MANAGEMENT_ROOM_INTRO) {
    contentType(ContentType.Application.Json) {
      post {
        val code = call.getRoomCodeParam()
        val body = call.receive<RoomInteractionInfo>()
        dependencies.roomManagementService
          .updateRoomInteractionInfo(code, body, RoomStatementVariant.INTRO)
          .also { call.respond(it) }
      }
    }
    contentType(ContentType.MultiPart.FormData) {
      post {
        val code = call.getRoomCodeParam()
        dependencies.roomManagementService
          .handleRoomInteractionInfoCall(code, RoomStatementVariant.INTRO, call)
          .also { call.respond(it) }
      }
    }
  }

  route(URLS.API_ROOMS_MANAGEMENT_ROOM_OUTRO) {
    contentType(ContentType.Application.Json) {
      post {
        val code = call.getRoomCodeParam()
        val body = call.receive<RoomInteractionInfo>()
        dependencies.roomManagementService
          .updateRoomInteractionInfo(code, body, RoomStatementVariant.OUTRO)
          .also { call.respond(it) }
      }
    }
    contentType(ContentType.MultiPart.FormData) {
      post {
        val code = call.getRoomCodeParam()
        dependencies.roomManagementService
          .handleRoomInteractionInfoCall(code, RoomStatementVariant.OUTRO, call)
          .also { call.respond(it) }
      }
    }
  }

  get(URLS.API_ROOMS_MANAGEMENT_EXCEL_EXPORT) {
    val code = call.getRoomCodeParam()
    val language = call.getLanguageQueryParam()
    val excel = dependencies.roomExportService.excelExportRoom(code, language)

    call.response.header(
      HttpHeaders.ContentDisposition,
      ContentDisposition.Attachment.withParameters(
        listOf(
          HeaderValueParam(ContentDisposition.Parameters.FileNameAsterisk, "room-export-$code.xlsx"),
          HeaderValueParam(ContentDisposition.Parameters.FileName, "room-export-$code.xlsx")
        )
      ).toString()
    )
    call.respondOutputStream {
      excel.write(this)
    }
  }
}
