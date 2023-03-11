package com.fynnian.application

import com.fynnian.application.common.Repository
import com.fynnian.application.common.room.Room
import com.fynnian.application.common.room.RoomImage
import com.fynnian.application.common.room.RoomStatements
import com.fynnian.application.common.room.RoomStatus
import com.fynnian.application.common.user.User
import com.fynnian.application.config.DataSource
import com.fynnian.application.jooq.tables.references.USERS
import com.fynnian.application.room.toRecord
import com.fynnian.application.user.toDomain
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.ObjectAssert
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeAll
import java.util.*

abstract class BaseTestSetup {

  fun runTestApplication(
    block: suspend (
      repository: TestRepository,
      client: HttpClient
    ) -> Unit
  ) {
    testApplication {
      environment {
        // set null as path to load form the default path i.e. from the main resources
        config = ApplicationConfig(null).mergeWith(
          MapApplicationConfig(
            "ktor.datasource.driver" to testDataSource.driver,
            "ktor.datasource.schema" to testDataSource.schema,
            "ktor.datasource.url" to testDataSource.url,
            "ktor.datasource.user" to testDataSource.user,
            "ktor.datasource.password" to testDataSource.password,
          )
        )
      }

      val client = createClient {
        install(ContentNegotiation) {
          json()
        }
      }
      block(TestRepository(testDataSource), client)
    }
  }

  suspend fun HttpResponse.statusIs(status: HttpStatusCode): ObjectAssert<HttpStatusCode> =
    assertThat(this.status)
      .withFailMessage(
        """
        Expected: '$status' but was '${this.status}'
        response: 
        ${bodyAsText()}
      """.trimIndent()
      )
      .isEqualTo(status)

  suspend fun HttpResponse.statusIsOK(): ObjectAssert<HttpStatusCode> = statusIs(HttpStatusCode.OK)
  suspend fun HttpResponse.statusIsNotFound(): ObjectAssert<HttpStatusCode> = statusIs(HttpStatusCode.NotFound)
  suspend fun HttpResponse.statusIsBadRequest(): ObjectAssert<HttpStatusCode> = statusIs(HttpStatusCode.BadRequest)


  companion object {

    private val testDataSource = DataSource(
      mapOf(
        "driver" to "org.h2.Driver",
        "schema" to "room_of_horrors",
        "url" to "jdbc:h2:file:./build/flyway/test_horrors_db",
        "user" to "test",
        "password" to "test",
      )
    )

    @BeforeAll
    @JvmStatic
    fun testDBSetup() {
      val flyway = Flyway.configure()
        .dataSource(testDataSource.url, testDataSource.user, testDataSource.password)
        .schemas(testDataSource.schema)
        .createSchemas(true)
        .cleanDisabled(false)
        .load()

      flyway.clean()
      flyway.migrate()
    }
  }
}


class TestRepository(dataSource: DataSource) : Repository(dataSource) {

  fun createUser(): User = jooq {
    newRecord(USERS)
      .apply {
        id = UUID.randomUUID()
        profession = "Nurse"
        store()
      }.toDomain()
  }

  fun createRoom(code: String = Room.genRoomCode()): Room = jooq {
    val room = Room(
      code,
      RoomStatus.OPEN,
      "Room Title",
      "Room Description",
      "Room Question",
      0,
      RoomStatements(
        "startingText",
        "startingVideoTitle",
        "startingVideoUrl"
      ),
      RoomStatements(
        "endingText",
        "endingVideoTitle",
        "endingVideoUrl"
      ),
      listOf(
        RoomImage(
          UUID.randomUUID(),
          "https://example.com",
          "Image Title"
        )
      )
    )
    batchInsert(
      room.toRecord(),
      *room.images.map { it.toRecord(room.code) }.toTypedArray()
    ).execute()
    room
  }
}