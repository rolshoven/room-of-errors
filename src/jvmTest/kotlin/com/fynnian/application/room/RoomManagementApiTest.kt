package com.fynnian.application.room

import com.fynnian.application.BaseTestSetup
import com.fynnian.application.common.URLS
import com.fynnian.application.common.URLS.replaceParam
import com.fynnian.application.common.room.RoomDetails
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class RoomManagementApiTest : BaseTestSetup() {

  @Test
  fun `get rooms to manage`() {
    runTestApplication { client ->
      val response = client.get(URLS.API_ROOMS_MANAGEMENT)
      response.statusIsOK()

      val list = response.body<List<RoomDetails>>()
      assertThat(list.size).isGreaterThan(0)
    }
  }

  @Test
  fun `return 404 not found for code of room that does not exists`() {
    runTestApplication { client ->
      val response = client.get(URLS.API_ROOMS_MANAGEMENT_BY_ID.replaceParam(URLS.Param("code", "12345678")))
      response.statusIsNotFound()
    }
  }
}