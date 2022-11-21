package com.fynnian.application.common.room

import com.benasher44.uuid.Uuid
import com.fynnian.application.common.UuidSerializer
import kotlinx.serialization.Serializable

@Serializable
data class Answer(
  @Serializable(with = UuidSerializer::class)
  val id: Uuid,
  val roomCode: String,
  @Serializable(with = UuidSerializer::class)
  val imageId: Uuid,
  @Serializable(with = UuidSerializer::class)
  val userId: Uuid,
  val no: Int,
  val coordinates: Coordinates,
  val answer: String
)

@Serializable
data class Coordinates(
  val horizontal: Double,
  val vertical: Double
)