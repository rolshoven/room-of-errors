package com.fynnian.application.room

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import com.fynnian.application.APIException
import com.fynnian.application.common.AppPaths
import com.fynnian.application.common.checkRequestIds
import com.fynnian.application.common.getRoomCodeParam
import com.fynnian.application.common.room.Room
import com.fynnian.application.common.room.RoomImage
import com.fynnian.application.common.room.RoomStatus
import com.fynnian.application.config.DI
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

private const val roomCodeParam = "roomCode"

fun Route.roomManagementApi(dependencies: DI) {
  route(AppPaths.API_ROOMS.path) {
    // management - secured
    route(AppPaths.API_ROOMS_MANAGEMENT.path) {
      // list rooms
      get {
        dependencies.roomRepository
          .getRoomsForManagement()
          .also { call.respond(it) }
      }
      // room by id
      route("/{$roomCodeParam}") {
        // get by id
        get {
          val code = call.getRoomCodeParam(roomCodeParam)
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
            val code = call.getRoomCodeParam(roomCodeParam)
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
            val code = call.getRoomCodeParam(roomCodeParam)
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
              images = listOf(
                RoomImage(
                  id = imageId ?: throw APIException.BadRequest("image is required"),
                  title = fields["imageTitle"] ?: throw APIException.BadRequest("imageTitle is required"),
                  url = AppPaths.STATIC_IMAGES_ROOT.path + "/$imageFileName"
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
          val code = call.getRoomCodeParam(roomCodeParam)

          dependencies.roomRepository
            .deleteRoom(code)
            .also { call.response.status(HttpStatusCode.OK) }
        }

        // answers - with filter, for user

      }

    }
  }
}