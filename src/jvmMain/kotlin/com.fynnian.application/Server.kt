package com.fynnian.application

import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.cors.routing.*
import kotlinx.html.*

fun HTML.index() {
  head {
    title("Room of Horrors")
  }
  body {
    script(src = "/static/room-of-horrors.js") {}
  }
}

fun main() {
  embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
    install(ContentNegotiation) {
      json()
    }
    install(CORS) {
      allowMethod(HttpMethod.Get)
      allowMethod(HttpMethod.Post)
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
      static("/static") {
        resources()
      }
      route(AppPaths.API_ROOT.path) {
        route(AppPaths.API_USERS.path) {
            get {
                call.respond(listOf(User("1", "")))
            }
        }
      }
    }
  }.start(wait = true)
}