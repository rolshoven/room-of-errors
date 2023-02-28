package com.fynnian.application.room

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import com.fynnian.application.APIException
import com.fynnian.application.common.URLS
import com.fynnian.application.common.URLS.replaceParam
import com.fynnian.application.common.checkRequestIds
import com.fynnian.application.common.getRoomCodeParam
import com.fynnian.application.common.room.Room
import com.fynnian.application.common.room.RoomImage
import com.fynnian.application.common.room.RoomStatements
import com.fynnian.application.common.room.RoomStatus
import com.fynnian.application.config.DI
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

// ToDo: Secure endpoints
fun Route.roomManagementApi(dependencies: DI) {
  val codeParam = "code"

  route(URLS.API_ROOMS_MANAGEMENT) {
    // list rooms
    get {
      dependencies.roomRepository
        .getRoomsForManagement()
        .also { call.respond(it) }
    }
  }

  route(URLS.API_ROOMS_MANAGEMENT_BY_ID) {
    // get by id
    get {
      val code = call.getRoomCodeParam(codeParam)
      dependencies.roomRepository
        .getRooms(code)
        .firstOrNull()
        ?.also { call.respond(it) }
        ?: throw APIException.NotFound("Room with code $code not found")
    }
    // create / update
    // with web url
    contentType(ContentType.Application.Json) {
      put {
        val code = call.getRoomCodeParam(codeParam)
        val room = call.receive<Room>()

        call.checkRequestIds(code, room.code)
        dependencies.roomRepository
          .upsertRoom(room)
          .also { call.respond(it) }
      }
    }
    // with file
    contentType(ContentType.MultiPart.FormData) {
      put {
        val code = call.getRoomCodeParam(codeParam)
        val fields = mutableMapOf<String, String>()
        var imageId: Uuid? = null
        var imageFileName = ""

        call.receiveMultipart().forEachPart { part ->
          when (part) {
            is PartData.FormItem -> {
              if (part.name == "code") call.checkRequestIds(code, part.value)
              fields[part.name ?: ""] = part.value
            }

            is PartData.FileItem -> {
              val fileType = part.contentType
              if (fileType?.contentSubtype != "png") APIException.BadRequest("only PNG allowed")

              imageId = uuid4()
              imageFileName = imageId.toString() + "." + fileType!!.contentSubtype
              val fileBytes = part.streamProvider().readBytes()
              File("${dependencies.config.content.uploadDir}/$imageFileName").absoluteFile.writeBytes(fileBytes)
            }

            else -> {}
          }
          part.dispose()
        }

        val newRoom = Room(
          code = fields["code"] ?: throw APIException.BadRequest("code is required"),
          roomStatus = RoomStatus.OPEN,
          title = fields["title"] ?: throw APIException.BadRequest("title is required"),
          description = fields["description"] ?: "",
          question = fields["question"] ?: throw APIException.BadRequest("question is required"),
          timeLimitMinutes = 0, // ToDo
          startingStatements = RoomStatements(null, null, null),
          endingStatements = RoomStatements(null, null, null),
          images = listOf(
            RoomImage(
              id = imageId ?: throw APIException.BadRequest("image is required"),
              title = fields["imageTitle"] ?: throw APIException.BadRequest("imageTitle is required"),
              url = URLS.STATIC_IMAGES_IMAGE.replaceParam(URLS.IMAGE_NAME_PARAM(imageFileName))
            )
          )
        )

        // ToDo: handle updating of new image, allow it?
        dependencies.roomRepository
          .upsertRoom(newRoom)
          .also { call.respond(it) }
      }
    }
    // delete
    delete {
      val code = call.getRoomCodeParam(codeParam)

      dependencies.roomRepository
        .deleteRoom(code)
        .also { call.response.status(HttpStatusCode.OK) }
    }
  }

  get(URLS.API_ROOMS_MANAGEMENT_EXCEL_EXPORT) {
    val code = call.getRoomCodeParam(codeParam)
    val excel = dependencies.roomExportService.excelExportRoom(code)

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