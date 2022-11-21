package com.fynnian.application

import com.fynnian.application.common.AppPaths
import com.fynnian.application.config.AppConfig
import com.fynnian.application.config.DI
import com.fynnian.application.room.roomApi
import com.fynnian.application.room.roomManagementApi
import com.fynnian.application.user.userApi
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

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {

  val config = AppConfig.initFrom(environment.config)
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
    anyHost()
  }
  install(Compression) {
    gzip()
  }
  routing {
    get(AppPaths.HOME.path) {
      call.respondText(
        this::class.java.classLoader.getResource("index.html")!!.readText(),
        ContentType.Text.Html
      )
    }
    static(AppPaths.STATIC_ROOT.path) {
      resources()
    }
    route(AppPaths.API_ROOT.path) {
      userApi(dependencies)
      roomApi(dependencies)
      roomManagementApi(dependencies)
    }
  }
}