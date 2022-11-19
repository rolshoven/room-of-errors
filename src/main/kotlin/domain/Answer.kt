package domain

data class Answer(
  val no: Int,
  val roomId: String,
  val coordinates: Coordinates,
  val answer: String
)

data class Coordinates(
  val horizontal: Double,
  val vertical: Double
)