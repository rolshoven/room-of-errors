package com.fynnian.application.user

import com.fynnian.application.APIException
import com.fynnian.application.common.*
import com.fynnian.application.config.AppConfig
import com.fynnian.application.jooq.Tables.USERS
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private const val idParam = "id"

fun Route.userApi(config: AppConfig) {
  route(AppPaths.API_USERS.path) {
    route("/{$idParam}") {
      // get user by id
      get() {
        val id = call.getUUIDParam(idParam)

        Repository(config.dataSource)
          .createContext()
          .select(USERS.asterisk())
          .from(USERS)
          .where(USERS.ID.eq(id))
          .map { it.into(USERS).toDomain() }
          .firstOrNull()
          ?.also { call.respond(it) }
          ?: throw APIException.NotFound("User with id: $id not found")
      }
      // create / update user by id
      put {
        val id = call.getUUIDParam(idParam)
        val request = call.receive<User>()
        call.checkRequestIds(id, request.id)

        Repository(config.dataSource)
          .createContext()
          .insertInto(USERS)
          .set(request.toRecord())
          .onConflict(USERS.ID)
          .doUpdate()
          .set(request.toRecord())
          .returning()
          .map { it.into(USERS).toDomain() }
          .first()
          .also { call.respond(it) }

      }
      // delete user
      delete {
        val id = call.getUUIDParam(idParam)

        Repository(config.dataSource)
          .createContext()
          .deleteFrom(USERS)
          .where(USERS.ID.eq(id))
          .returning()
          .firstOrNull()
          .also { call.response.status(HttpStatusCode.OK) }
          ?: throw APIException.NotFound("User with id: $id not found")
      }
    }
  }
}

