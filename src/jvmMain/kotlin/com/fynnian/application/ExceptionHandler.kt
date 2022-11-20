package com.fynnian.application

import com.fynnian.application.common.APIErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*


fun Application.installExceptionHandling() {
  install(StatusPages) {
    exception<APIException> { call, cause -> call.respond(cause.status, cause.asResponse()) }
  }
}

sealed class APIException(
  override val message: String,
  override val cause: Throwable?,
  val status: HttpStatusCode
) : Exception(message, cause) {

  data class BadRequest(
    override val message: String = "Bad Request",
    override val cause: Throwable? = null
  ) : APIException(message, cause, HttpStatusCode.BadRequest)

  data class NotFound(
    override val message: String = "Not Found",
    override val cause: Throwable? = null
  ) : APIException(message, cause, HttpStatusCode.NotFound)

  data class ServerError(
    override val message: String = "Internal Server Error",
    override val cause: Throwable? = null
  ) : APIException(message, cause, HttpStatusCode.InternalServerError)

  fun asResponse() = APIErrorResponse(message, status.description, status.value)
}