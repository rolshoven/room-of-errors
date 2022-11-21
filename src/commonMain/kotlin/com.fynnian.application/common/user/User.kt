package com.fynnian.application.common.user

import com.benasher44.uuid.Uuid
import com.fynnian.application.common.UuidSerializer
import kotlinx.serialization.Serializable

@Serializable
data class User(
  @Serializable(with = UuidSerializer::class)
  val id: Uuid,
  val profession: String?
)