package com.fynnian.application.common


import com.benasher44.uuid.Uuid
import com.fynnian.application.common.room.RoomStatus

object URLS {

  const val ROOT = "/training"

  const val API_ROOT = "$ROOT/api"
  const val API_USERS = "$API_ROOT/users"
  const val API_USERS_BY_ID = "$API_ROOT/users/{id}"
  const val API_ROOMS = "$API_ROOT/rooms"
  const val API_ROOMS_BY_ID = "$API_ROOT/rooms/{code}"
  const val API_ROOMS_USER_STATUS = "$API_ROOT/rooms/{code}/users/{id}/status"
  const val API_ROOMS_USER_START = "$API_ROOT/rooms/{code}/users/{id}/start-room"
  const val API_ROOMS_USER_FINISH = "$API_ROOT/rooms/{code}/users/{id}/finish-room"
  const val API_ROOMS_USER_CLOSE = "$API_ROOT/rooms/{code}/users/{id}/close-room"
  const val API_ROOMS_USER_ANSWERS = "$API_ROOT/rooms/{code}/users/{id}/answers"
  const val API_ROOMS_USER_GROUP_INFO = "$API_ROOT/rooms/{code}/users/{id}/group-info"
  const val API_ROOMS_ANSWER_BY_ID = "$API_ROOT/rooms/{code}/answers/{id}"

  const val API_ROOMS_MANAGEMENT = "$API_ROOT/management/rooms"
  const val API_ROOMS_MANAGEMENT_BY_ID = "$API_ROOT/management/rooms/{code}"
  const val API_ROOMS_MANAGEMENT_ROOM_OPEN = "$API_ROOT/management/rooms/{code}/open-room"
  const val API_ROOMS_MANAGEMENT_ROOM_CLOSE = "$API_ROOT/management/rooms/{code}/close-room"

  const val API_ROOMS_MANAGEMENT_ROOM_IMAGE = "$API_ROOT/management/rooms/{code}/image"
  const val API_ROOMS_MANAGEMENT_ROOM_IMAGE_BY_ID = "$API_ROOT/management/rooms/{code}/image/{id}"
  const val API_ROOMS_MANAGEMENT_ROOM_INTRO = "$API_ROOT/management/rooms/{code}/intro"
  const val API_ROOMS_MANAGEMENT_ROOM_OUTRO = "$API_ROOT/management/rooms/{code}/outro"

  const val API_ROOMS_MANAGEMENT_EXCEL_EXPORT = "$API_ROOT/management/rooms/{code}/export"
  const val STATIC_ROOT = "$ROOT/static"
  const val STATIC_IMAGES_ROOT = "$STATIC_ROOT/images"
  const val STATIC_IMAGES_IMAGE = "$STATIC_ROOT/images/{imageName}"
  const val STATIC_VIDEOS_ROOT = "$STATIC_ROOT/videos"
  const val STATIC_VIDEOS_VIDEO = "$STATIC_ROOT/videos/{videoName}"

  const val HOME = "$ROOT/"
  const val ROOM = "$ROOT/room/{code}"
  const val MANAGEMENT = "$ROOT/management"
  const val MANAGEMENT_ROOM_DETAIL = "$ROOT/management/room/{code}"

  fun String.replaceParam(vararg params: Param) =
    params.fold(this) { u, param -> u.replace("{${param.key}}", param.value) }

  fun String.addQuerParams(vararg params: Param) =
    this + "?" + params.joinToString("&") { "${it.key}=${it.value}" }
  data class Param(val key: String, val value: String)

  val USER_ID_PARAM = { id: Uuid -> Param("id", id.toString()) }
  val ROOM_CODE_PARAM = { code: String -> Param("code", code) }
  val ANSWER_ID_PARAM = { id: Uuid -> Param("id", id.toString()) }
  val IMAGE_ID_PARAM = { id: Uuid -> Param("id", id.toString()) }
  val IMAGE_NAME_PARAM = { fileName: String -> Param("imageName", fileName) }
  val VIDEO_NAME_PARAM = { fileName: String -> Param("videoName", fileName) }
  val LANGUAGE_PARAM = { language: Language -> Param("language", language.toString()) }
  val ROOM_STATUS_PARAM = { status: RoomStatus -> Param("status", status.toString())}
}