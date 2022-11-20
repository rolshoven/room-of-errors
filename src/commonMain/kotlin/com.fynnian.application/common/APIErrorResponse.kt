package com.fynnian.application.common

import kotlinx.serialization.Serializable

@Serializable
data class APIErrorResponse(
  val message: String,
  val status: String,
  val statusCode: Int
)
