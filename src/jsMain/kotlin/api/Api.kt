package api

import com.benasher44.uuid.Uuid
import com.fynnian.application.common.APIErrorResponse
import com.fynnian.application.common.URLS.ANSWER_ID_PARAM
import com.fynnian.application.common.URLS.API_ROOMS_ANSWER_BY_ID
import com.fynnian.application.common.URLS.API_ROOMS_BY_ID
import com.fynnian.application.common.URLS.API_ROOMS_MANAGEMENT
import com.fynnian.application.common.URLS.API_ROOMS_MANAGEMENT_BY_ID
import com.fynnian.application.common.URLS.API_ROOMS_MANAGEMENT_ROOM_CLOSE
import com.fynnian.application.common.URLS.API_ROOMS_MANAGEMENT_ROOM_IMAGE
import com.fynnian.application.common.URLS.API_ROOMS_MANAGEMENT_ROOM_IMAGE_BY_ID
import com.fynnian.application.common.URLS.API_ROOMS_MANAGEMENT_ROOM_INTRO
import com.fynnian.application.common.URLS.API_ROOMS_MANAGEMENT_ROOM_OPEN
import com.fynnian.application.common.URLS.API_ROOMS_MANAGEMENT_ROOM_OUTRO
import com.fynnian.application.common.URLS.API_ROOMS_USER_ANSWERS
import com.fynnian.application.common.URLS.API_ROOMS_USER_CLOSE
import com.fynnian.application.common.URLS.API_ROOMS_USER_FINISH
import com.fynnian.application.common.URLS.API_ROOMS_USER_START
import com.fynnian.application.common.URLS.API_ROOMS_USER_STATUS
import com.fynnian.application.common.URLS.API_USERS_BY_ID
import com.fynnian.application.common.URLS.IMAGE_ID_PARAM
import com.fynnian.application.common.URLS.ROOM_CODE_PARAM
import com.fynnian.application.common.URLS.ROOM_STATUS_PARAM
import com.fynnian.application.common.URLS.USER_ID_PARAM
import com.fynnian.application.common.URLS.addQuerParams
import com.fynnian.application.common.URLS.replaceParam
import com.fynnian.application.common.room.*
import com.fynnian.application.common.user.User
import components.APIResponseSnackbarContext
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import js.typedarrays.Int8Array
import kotlinx.coroutines.await
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import react.StateSetter
import react.useContext
import web.file.File
import kotlin.math.floor

open class Api {
  val client = HttpClient {
    install(ContentNegotiation) {
      json(Json {
        ignoreUnknownKeys = true
      })
    }
  }

  val snackbarContext = useContext(APIResponseSnackbarContext)
  fun showError(error: APIErrorResponse?) {
    snackbarContext.apiErrorResponse(error)
    snackbarContext.showSnackbar(true)
  }

  // ToDo: handle error better, currently a mix of showing snack bar and returning null
  suspend inline fun <reified T> processSimpleCall(
    displayError: Boolean = true,
    request: HttpClient.() -> HttpResponse
  ): T? {
    val response = client.request().call.response
    return if (response.status == HttpStatusCode.OK) {
      snackbarContext.apiErrorResponse(null)
      response.body<T>()
    } else {
      if (displayError) showError(response.bodyAsText().run {
        try {
          Json.decodeFromString(this)
        } catch (e: Exception) {
          APIErrorResponse(this, response.status.description, response.status.value)
        }
      })
      null
    }
  }

  // ToDo: handle error better, currently a mix of showing snack bar and returning null
  suspend inline fun processDeleteCall(
    displayError: Boolean = true,
    request: HttpClient.() -> HttpResponse
  ): APIErrorResponse? {
    val response = client.request()
    return if (response.status == HttpStatusCode.OK) {
      snackbarContext.apiErrorResponse(null)
      null
    } else {
      if (displayError) showError(response.bodyAsText().run {
        try {
          Json.decodeFromString(this)
        } catch (e: Exception) {
          APIErrorResponse(this, response.status.description, response.status.value)
        }
      })
      response.body<APIErrorResponse>()
    }
  }
}

class UserApi : Api() {

  suspend fun getUser(id: Uuid): User? {
    return processSimpleCall(displayError = false) { get(API_USERS_BY_ID.replaceParam(USER_ID_PARAM(id))) }
  }

  suspend fun upsertUser(user: User): User? {
    return processSimpleCall(displayError = false) {
      put(API_USERS_BY_ID.replaceParam(USER_ID_PARAM(user.id))) {
        contentType(ContentType.Application.Json)
        setBody(user)
      }
    }
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
        USER_ID_PARAM(user.id)
      )
    )
    return if (response.status == HttpStatusCode.OK) response.body()
    else null
  }

  suspend fun startRoom(code: String, user: User): UsersRoomStatus? {
    val response = client.post(
      API_ROOMS_USER_START.replaceParam(
        ROOM_CODE_PARAM(code),
        USER_ID_PARAM(user.id)
      )
    )
    return if (response.status == HttpStatusCode.OK) response.body()
    else null
  }

  suspend fun finishRoom(code: String, user: User): UsersRoomStatus? {
    return processSimpleCall {
      post(
        API_ROOMS_USER_FINISH.replaceParam(
          ROOM_CODE_PARAM(code),
          USER_ID_PARAM(user.id)
        )
      )
    }
  }

  suspend fun closeRoom(code: String, user: User): UsersRoomStatus? {
    return processSimpleCall {
      post(
        API_ROOMS_USER_CLOSE.replaceParam(
          ROOM_CODE_PARAM(code),
          USER_ID_PARAM(user.id)
        )
      )
    }
  }

  suspend fun getAnswers(code: String, user: User): List<Answer> {
    val response = client.get(
      API_ROOMS_USER_ANSWERS.replaceParam(
        ROOM_CODE_PARAM(code),
        USER_ID_PARAM(user.id)
      )
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

  suspend fun getRooms(status: RoomStatus?): List<RoomManagementDetail> {
    return processSimpleCall {
      get(
        if (status == null) API_ROOMS_MANAGEMENT
        else API_ROOMS_MANAGEMENT.addQuerParams(ROOM_STATUS_PARAM(status))
      )
    }
      ?: emptyList()
  }

  suspend fun getRoom(code: String): RoomManagementDetail? {
    return processSimpleCall { get(API_ROOMS_MANAGEMENT_BY_ID.replaceParam(ROOM_CODE_PARAM(code))) }
  }

  suspend fun createRoom(room: RoomCreation): RoomManagementDetail? {
    return processSimpleCall {
      post(API_ROOMS_MANAGEMENT_BY_ID.replaceParam(ROOM_CODE_PARAM(room.code))) {
        contentType(ContentType.Application.Json)
        setBody(room)
      }
    }
  }

  suspend fun patchRoom(patch: RoomPatch): RoomManagementDetail? {
    return processSimpleCall {
      patch(API_ROOMS_MANAGEMENT_BY_ID.replaceParam(ROOM_CODE_PARAM(patch.code))) {
        contentType(ContentType.Application.Json)
        setBody(patch)
      }
    }
  }

  suspend fun getRoomImages(code: String): List<RoomImage> {
    return processSimpleCall { get(API_ROOMS_MANAGEMENT_ROOM_IMAGE.replaceParam(ROOM_CODE_PARAM(code))) } ?: emptyList()
  }

  suspend fun deleteRoomImage(code: String, imageId: Uuid): APIErrorResponse? {
    return processDeleteCall {
      delete(
        API_ROOMS_MANAGEMENT_ROOM_IMAGE_BY_ID.replaceParam(
          ROOM_CODE_PARAM(code),
          IMAGE_ID_PARAM(imageId)
        )
      )
    }
  }

  suspend fun addRoomImageWithUpload(
    roomCode: String,
    imageTitle: String,
    image: File
  ): RoomImage? {
    return processSimpleCall {
      val imageByteArray = image.arrayBuffer().await().let { Int8Array(it).unsafeCast<ByteArray>() }
      post(API_ROOMS_MANAGEMENT_ROOM_IMAGE.replaceParam(ROOM_CODE_PARAM(roomCode))) {
        setBody(
          MultiPartFormDataContent(
            formData {
              append(RoomImage.TITLE_FORM_PARAM, imageTitle)
              append(RoomImage.FILE_FORM_PARAM, imageByteArray, Headers.build {
                append(HttpHeaders.ContentType, "image/png")
                append(HttpHeaders.ContentDisposition, "filename=\"${imageTitle}.png\"")
              })
            },
            boundary = "WebAppBoundary"
          )
        )
      }
    }
  }

  suspend fun upsertRoomInteractionInfoWithUpload(
    variant: RoomStatementVariant,
    roomCode: String,
    text: String?,
    videoTitle: String?,
    video: File?,
    setProgress: StateSetter<Int>
  ): RoomManagementDetail? {
    return processSimpleCall {
      val url = when (variant) {
        RoomStatementVariant.INTRO -> API_ROOMS_MANAGEMENT_ROOM_INTRO
        RoomStatementVariant.OUTRO -> API_ROOMS_MANAGEMENT_ROOM_OUTRO
      }
      post(url.replaceParam(ROOM_CODE_PARAM(roomCode))) {
        if (videoTitle == null || video == null) {
          contentType(ContentType.Application.Json)
          setBody(
            RoomInteractionInfo(
              text = text,
              videoTitle = null,
              videoURl = null
            )
          )
        } else {
          val videoByteArray = video.arrayBuffer().await().let { Int8Array(it).unsafeCast<ByteArray>() }
          setBody(
            MultiPartFormDataContent(
              formData {
                text?.let { append(RoomInteractionInfo.TEXT_FORM_PARAM, it) }
                append(RoomInteractionInfo.VIDEO_TITLE_FORM_PARAM, videoTitle)
                append(RoomInteractionInfo.VIDEO_FORM_PARAM, videoByteArray, Headers.build {
                  append(HttpHeaders.ContentType, video.type)
                  append(HttpHeaders.ContentDisposition, "filename=\"${videoTitle}.${video.type}\"")
                })
              },
              boundary = "WebAppBoundary"
            )
          )
        }
        onUpload { bytesSentTotal, contentLength ->
          val pct = bytesSentTotal / (contentLength / 100.0)
          val part = floor((pct + 5 - 1) / 5) * 5
          setProgress(part.toInt())
        }
      }
    }
  }


  suspend fun openRoom(code: String): RoomManagementDetail? {
    return processSimpleCall { post(API_ROOMS_MANAGEMENT_ROOM_OPEN.replaceParam(ROOM_CODE_PARAM(code))) }
  }

  suspend fun closeRoom(code: String): RoomManagementDetail? {
    return processSimpleCall { post(API_ROOMS_MANAGEMENT_ROOM_CLOSE.replaceParam(ROOM_CODE_PARAM(code))) }
  }

  suspend fun deleteRoom(code: String): APIErrorResponse? {
    return processDeleteCall { delete(API_ROOMS_MANAGEMENT_BY_ID.replaceParam(ROOM_CODE_PARAM(code))) }
  }
}