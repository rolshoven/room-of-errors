package com.fynnian.application.common

import com.benasher44.uuid.Uuid
import kotlinx.serialization.Serializable

@Serializable
data class User(
  @Serializable(with = UuidSerializer::class)
  val id: Uuid,
  val profession: String
)