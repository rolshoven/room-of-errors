package api

import com.benasher44.uuid.Uuid
import com.fynnian.application.common.AppPaths
import com.fynnian.application.common.room.Answer
import com.fynnian.application.common.room.Room
import com.fynnian.application.common.room.RoomDetails
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

  private val basePath: String = listOf(AppPaths.API_ROOT, AppPaths.API_USERS).joinToString("") { it.path }

  suspend fun getUser(id: Uuid): User? {
    val response = client.get("$basePath/$id").call.response
    return if (response.status == HttpStatusCode.OK) response.body<User>()
    else null
  }

  suspend fun upsertUser(user: User): User? {
    val response = client.put(basePath + "/${user.id}") {
      contentType(ContentType.Application.Json)
      setBody(user)
    }.call.response

    return if (response.status == HttpStatusCode.OK) response.body<User>()
    else null
  }

}

class RoomApi : Api() {
  private val basePath: String = listOf(AppPaths.API_ROOT, AppPaths.API_ROOMS).joinToString("") { it.path }

  suspend fun getRoom(code: String): Room? {
    val response = client.get("$basePath/$code").call.response
    return if (response.status == HttpStatusCode.OK) response.body()
    else null
  }

  suspend fun getAnswers(roomCode: String, user: User): List<Answer> {
    val response = client.get("$basePath/$roomCode/answers") {
      parameter("userId", user.id)
    }
    return if (response.status == HttpStatusCode.OK) response.body()
    else emptyList()
  }

  suspend fun upsertAnswer(code: String, answer: Answer): Answer? {
    val response = client.put("$basePath/$code/answers/${answer.id}") {
      contentType(ContentType.Application.Json)
      setBody(answer)
    }
    return if (response.status == HttpStatusCode.OK) response.body()
    else null
  }

  //ToDo: better error handling
  suspend fun deleteAnswer(code: String, answerId: Uuid): HttpStatusCode {
    val response = client.delete("$basePath/$code/answers/$answerId")
    return if (response.status == HttpStatusCode.OK) HttpStatusCode.OK
    else HttpStatusCode.NotFound
  }
}

class RoomManagementApi : Api() {
  companion object {
    val basePath: String =
      listOf(AppPaths.API_ROOT, AppPaths.API_ROOMS, AppPaths.API_ROOMS_MANAGEMENT).joinToString("") { it.path }
    val roomExportUrl = { roomCode: String -> "$basePath/$roomCode/export" }
  }

  suspend fun getRooms(): List<RoomDetails> {
    val response = client.get(basePath)
    return if (response.status == HttpStatusCode.OK) response.body()
    else emptyList()
  }

  suspend fun getRoom(code: String): Room? {
    val response = client.get("$basePath/$code")
    return if (response.status == HttpStatusCode.OK) response.body()
    else null
  }

  suspend fun upsertRoom(room: Room): Room? {
    val response = client.put("$basePath/${room.code}") {
      contentType(ContentType.Application.Json)
      setBody(room)
    }
    return if (response.status == HttpStatusCode.OK) response.body()
    else null
  }

  //ToDo: Proper
  suspend fun createRoomWithUpload(room: Room, image: File): Room? {
    val imageByteArray = image.arrayBuffer().await().let { Int8Array(it).unsafeCast<ByteArray>() }
    val response = client.put("$basePath/${room.code}") {
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
    val response = client.delete("$basePath/$code")
    return if (response.status == HttpStatusCode.OK) HttpStatusCode.OK
    else HttpStatusCode.NotFound
  }
}