package com.fynnian.application.user

import com.benasher44.uuid.Uuid
import com.fynnian.application.APIException
import com.fynnian.application.common.Repository
import com.fynnian.application.common.user.User
import com.fynnian.application.config.DataSource
import com.fynnian.application.jooq.tables.records.UsersRecord
import com.fynnian.application.jooq.tables.references.USERS
import java.time.OffsetDateTime


class UserRepository(dataSource: DataSource) : Repository(dataSource) {
  fun getUserById(id: Uuid): User {
    return jooq {
      select(USERS.asterisk())
        .from(USERS)
        .where(USERS.ID.eq(id))
        .map { it.into(USERS).toDomain() }
        .firstOrNull()
        ?: throw APIException.NotFound("User with id: $id not found")
    }
  }

  fun upsertUser(user: User): User {
    return jooq {
      insertInto(USERS)
        .set(user.toRecord())
        .onConflict(USERS.ID)
        .doUpdate()
        .set(
          user
            .toRecord()
            .also { it.updatedAt = OffsetDateTime.now() } // simply way of setting the updatedAt without trigger
        )
        .returning()
        .map { it.into(USERS).toDomain() }
        .first()
    }
  }

  fun deleteUser(id: Uuid) {
    jooq {
      deleteFrom(USERS)
        .where(USERS.ID.eq(id))
        .returning()
        .firstOrNull()
        ?: throw APIException.NotFound("User with id: $id not found")
    }
  }
}

fun User.toRecord() = UsersRecord().also {
  it.id = id
  it.profession = profession
}

fun UsersRecord.toDomain() = User(id!!, profession)