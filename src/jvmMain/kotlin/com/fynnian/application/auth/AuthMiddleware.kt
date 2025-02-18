package com.fynnian.application.auth

import com.fynnian.application.common.URLS
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

/**
 * Middleware that enforces authentication before executing protected routes.
 */
val httpClient = HttpClient {
    followRedirects = true
}

fun Route.authenticatedRoute(build: Route.() -> Unit): Route {
    val authPlugin = createRouteScopedPlugin("AuthPlugin") {
        onCall { call ->
            try {
                val cookies = call.request.headers[HttpHeaders.Cookie]
                println("Forwarding cookies: $cookies")  // Debugging

                val response: HttpResponse = httpClient.get(URLS.API_AUTH_CHECK) {
                    header(HttpHeaders.Cookie, cookies ?: "")
                }

                if (response.status != HttpStatusCode.OK) {
                    call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
                    return@onCall
                }
            } catch (e: Exception) {
                println("Auth request failed: ${e.message}")  // Debugging
                call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
                return@onCall
            }
        }
    }

    val route = createChild(CustomSelector())
    route.install(authPlugin)
    route.build()
    return route
}

private class CustomSelector : RouteSelector() {
    override suspend fun evaluate(context: RoutingResolveContext, segmentIndex: Int) = RouteSelectorEvaluation.Transparent
}