package com.fynnian.application.auth

import io.github.jan.supabase.auth.user.UserSession
import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

/**
 * Middleware that enforces authentication before executing protected routes.
 */
fun Route.authenticatedRoute(routeBuilder: Route.() -> Unit) {
    val authPlugin = createRouteScopedPlugin("AuthPlugin") {
        onCall { call ->
            val session = call.sessions.get<UserSession>()

            if (session == null) {
                call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
                return@onCall
            }
        }
    }

    route("", routeBuilder).install(authPlugin)
}
