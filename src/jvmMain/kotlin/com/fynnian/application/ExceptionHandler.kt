package com.fynnian.application

import com.benasher44.uuid.Uuid
import com.fynnian.application.common.APIErrorResponse
import com.fynnian.application.common.APIValidationErrorResponse
import com.fynnian.application.common.ErrorResponse
import com.fynnian.application.common.I18n
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

  data class ValidationError(
    override val message: String = "Validation failed, see the errors field for details",
    val errors: Map<String, I18n.TranslationKey>
  ) : APIException(message, null, HttpStatusCode.BadRequest) {
    override fun asResponse() = APIValidationErrorResponse(message, status.description, status.value, errors)
  }

  data class RoomNotFound(
    val code: String
  ) : APIException("Room with code '$code' not found", null, HttpStatusCode.NotFound)

  data class UserNotFound(
    val id: Uuid
  ) : APIException("User with id '$id' not found", null, HttpStatusCode.NotFound)

  open fun asResponse(): ErrorResponse = APIErrorResponse(message, status.description, status.value)
}