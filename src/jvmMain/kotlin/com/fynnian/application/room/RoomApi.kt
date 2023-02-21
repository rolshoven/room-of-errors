package com.fynnian.application.room

import com.fynnian.application.APIException
import com.fynnian.application.common.*
import com.fynnian.application.common.room.Answer
import com.fynnian.application.config.DI
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.roomApi(dependencies: DI) {
  val roomCodeParam = "code"
  val answerIdParam = "id"

  // room by id - public
  route(URLS.API_ROOMS_BY_ID) {
    // get by id
    get {
      val code = call.getRoomCodeParam(roomCodeParam)
      dependencies.roomRepository
        .getRooms(code)
        .firstOrNull()
        ?.also { call.respond(it) }
        ?: throw APIException.NotFound("Room with code $code not found")

    }
  }

  route(URLS.API_ROOMS_USER_STATUS) {
    get {
      val code = call.getRoomCodeParam()
      val userId = call.getUserIdParam()

      dependencies.usersRoomStatusRepository
        .getUsersRoomStatus(userId, code)
        .also { call.respond(it) }
    }
  }

  route(URLS.API_ROOMS_USER_START) {
    post {
      val code = call.getRoomCodeParam()
      val userId = call.getUserIdParam()

      dependencies.usersRoomStatusRepository
        .startRoom(userId, code)
        .also { call.respond(it) }
    }
  }

  route(URLS.API_ROOMS_USER_FINISH) {
    post {
      val code = call.getRoomCodeParam()
      val userId = call.getUserIdParam()

      dependencies.usersRoomStatusRepository
        .finishRoom(userId, code)
        .also { call.respond(it) }
    }
  }

  route(URLS.API_ROOMS_USER_ANSWERS) {
    // list answers
    get {
      val code = call.getRoomCodeParam(roomCodeParam)
      val userId = call.getUserIdParam()

      dependencies.answersRepository
        .getAnswersOfUserForRoom(code, userId)
        .also { call.respond(it) }
    }
  }

  route(URLS.API_ROOMS_ANSWER_BY_ID) {
    // get by id
    get {
      val id = call.getUUIDParam(answerIdParam)
      val code = call.getRoomCodeParam(roomCodeParam)

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