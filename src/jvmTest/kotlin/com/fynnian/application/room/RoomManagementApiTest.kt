package com.fynnian.application.room

import com.fynnian.application.BaseTestSetup
import com.fynnian.application.common.URLS
import com.fynnian.application.common.URLS.ROOM_CODE_PARAM
import com.fynnian.application.common.URLS.USER_ID_PARAM
import com.fynnian.application.common.URLS.replaceParam
import com.fynnian.application.common.room.RoomDetails
import com.fynnian.application.common.room.UsersRoomParticipationStatus
import com.fynnian.application.common.room.UsersRoomStatus
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class RoomManagementApiTest : BaseTestSetup() {

  @Test
  fun `get rooms to manage`() {
    runTestApplication { _, client ->
      val response = client.get(URLS.API_ROOMS_MANAGEMENT)
      response.statusIsOK()

      val list = response.body<List<RoomDetails>>()
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
  fun `create users room status on get request if it not exist`() = runTestApplication { repository, client ->

    val user = repository.createUser()
    val room = repository.createRoom()

    client
      .get(
        URLS.API_ROOMS_USER_STATUS.replaceParam(
          ROOM_CODE_PARAM(room.code),
          USER_ID_PARAM(user.id)
        )
      )
      .apply {
        statusIsOK()
        val response = body<UsersRoomStatus>()

        assertThat(response.userId).isEqualTo(user.id)
        assertThat(response.roomCode).isEqualTo(room.code)
        assertThat(response.participationStatus).isEqualTo(UsersRoomParticipationStatus.NOT_STARTED)
      }
  }

  @Test
  fun `user can start a room`() = runTestApplication { repository, client ->

    val user = repository.createUser()
    val room = repository.createRoom()

    client
      .post(
        URLS.API_ROOMS_USER_START.replaceParam(
          ROOM_CODE_PARAM(room.code),
          USER_ID_PARAM(user.id)
        )
      )
      .apply {
        statusIsOK()
        val response = body<UsersRoomStatus>()

        assertThat(response.userId).isEqualTo(user.id)
        assertThat(response.roomCode).isEqualTo(room.code)
        assertThat(response.participationStatus).isEqualTo(UsersRoomParticipationStatus.STARTED)
      }
  }

  @Test
  fun `user can finish a room`() = runTestApplication { repository, client ->

    val user = repository.createUser()
    val room = repository.createRoom()

    client
      .post(
        URLS.API_ROOMS_USER_FINISH.replaceParam(
          ROOM_CODE_PARAM(room.code),
          USER_ID_PARAM(user.id)
        )
      )
      .apply {
        statusIsOK()
        val response = body<UsersRoomStatus>()

        assertThat(response.userId).isEqualTo(user.id)
        assertThat(response.roomCode).isEqualTo(room.code)
        assertThat(response.participationStatus).isEqualTo(UsersRoomParticipationStatus.FINISHED)
      }
  }
}