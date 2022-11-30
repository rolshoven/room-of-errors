package com.fynnian.application.config

import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory

class FlywayConfig(private val dataSource: DataSource) {

  init {
    log.info("Load Flyway config and apply flyway migrations")
    flyway().migrate()
  }

  private fun flyway(): Flyway {
    return Flyway.configure()
      .dataSource(dataSource.url, dataSource.user, dataSource.password)
      .schemas(dataSource.schema)
      .createSchemas(true)
      .load()
  }

  companion object {
    @JvmStatic
    @Suppress("JAVA_CLASS_ON_COMPANION")
    private val log = LoggerFactory.getLogger(javaClass.enclosingClass)
  }
}