package com.fynnian.application.common

import com.fynnian.application.config.DataSource
import org.jooq.DSLContext
import org.jooq.impl.DSL
import java.sql.DriverManager
import java.time.OffsetDateTime
import java.time.ZoneId

open class Repository(private val dataSource: DataSource) {

  fun <T> jooq(statement: DSLContext.() -> T): T {
    DriverManager.getConnection(dataSource.url, dataSource.user, dataSource.password).use {
      return DSL.using(it, dataSource.dialect).statement()
    }
  }

  fun nowAtCHOffsetDateTime(): OffsetDateTime = OffsetDateTime.now(ZoneId.of("Europe/Zurich"))

}