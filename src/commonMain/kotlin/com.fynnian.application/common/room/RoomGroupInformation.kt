package com.fynnian.application.common.room

import com.benasher44.uuid.Uuid
import com.fynnian.application.common.UuidSerializer
import kotlinx.serialization.Serializable

@Serializable
data class RoomGroupInformation(
  @Serializable(with = UuidSerializer::class)
  val userId: Uuid,
  val roomCode: String,
  val groupSize: Int,
  val groupName: String
)

object GroupNameGenerator {

  fun getName(): String = adjectives.random() + " " + nouns.random()

  val adjectives = setOf(
    "tropischer",
    "neugierige",
    "achtsame",
    "arkane",
    "attraktive",
    "lodernde",
    "geladene",
    "autonome",
    "bärenstarke",
    "wilde",
    "bewölkte",
    "blonde",
    "brisante",
    "brilliante",
    "buschige",
    "bunte",
    "schnelle",
    "charmante",
    "dreibeinige",
    "durchsichtige",
    "ehrwürdige",
    "entzückte",
    "freche",
    "fröhliche",
    "furchtlose",
    "giftgrüne",
    "karierte",
    "zahme",
    "zeitlose",
    "zauberhafte",
    "heisse",
    "umsichtige",
    "tapfere",
    "kleine",
    "gestreifte",
    "synchrone",
    "stolze",
    "stilsichere",
    "steuerfreie",
    "sonnige",
    "smaragdgrüne",
    "rubinrote",
    "tanzende",
    "strahlende",
    "selbstlose",
    "salzige",
    "süsse",
    "purpurne",
    "regnerische"
  )
  val nouns = setOf(
    "Giraffen",
    "Autos",
    "Katzen",
    "Ärtze",
    "Menschen",
    "Bären",
    "Tassen",
    "Türme",
    "Tasten",
    "Aquarien",
    "Hunde",
    "Kekse",
    "Spritzen",
    "Stifte",
    "Lemuren",
    "Einhörner",
    "Kobolde",
    "Zwerge",
    "Elfen",
    "Berge",
    "Hügel",
    "Tannen",
    "Orangen",
    "Nachtigall",
    "Spitäler",
    "Mäuse",
    "Kittel",
    "Gräser",
    "Schildkröten",
    "Mofas",
    "Räume",
    "Pinguine",
    "Zauberer",
    "Nudeln",
    "Schokolade",
    "Normannen",
    "Tauben",
    "Karotten",
    "Kaffee",
    "Pflegende",
    "Birken",
    "Berge",
    "Trauben",
    "Seelöwen",
    "Otter",
    "Blumen",
    "Velos",
    "Uhus",
    "Rosen",
    "Roboter",
    "Inseln"
  )
}