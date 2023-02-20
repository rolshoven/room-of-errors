package com.fynnian.application

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.ObjectAssert

abstract class BaseTestSetup {

  fun runTestApplication(block: suspend (client: HttpClient) -> Unit) {
    testApplication {
      val client = createClient {
        install(ContentNegotiation) {
          json()
        }
      }
      block(client)
    }
  }

  fun HttpResponse.statusIs(status: HttpStatusCode): ObjectAssert<HttpStatusCode> = assertThat(this.status).isEqualTo(status)
  fun HttpResponse.statusIsOK(): ObjectAssert<HttpStatusCode> = statusIs(HttpStatusCode.OK)
  fun HttpResponse.statusIsNotFound(): ObjectAssert<HttpStatusCode> = statusIs(HttpStatusCode.NotFound)
}