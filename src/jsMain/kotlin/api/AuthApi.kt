package api

import com.fynnian.application.common.URLS
import kotlinx.coroutines.await
import org.w3c.fetch.Response
import kotlinx.browser.window

class AuthApi {
    suspend fun isAuthenticated(): Boolean {
        return try {
            val response: Response = window.fetch(URLS.API_AUTH_CHECK).await()
            response.ok // true if status is 200
        } catch (e: Throwable) {
            console.log("Auth check failed:", e)
            false
        }
    }
}
