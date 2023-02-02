package com.fynnian.application.common

typealias Localization = Map<I18n.TranslationKey, String>

typealias Localizations = Map<Language, Localization>

object I18n {

  private val localization: Localizations = mapOf(
    Language.DE to mapOf(
      TranslationKey.ROOM_NAVIGATOR_TITLE_WELCOME to "Wilkommen im Room of Horrors",
      TranslationKey.ROOM_NAVIGATOR_TITLE_INVALID_CODE to "Es gibt keinen Raum mit Code {roomCode}",
      TranslationKey.ROOM_NAVIGATOR_INPUT_LABEL to "Bitte gib ein 8 stelligen Raum code ein",
      TranslationKey.ROOM_NAVIGATOR_INPUT_PLACEHOLDER to "abcd1234",
      TranslationKey.ROOM_NAVIGATOR_BUTTON to "Zum Raum",
    ),
    Language.EN to mapOf(
      TranslationKey.ROOM_NAVIGATOR_TITLE_WELCOME to "welcome to the room of horrors",
      TranslationKey.ROOM_NAVIGATOR_TITLE_INVALID_CODE to "There is no room with code {roomCode}",
      TranslationKey.ROOM_NAVIGATOR_INPUT_LABEL to "please enter an 8 char room code",
      TranslationKey.ROOM_NAVIGATOR_INPUT_PLACEHOLDER to "abcd1234",
      TranslationKey.ROOM_NAVIGATOR_BUTTON to "Go to room",
    ),
    Language.FR to mapOf(

    ),
    Language.IT to mapOf(

    )
  )

  val defaultLanguage = Language.DE
  val availableLanguages = listOf(Language.DE, Language.EN)

  enum class TranslationKey {
    ROOM_NAVIGATOR_TITLE_INVALID_CODE,
    ROOM_NAVIGATOR_TITLE_WELCOME,
    ROOM_NAVIGATOR_INPUT_LABEL,
    ROOM_NAVIGATOR_INPUT_PLACEHOLDER,
    ROOM_NAVIGATOR_BUTTON,
  }

  data class TemplateProperty(private val key: String, val value: String) {
    val propertyKey = "{$key}"
  }

  fun get(language: Language, key: TranslationKey) = localization[language]!![key] ?: ""
  fun get(language: Language, key: TranslationKey, vararg values: TemplateProperty): String {
    return get(language, key).let { text ->
        values.fold(text) { acc: String, value: TemplateProperty -> acc.replace(value.propertyKey, value.value) }
      }
  }
}

enum class Language {
  DE,
  FR,
  IT,
  EN
}

