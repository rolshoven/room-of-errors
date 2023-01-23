package com.fynnian.application.room

import com.fynnian.application.APIException
import com.fynnian.application.common.*
import com.fynnian.application.common.room.Room
import com.fynnian.application.common.room.Answer
import com.fynnian.application.config.DI
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private const val roomCodeParam = "roomCode"
private const val answerIdParam = "answerId"
private const val userId = "userId"

fun Route.roomApi(dependencies: DI) {
  route(AppPaths.API_ROOMS.path) {

    // room by id - public
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

      // answers - with filter, for user
      route("/answers") {
        // list answers
        get {
          val code = call.getRoomCodeParam(roomCodeParam)
          val userId = call.getUUIDQueryParam(userId)

          dependencies.answersRepository
            .getAnswersOfUserForRoom(code, userId)
            .also { call.respond(it) }
        }
        route("/{$answerIdParam}") {
          // get by id
          get {
            val id = call.getUUIDParam(answerIdParam)

            dependencies.answersRepository
              .getAnswerById(id)
              .also { call.respond(it) }
          }
          // create / update
          put {
            val id = call.getUUIDParam(answerIdParam)
            val code = call.getRoomCodeParam(roomCodeParam)
            val answer = call.receive<Answer>()

            call.checkRequestIds(id, answer.id)
            call.checkRequestIds(code, answer.roomCode)

            dependencies.answersRepository
              .upsertAnswer(answer)
              .also { call.respond(it) }
          }
          // delete
          delete {
            val id = call.getUUIDParam(answerIdParam)
            val code = call.getRoomCodeParam(roomCodeParam)

            dependencies.answersRepository
              .deleteAnswer(id, code)
              .also { call.response.status(HttpStatusCode.OK) }
          }
        }
      }
    }
    // management - secured
    route(AppPaths.API_ROOMS_MANAGEMENT.path) {
      // room by id - secured
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

        put {
          val code = call.getRoomCodeParam(roomCodeParam)
          val room = call.receive<Room>()

          call.checkRequestIds(code, room.code)
          dependencies.roomRepository
            .upsertRoom(room)
            .also { call.respond(it) }
        }
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