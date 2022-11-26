package api

import com.benasher44.uuid.Uuid
import com.fynnian.application.common.AppPaths
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
}