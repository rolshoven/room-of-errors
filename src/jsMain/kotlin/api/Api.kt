package api

import com.benasher44.uuid.Uuid
import com.fynnian.application.common.URLS.ANSWER_ID_PARAM
import com.fynnian.application.common.URLS.API_ROOMS_USER_ANSWERS
import com.fynnian.application.common.URLS.API_ROOMS_ANSWER_BY_ID
import com.fynnian.application.common.URLS.API_ROOMS_BY_ID
import com.fynnian.application.common.URLS.API_ROOMS_MANAGEMENT
import com.fynnian.application.common.URLS.API_ROOMS_MANAGEMENT_BY_ID
import com.fynnian.application.common.URLS.API_ROOMS_USER_FINISH
import com.fynnian.application.common.URLS.API_ROOMS_USER_START
import com.fynnian.application.common.URLS.API_ROOMS_USER_STATUS
import com.fynnian.application.common.URLS.API_USERS_BY_ID
import com.fynnian.application.common.URLS.ROOM_CODE_PARAM
import com.fynnian.application.common.URLS.USER_ID_PARAM
import com.fynnian.application.common.URLS.replaceParam
import com.fynnian.application.common.room.Answer
import com.fynnian.application.common.room.Room
import com.fynnian.application.common.room.RoomDetails
import com.fynnian.application.common.room.UsersRoomStatus
import com.fynnian.application.common.user.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import js.typedarrays.Int8Array
import kotlinx.coroutines.await
import kotlinx.serialization.json.Json
import web.file.File

open class Api {
  val client = HttpClient {
    install(ContentNegotiation) {
      json(Json {
        ignoreUnknownKeys = true
      })
    }
  }
}

class UserApi : Api() {

  suspend fun getUser(id: Uuid): User? {
    val response = client.get(API_USERS_BY_ID.replaceParam(USER_ID_PARAM(id))).call.response
    return if (response.status == HttpStatusCode.OK) response.body<User>()
    else null
  }

  suspend fun upsertUser(user: User): User? {
    val response = client.put(API_USERS_BY_ID.replaceParam(USER_ID_PARAM(user.id))) {
      contentType(ContentType.Application.Json)
      setBody(user)
    }.call.response

    return if (response.status == HttpStatusCode.OK) response.body<User>()
    else null
  }

}

class RoomApi : Api() {

  suspend fun getRoom(code: String): Room? {
    val response = client.get(API_ROOMS_BY_ID.replaceParam(ROOM_CODE_PARAM(code))).call.response
    return if (response.status == HttpStatusCode.OK) response.body()
    else null
  }

  suspend fun getUsersRoomStatus(code: String, user: User): UsersRoomStatus? {
    val response = client.get(
      API_ROOMS_USER_STATUS.replaceParam(
        ROOM_CODE_PARAM(code),
        USER_ID_PARAM(user.id))
    )
    return if (response.status == HttpStatusCode.OK) response.body()
    else null
  }

  suspend fun startRoom(code: String, user: User): UsersRoomStatus? {
    val response = client.post(
      API_ROOMS_USER_START.replaceParam(
        ROOM_CODE_PARAM(code),
        USER_ID_PARAM(user.id))
    )
    return if (response.status == HttpStatusCode.OK) response.body()
    else null
  }
  suspend fun finishRoom(code: String, user: User): UsersRoomStatus? {
    val response = client.post(
      API_ROOMS_USER_FINISH.replaceParam(
        ROOM_CODE_PARAM(code),
        USER_ID_PARAM(user.id))
    )
    return if (response.status == HttpStatusCode.OK) response.body()
    else null
  }

  suspend fun getAnswers(code: String, user: User): List<Answer> {
    val response = client.get(
      API_ROOMS_USER_ANSWERS.replaceParam(
        ROOM_CODE_PARAM(code),
        USER_ID_PARAM(user.id))
    )
    return if (response.status == HttpStatusCode.OK) response.body()
    else emptyList()
  }

  suspend fun upsertAnswer(code: String, answer: Answer): Answer? {
    val response = client.put(
      API_ROOMS_ANSWER_BY_ID.replaceParam(
        ROOM_CODE_PARAM(code),
        ANSWER_ID_PARAM(answer.id)
      )
    ) {
      contentType(ContentType.Application.Json)
      setBody(answer)
    }
    return if (response.status == HttpStatusCode.OK) response.body()
    else null
  }

  //ToDo: better error handling
  suspend fun deleteAnswer(code: String, answerId: Uuid): HttpStatusCode {
    val response = client.delete(
      API_ROOMS_ANSWER_BY_ID.replaceParam(
        ROOM_CODE_PARAM(code),
        ANSWER_ID_PARAM(answerId)
      )
    )
    return if (response.status == HttpStatusCode.OK) HttpStatusCode.OK
    else HttpStatusCode.NotFound
  }
}

class RoomManagementApi : Api() {

  suspend fun getRooms(): List<RoomDetails> {
    val response = client.get(API_ROOMS_MANAGEMENT)
    return if (response.status == HttpStatusCode.OK) response.body()
    else emptyList()
  }

  suspend fun getRoom(code: String): Room? {
    val response = client.get(API_ROOMS_MANAGEMENT_BY_ID.replaceParam(ROOM_CODE_PARAM(code)))
    return if (response.status == HttpStatusCode.OK) response.body()
    else null
  }

  suspend fun upsertRoom(room: Room): Room? {
    val response = client.put(API_ROOMS_MANAGEMENT_BY_ID.replaceParam(ROOM_CODE_PARAM(room.code))) {
      contentType(ContentType.Application.Json)
      setBody(room)
    }
    return if (response.status == HttpStatusCode.OK) response.body()
    else null
  }

  //ToDo: Proper
  suspend fun createRoomWithUpload(room: Room, image: File): Room? {
    val imageByteArray = image.arrayBuffer().await().let { Int8Array(it).unsafeCast<ByteArray>() }
    val response = client.put(API_ROOMS_MANAGEMENT_BY_ID.replaceParam(ROOM_CODE_PARAM(room.code))) {
      setBody(
        MultiPartFormDataContent(
          formData {
            append("code", room.code)
            append("title", room.title)
            append("description", room.description)
            append("question", room.question)
            append("imageTitle", room.images.first().title)

            append("image", imageByteArray, Headers.build {
              append(HttpHeaders.ContentType, "image/png")
              append(HttpHeaders.ContentDisposition, "filename=\"${room.images.first().title}.png\"")
            })
          },
          boundary = "WebAppBoundary"
        )
      )
    }
    return if (response.status == HttpStatusCode.OK) response.body()
    else null
  }

  suspend fun deleteRoom(code: String): HttpStatusCode {
    val response = client.delete(API_ROOMS_MANAGEMENT_BY_ID.replaceParam(ROOM_CODE_PARAM(code)))
    return if (response.status == HttpStatusCode.OK) HttpStatusCode.OK
    else HttpStatusCode.NotFound
  }
}