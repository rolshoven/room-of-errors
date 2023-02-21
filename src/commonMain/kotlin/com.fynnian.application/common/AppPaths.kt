package com.fynnian.application.common

import com.benasher44.uuid.Uuid

object URLS {
  const val API_ROOT = "/api"
  const val API_USERS = "$API_ROOT/users"
  const val API_USERS_BY_ID = "$API_ROOT/users/{id}"
  const val API_ROOMS = "$API_ROOT/rooms"
  const val API_ROOMS_BY_ID = "$API_ROOT/rooms/{code}"
  const val API_ROOMS_USER_STATUS = "$API_ROOT/rooms/{code}/users/{id}/status"
  const val API_ROOMS_USER_START = "$API_ROOT/rooms/{code}/users/{id}/start-room"
  const val API_ROOMS_USER_FINISH = "$API_ROOT/rooms/{code}/users/{id}/finish-room"
  const val API_ROOMS_USER_ANSWERS = "$API_ROOT/rooms/{code}/users/{id}/answers"
  const val API_ROOMS_ANSWER_BY_ID = "$API_ROOT/rooms/{code}/answers/{id}"
  const val API_ROOMS_MANAGEMENT = "$API_ROOT/management/rooms"
  const val API_ROOMS_MANAGEMENT_BY_ID = "$API_ROOT/management/rooms/{code}"
  const val API_ROOMS_MANAGEMENT_EXCEL_EXPORT = "$API_ROOT/management/rooms/{code}/export"
  const val STATIC_ROOT = "/static"
  const val STATIC_IMAGES_ROOT = "/static/images"
  const val STATIC_IMAGES_IMAGE = "/static/images/{imageName}"

  const val HOME = "/"
  const val ROOM = "/room/{code}"
  const val MANAGEMENT = "/management"

  fun String.replaceParam(vararg params: Param) =
    params.fold(this) { u, param -> u.replace("{${param.key}}", param.value) }

  data class Param(val key: String, val value: String)

  val USER_ID_PARAM = { id: Uuid -> Param("id", id.toString()) }
  val ROOM_CODE_PARAM = { code: String -> Param("code", code) }
  val ANSWER_ID_PARAM = { id: Uuid -> Param("id", id.toString()) }
  val IMAGE_NAME_PARAM = { fileName: String -> Param("imageName", fileName) }
}