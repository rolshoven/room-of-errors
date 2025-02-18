package com.fynnian.application.config

import io.ktor.server.config.*
import org.jooq.SQLDialect
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Paths

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
  val dataSource: DataSource,
  val content: Content,
  val supabaseUrl: String,
  val supabaseKey: String
) {
  companion object {
    private const val root = "ktor.environment"
    fun initFrom(config: ApplicationConfig): AppConfig {
      return AppConfig(
        config.propertyOrNull("$root.profile")
          ?.let { Profile.fromString(it.getString()) } ?: Profile.DEV,
        DataSource.initFrom(config),
        Content.initFrom(config),
        config.propertyOrNull("$root.supabaseUrl").toString(),
        config.propertyOrNull("$root.supabaseKey").toString()
        )
    }
  }
}

data class DataSource(val map: Map<String, String?>) {
  val driver: String by map
  val schema: String by map
  val url: String by map
  val user: String by map
  val password: String by map
  val dialect = when {
    driver.contains("H2", ignoreCase = true) -> SQLDialect.H2
    driver.contains("POSTGRES", ignoreCase = true) -> SQLDialect.POSTGRES
    else -> throw Exception("the selected driver: $driver is not configured for jooq")
  }


  companion object {
    private const val root = "ktor.datasource"
    fun initFrom(config: ApplicationConfig): DataSource {
      return DataSource(
        mapOf(
          "driver" to config.getPropertyByKey("$root.driver"),
          "schema" to config.getPropertyByKey("$root.schema"),
          "url" to config.getPropertyByKey("$root.url"),
          "user" to config.getPropertyByKey("$root.user"),
          "password" to config.getPropertyByKey("$root.password"),
        )
      )
    }
  }
}

data class Content(val map: Map<String, String?>) {
  val uploadDir: String by map
  val imageUploadDir: String by map
  val videoUploadDir: String by map

  init {
    val baseUploadDir = Files.createDirectories(Paths.get(uploadDir))
    val imageUploadDir = Files.createDirectories(Paths.get(imageUploadDir))
    val videoUploadDir = Files.createDirectories(Paths.get(videoUploadDir))
    log.info("Create base directory for uploads $baseUploadDir")
    log.info("Create directory for image uploads $imageUploadDir")
    log.info("Create directory for videos uploads $videoUploadDir")
  }

  companion object {
    private const val root = "ktor.content"
    fun initFrom(config: ApplicationConfig): Content {
      return Content(
        mapOf(
          "uploadDir" to config.getPropertyByKey("$root.uploadDir"),
          "imageUploadDir" to config.getPropertyByKey("$root.uploadDir") + "/images",
          "videoUploadDir" to config.getPropertyByKey("$root.uploadDir") + "/videos"
        )
      )
    }

    @JvmStatic
    private val log = LoggerFactory.getLogger(AppConfig::class.java)
  }
}

private fun ApplicationConfig.getPropertyByKey(key: String): String? = propertyOrNull(key)?.getString()