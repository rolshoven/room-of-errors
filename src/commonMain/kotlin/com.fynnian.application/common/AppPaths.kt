package com.fynnian.application.common

enum class AppPaths(val path: String) {
  API_ROOT("/api"),
  API_USERS("/users"),
  API_ROOMS("/rooms"),
  API_ROOMS_MANAGEMENT("/management"),
  HOME("/"),
  STATIC_ROOT("/static")

}

enum class AppPathParams(val param: String) {
  ID("id")
}