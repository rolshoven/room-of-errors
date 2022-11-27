package api

import com.benasher44.uuid.Uuid
import com.fynnian.application.common.AppPaths
import com.fynnian.application.common.room.Answer
import com.fynnian.application.common.room.Room
import com.fynnian.application.common.user.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

open class Api {
  val client = HttpClient {
    install(ContentNegotiation) {
      json()
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

  suspend fun getAnswers(code: String, user: User): List<Answer> {
    val response = client.get("$basePath/$code/answers")
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