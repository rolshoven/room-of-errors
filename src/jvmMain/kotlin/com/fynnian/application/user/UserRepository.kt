package com.fynnian.application.user

import com.fynnian.application.common.User
import com.fynnian.application.jooq.tables.records.UsersRecord


fun User.toRecord() = UsersRecord().also {
  it.id = id
  // it.profession = profession
}

fun UsersRecord.toDomain() = User(id, "ToDo")