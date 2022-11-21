package com.fynnian.application.common

import com.fynnian.application.config.DataSource
import org.jooq.DSLContext
import org.jooq.impl.DSL
import java.sql.DriverManager

class Repository(private val dataSource: DataSource) {

  fun createContext(): DSLContext {
    with(DriverManager.getConnection(dataSource.url, dataSource.user, dataSource.password)) {
      return DSL.using(this, dataSource.dialect)
    }
  }

}