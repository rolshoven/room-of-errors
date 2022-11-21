package com.fynnian.application.room

import com.fynnian.application.APIException
import com.fynnian.application.common.AppPaths
import com.fynnian.application.common.checkRequestIds
import com.fynnian.application.common.room.Room
import com.fynnian.application.common.getRoomCodeParam
import com.fynnian.application.config.DI
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private const val roomCodeParam = "roomCode"

fun Route.roomManagementApi(dependencies: DI) {
  route(AppPaths.API_ROOMS.path) {
    // management - secured
    route(AppPaths.API_ROOMS_MANAGEMENT.path) {
      // list rooms
      get {
        dependencies.roomRepository
          .getRooms()
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
        put {
          val code = call.getRoomCodeParam(roomCodeParam)
          val room = call.receive<Room>()

          call.checkRequestIds(code, room.code)
          dependencies.roomRepository
            .upsertRoom(room)
            .also { call.respond(it) }
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