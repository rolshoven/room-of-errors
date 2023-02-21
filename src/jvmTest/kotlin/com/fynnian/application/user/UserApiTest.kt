package com.fynnian.application.user

import com.fynnian.application.BaseTestSetup
import com.fynnian.application.common.URLS
import com.fynnian.application.common.URLS.USER_ID_PARAM
import com.fynnian.application.common.URLS.replaceParam
import com.fynnian.application.common.user.User
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.assertj.core.api.Assertions.assertThat
import java.util.UUID
import kotlin.test.Test


class UserApiTest : BaseTestSetup() {

  @Test
  fun `get user by id`() {
    runTestApplication { repository, client ->

      val expectedUser = repository.createUser()

      val response = client.get(URLS.API_USERS_BY_ID.replaceParam(USER_ID_PARAM(expectedUser.id)))
      response.statusIsOK()

      val user = response.body<User>()
      assertThat(user.id).isEqualTo(expectedUser.id)
      assertThat(user.profession).isEqualTo(expectedUser.profession)
    }
  }

  @Test
  fun `get user by id returns not found for invalid id`() {
    runTestApplication { _, client ->
      client
        .get(URLS.API_USERS_BY_ID.replaceParam(USER_ID_PARAM(UUID.randomUUID())))
        .statusIsNotFound()

    }
  }

  @Test
  fun `can create new user`() {
    runTestApplication { _, client ->

      val newUser = User(UUID.randomUUID(), "Some Profession")

      client
        .put(URLS.API_USERS_BY_ID.replaceParam(USER_ID_PARAM(newUser.id))) {
          contentType(ContentType.Application.Json)
          setBody(newUser)
        }
        .apply {
          statusIsOK()
          val user = body<User>()

          assertThat(user.id).isEqualTo(newUser.id)
          assertThat(user.profession).isEqualTo(newUser.profession)

        }
    }
  }
}