package com.fynnian.application.auth

import com.fynnian.application.common.URLS
import com.fynnian.application.config.DI
import com.fynnian.application.config.Profile
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserSession
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.authApi(dependencies: DI) {
    route(URLS.API_AUTH_CHECK) {
        get {
            // Always authenticated in dev profile
            if (dependencies.profile == Profile.DEV) {
                call.respond(HttpStatusCode.OK, "Authenticated (Dev Mode)")
            }

            val token = call.request.cookies["userAuthenticated"]
            if (token.isNullOrBlank()) {
                call.respond(HttpStatusCode.Unauthorized, "Not authenticated")
                return@get
            }

            val cachedSession = call.sessions.get<UserSession>()
            if (cachedSession != null) {
                call.respond(HttpStatusCode.OK, "Authenticated")
                return@get
            }

            try {
                val session = dependencies.supabaseClient.auth.retrieveUser(token)

                call.sessions.set(session)

                call.respond(HttpStatusCode.OK, "Authenticated")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid token")
            }

        }
    }
}
