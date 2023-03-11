package com.fynnian.application.room

import com.fynnian.application.BaseTestSetup
import com.fynnian.application.common.URLS
import com.fynnian.application.common.URLS.ROOM_CODE_PARAM
import com.fynnian.application.common.URLS.replaceParam
import com.fynnian.application.common.room.Room
import com.fynnian.application.common.room.RoomCreation
import com.fynnian.application.common.room.RoomManagementDetail
import com.fynnian.application.common.room.RoomStatus
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class RoomManagementApiTest : BaseTestSetup() {

  @Test
  fun `get rooms to manage`() {
    runTestApplication { repository, client ->

      repository.createRoom()

      val response = client.get(URLS.API_ROOMS_MANAGEMENT)
      response.statusIsOK()

      val list = response.body<List<RoomManagementDetail>>()
      assertThat(list.size).isGreaterThan(0)
    }
  }

  @Test
  fun `return 404 not found for code of room that does not exists`() {
    runTestApplication { _, client ->
      val response = client.get(URLS.API_ROOMS_MANAGEMENT_BY_ID.replaceParam(ROOM_CODE_PARAM("12345678")))
      response.statusIsNotFound()
    }
  }

  @Test
  fun `create new room`() = runTestApplication { _, client ->

    val newRoom = RoomCreation("ab12rf34", "room creation test")

    client.post(URLS.API_ROOMS_MANAGEMENT_BY_ID.replaceParam(ROOM_CODE_PARAM(newRoom.code))) {
      contentType(ContentType.Application.Json)
      setBody(newRoom)
    }
      .apply {
        statusIsOK()

        val response = body<Room>()

        assertThat(response.code).isEqualTo(newRoom.code)
        assertThat(response.roomStatus).isEqualTo(RoomStatus.NOT_READY)
        assertThat(response.title).isEqualTo(newRoom.title)
        assertThat(response.description).isNull()
        assertThat(response.question).isNull()
        assertThat(response.timeLimitMinutes).isNull()
        assertThat(response.intro.text).isNull()
        assertThat(response.intro.videoTitle).isNull()
        assertThat(response.intro.videoTitle).isNull()
        assertThat(response.outro.text).isNull()
        assertThat(response.outro.videoTitle).isNull()
        assertThat(response.outro.videoTitle).isNull()
        assertThat(response.images).hasSize(0)
      }
  }

  @Test
  fun `check that code in path and payload match when creating a new room`() = runTestApplication { _, client ->

    val newRoom = RoomCreation("ab12rf34", "room creation test")

    client.post(URLS.API_ROOMS_MANAGEMENT_BY_ID.replaceParam(ROOM_CODE_PARAM("12345678"))) {
      contentType(ContentType.Application.Json)
      setBody(newRoom)
    }
      .apply { statusIsBadRequest() }
  }

}