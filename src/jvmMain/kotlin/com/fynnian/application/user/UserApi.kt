package com.fynnian.application.user

import com.fynnian.application.common.AppPaths
import com.fynnian.application.common.User
import com.fynnian.application.common.getUUIDParam
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private const val idParam = "id"

fun Route.userApi() {
  route(AppPaths.API_USERS.path) {
    route("/{$idParam}") {
      // get user by id
      get() {
        call.respond(User(call.getUUIDParam(idParam), ""))
      }
      // create / update user by id
      put {

      }
      // delete user
      delete {

      }
    }
  }
}

