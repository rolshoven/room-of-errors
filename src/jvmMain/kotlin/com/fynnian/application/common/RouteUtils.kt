package com.fynnian.application.common

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import com.fynnian.application.APIException
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import java.io.InputStream

suspend fun ApplicationCall.processMultipartForm(
  acceptedTypes: Set<ContentType>,
  fileHandler: (fileStream: InputStream, contentType: ContentType) -> String
): MutableMap<String, String> {

  val fields = mutableMapOf<String, String>()

  receiveMultipart().forEachPart { part ->
    when (part) {
      is PartData.FormItem -> { fields[part.name ?: ""] = part.value }

      is PartData.FileItem -> {
        val fileType = part.contentType
        if (acceptedTypes.contains(fileType).not())
          throw APIException.BadRequest("Invalid type $fileType for field ${part.name} - allowed types are: $acceptedTypes")

        fields[part.name ?: ""] = fileHandler(part.streamProvider(), fileType!!)
      }

      else -> {}
    }
    part.dispose()
  }

  return fields
}

fun ApplicationCall.getUUIDParam(key: String): Uuid {
  parameters[key]?.let {
    try {
      return uuidFrom(it)
    } catch (e: Exception) {
      throw APIException.BadRequest("Invalid UUID format: $it")
    }
  } ?: throw APIException.BadRequest("Missing required path parameter: $key")
}

fun ApplicationCall.getPathParam(key: String): String {
  return parameters[key] ?: throw APIException.BadRequest("Missing required path parameter: $key")
}

fun ApplicationCall.getRequiredQueryParam(key: String): String =
  getQueryParam(key) ?: throw APIException.BadRequest("Missing required query parameter: $key")

fun ApplicationCall.getQueryParam(key: String): String? = parameters[key]


fun ApplicationCall.getRoomCodeParam(key: String): String {
  return getPathParam(key)
    .also { if (it.length != 8) throw APIException.BadRequest("The room code must be 8 characters") }
}

fun ApplicationCall.getRoomCodeParam(): String = getRoomCodeParam("code")
fun ApplicationCall.getUserIdParam(): Uuid = getUUIDParam("id")

fun ApplicationCall.getImageIdParam(): Uuid = getUUIDParam("id")
fun ApplicationCall.checkRequestIds(path: Uuid, payload: Uuid) = checkRequestIds(path.toString(), payload.toString())
fun ApplicationCall.checkRequestIds(path: String, payload: String) {
  if (path != payload) throw APIException.BadRequest("Path id $path doesn't match payload id $payload for resource")
}

/**
 * Get the optional `language` query param, if not set returns [Language.DE] as default
 */
fun ApplicationCall.getLanguageQueryParam(): Language {
  return getQueryParam("language")
    ?.let {
      try {
        Language.valueOf(it.uppercase())
      } catch (e: Exception) {
        throw APIException.BadRequest("the given value $it is not a valid language, use one of ${Language.values()}")
      }
    }
    ?: Language.DE
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