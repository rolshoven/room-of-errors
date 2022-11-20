package domain

data class Room(
  val id: String,
  val title: String,
  val image: RoomImage
)

data class RoomImage(
  val url: String,
  val title: String,
)