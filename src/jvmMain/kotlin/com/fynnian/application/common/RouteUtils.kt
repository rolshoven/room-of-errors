package com.fynnian.application.common

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import com.fynnian.application.APIException
import io.ktor.server.application.*

fun ApplicationCall.getUUIDParam(key: String): Uuid {
  parameters[key]?.let {
    try {
      return uuidFrom(it)
    } catch (e: Exception) {
      throw APIException.BadRequest("Invalid UUID format: $it")
    }
  } ?: throw APIException.BadRequest("Missing required path parameter $key")
}