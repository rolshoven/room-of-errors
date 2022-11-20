package com.fynnian.application.config

import io.ktor.server.config.*

enum class Profile {
  DEV,
  PROD;

  companion object {
    fun fromString(value: String): Profile {
      try {
        return valueOf(value.uppercase())
      } catch (e: Exception) {
        throw Exception("invalid profile value `$value`, possible profiles ${values().toList()}")
      }
    }
  }

}

data class AppConfig(
  val profile: Profile,
) {
  companion object {
    private const val root = "ktor.environment"

    fun initFrom(config: ApplicationConfig): AppConfig {
      return AppConfig(
        config.propertyOrNull("$root.profile")
          ?.let { Profile.fromString(it.getString()) } ?: Profile.DEV,
      )
    }
  }
}