package com.fynnian.application.room

import com.fynnian.application.BaseTestSetup
import com.fynnian.application.common.URLS
import com.fynnian.application.common.URLS.ROOM_CODE_PARAM
import com.fynnian.application.common.URLS.USER_ID_PARAM
import com.fynnian.application.common.URLS.replaceParam
import com.fynnian.application.common.room.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class RoomApiTest : BaseTestSetup() {

  @Test
  fun `get room`() = runTestApplication { repository, client ->

    val room = repository.createRoom()

    client
      .get(URLS.API_ROOMS_BY_ID.replaceParam(ROOM_CODE_PARAM(room.code)))
      .apply {
        statusIsOK()
        val response = body<Room>()

        assertThat(response.code).isEqualTo(room.code)
        assertThat(response.roomStatus).isEqualTo(RoomStatus.OPEN)
        assertThat(response.title).isNotBlank
        assertThat(response.description).isNotBlank
        assertThat(response.question).isNotBlank
        assertThat(response.startingStatements).isNotNull
        assertThat(response.endingStatements).isNotNull
        assertThat(response.images).hasSizeGreaterThanOrEqualTo(1)
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