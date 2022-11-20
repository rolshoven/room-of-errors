package com.fynnian.application

import kotlinx.serialization.Serializable

@Serializable
data class User(
  val id: String,
  val email: String
)