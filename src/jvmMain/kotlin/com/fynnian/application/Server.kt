package com.fynnian.application

import com.fynnian.application.common.URLS
import com.fynnian.application.config.AppConfig
import com.fynnian.application.config.DI
import com.fynnian.application.config.FlywayConfig
import com.fynnian.application.config.Profile
import com.fynnian.application.room.roomApi
import com.fynnian.application.room.roomManagementApi
import com.fynnian.application.user.userApi
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import java.io.File

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {

  val config = AppConfig.initFrom(environment.config)
  FlywayConfig(config.dataSource)
  val dependencies = DI(config)

  installExceptionHandling()
  install(ContentNegotiation) {
    json(Json {
      ignoreUnknownKeys = true
    })
  }
  install(CORS) {
    allowMethod(HttpMethod.Get)
    allowMethod(HttpMethod.Post)
    allowMethod(HttpMethod.Put)
    allowMethod(HttpMethod.Delete)

    allowHeader(HttpHeaders.ContentType)
    allowHeader(HttpHeaders.Authorization)

    anyHost()
  }
  install(Compression) {
    gzip()
  }
  routing {
    // for all web pages serve the index html, react browser router handles the reset in the client
    get(URLS.HOME) { call.serveIndexHTML() }
    get("/room/*") { call.serveIndexHTML() }
    get(URLS.MANAGEMENT) { call.serveIndexHTML() }
    get(URLS.MANAGEMENT + "/*") { call.serveIndexHTML() }

    // setup proxy for delivering the js file in dev mode from the webpack dev server
    if (config.profile == Profile.DEV) {
      get("/static/{file...}") {
        val file = call.parameters["file"]
        val client = HttpClient(CIO)
        val proxyCall = client.get("http://localhost:9090/$file")
        val contentType = proxyCall.headers["Content-Type"]?.let(ContentType::parse)
          ?: ContentType.Application.OctetStream
        call.respondBytes(proxyCall.readBytes(), contentType)
      }
    } else {
      static(URLS.STATIC_ROOT) {
        resources()
      }
    }
    static(URLS.STATIC_IMAGES_ROOT) {
      staticRootFolder = File(config.content.uploadDir)
      files(".")
    }
    userApi(dependencies)
    roomApi(dependencies)
    roomManagementApi(dependencies)
  }
}

suspend fun ApplicationCall.serveIndexHTML() {
  respondText(
    this::class.java.classLoader.getResource("index.html")!!.readText(),
    ContentType.Text.Html
  )
}