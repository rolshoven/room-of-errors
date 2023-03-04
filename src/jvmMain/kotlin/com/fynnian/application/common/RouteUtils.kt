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
  } ?: throw APIException.BadRequest("Missing required path parameter: $key")
}

fun ApplicationCall.getStringParam(key: String): String {
  return parameters[key] ?: throw APIException.BadRequest("Missing required path parameter: $key")
}

fun ApplicationCall.getRoomCodeParam(key: String): String {
  return getStringParam(key)
    .also { if (it.length != 8) throw APIException.BadRequest("The room code must be 8 characters") }
}

fun ApplicationCall.getRoomCodeParam(): String = getRoomCodeParam("code")
fun ApplicationCall.getUserIdParam(): Uuid = getUUIDParam("id")

fun ApplicationCall.getImageIdParam(): Uuid = getUUIDParam("id")
fun ApplicationCall.checkRequestIds(path: Uuid, payload: Uuid) = checkRequestIds(path.toString(), payload.toString())
fun ApplicationCall.checkRequestIds(path: String, payload: String) {
  if (path != payload) throw APIException.BadRequest("Path id $path doesn't match payload id $payload for resource")
}

fun ApplicationCall.getUUIDQueryParam(key: String): Uuid {
  request.queryParameters[key]?.let {
    try {
      return uuidFrom(it)
    } catch (e: Exception) {
      throw APIException.BadRequest("Invalid UUID format: $it")
    }
  } ?: throw APIException.BadRequest("Missing required query parameter: $key")
}