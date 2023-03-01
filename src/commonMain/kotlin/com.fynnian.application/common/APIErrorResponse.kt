package com.fynnian.application.common

import kotlinx.serialization.Serializable

interface ErrorResponse {
  val message: String
  val status: String
  val statusCode: Int
}

@Serializable
data class APIErrorResponse(
  override val message: String,
  override val status: String,
  override val statusCode: Int
) : ErrorResponse

@Serializable
data class APIValidationErrorResponse(
  override val message: String,
  override val status: String,
  override val statusCode: Int,
  val errors: Map<String, I18n.TranslationKey>
) : ErrorResponse