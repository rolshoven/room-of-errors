package com.fynnian.application.room

import com.fynnian.application.BaseTestSetup
import org.assertj.core.api.Assertions.assertThat
import com.fynnian.application.common.room.RoomStatus as RoomStatusDomain
import com.fynnian.application.jooq.enums.RoomStatus as RoomStatusJooq
import kotlin.test.Test

class RoomRepositoryTest : BaseTestSetup() {

  @Test
  fun `mapping room enums`() {

    val jooqStatus = RoomStatusJooq.values().apply { sortBy { it.literal } }
    val domainStatus = RoomStatusDomain.values().apply { sortBy { it.toString() } }

    jooqStatus.forEachIndexed { i, status -> assertThat(status.toDomain()).isEqualTo(domainStatus[i]) }
    domainStatus.forEachIndexed { i, status -> assertThat(status.toRecord()).isEqualTo(jooqStatus[i]) }
  }
}