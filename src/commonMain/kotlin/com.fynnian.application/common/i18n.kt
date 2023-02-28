package com.fynnian.application.common

typealias Localization = Map<I18n.TranslationKey, String>

typealias Localizations = Map<Language, Localization>

object I18n {

  private val localization: Localizations = mapOf(
    Language.DE to mapOf(
      TranslationKey.LOADING_SPINNER to "Lade Daten",

      TranslationKey.ROOM_NAVIGATOR_TITLE_WELCOME to "Willkommen im Room of Horrors",
      TranslationKey.ROOM_NAVIGATOR_TITLE_INVALID_CODE to "Es gibt keinen Raum mit Code {roomCode}",
      TranslationKey.ROOM_NAVIGATOR_INPUT_LABEL to "Bitte gib ein 8 stelligen Raum code ein",
      TranslationKey.ROOM_NAVIGATOR_INPUT_PLACEHOLDER to "abcd1234",
      TranslationKey.ROOM_NAVIGATOR_BUTTON to "Zum Raum",

      TranslationKey.ROOM_INFO_LABEL_NAME to "Raum Name: {roomTitle}",
      TranslationKey.ROOM_INFO_LABEL_ROOM_CODE to "Raum Code: {roomCode}",

      TranslationKey.ROOM_INTRO_TEXT to "Wenn du bereit bist um mit dem Raum zu starten klicke auf 'beginnen'.",
      TranslationKey.ROOM_INTRO_START_BUTTON to "Beginnen",

      TranslationKey.ROOM_OUTRO_TEXT to "Danke für das Mitmachen, die Lösung für den Raum wird zu einem späteren Zeitpunkt veröffentlicht.",

      TranslationKey.ROOM_IMAGE_HELP_TEXT to "Markiere die Fehler mit einem Klick auf das Bild",
      TranslationKey.ROOM_ANSWER_INPUT_PLACEHOLDER to "Deine Antwort",
      TranslationKey.ROOM_ANSWER_INPUT_PLACEHOLDER_DISABLED to "Bitte markiere einen Fehler",

      TranslationKey.ROOM_QRCODE_IMAGE_LABEL to "ROOM CODE",
      TranslationKey.ROOM_QRCODE_BUTTON_DOWNLOAD to "Bild Speichern",
      TranslationKey.ROOM_QRCODE_ALERT_DOWNLOAD_SUCCESS to "Bild erfolgreich generiert und gespeichert.",
      TranslationKey.ROOM_QRCODE_ALERT_DOWNLOAD_ERROR to "Konnte das Bild nicht generieren und speichern.",

      TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_LABEL_PARTICIPANTS to "Teilnehmer: {participants}",
      TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_LABEL_TOTAL_ANSWERS to "Total Antworten: {answers}",
      TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_BUTTON_EXCEL_EXPORT to "Excel Export",
      TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_EXCEL_EXPORT_FILE_NAME to "room-export-{roomCode}.xlsx",

      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_DIALOG_BUTTON to "Neuer Raum",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_DIALOG_TITLE to "Neuer Raum erstellen",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_CODE_LABEL to "Raum CODE",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_TITLE_LABEL to "Raum Titel" ,
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_DESCRIPTION_LABEL to "Raum Beschreibung",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_QUESTION_LABEL to "Raum Frage",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_TITLE_LABEL to "Bild Titel",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_UPLOAD_LABEL to "Lade ein PNG Bild des Raumes hoch",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_UPLOAD_MISSING_FILE to "Keine Datei - Benötigt ein PNG Bild",
    ),
    Language.EN to mapOf(
      TranslationKey.LOADING_SPINNER to "Loading Data",

      TranslationKey.ROOM_NAVIGATOR_TITLE_WELCOME to "welcome to the room of horrors",
      TranslationKey.ROOM_NAVIGATOR_TITLE_INVALID_CODE to "There is no room with code {roomCode}",
      TranslationKey.ROOM_NAVIGATOR_INPUT_LABEL to "please enter an 8 char room code",
      TranslationKey.ROOM_NAVIGATOR_INPUT_PLACEHOLDER to "abcd1234",
      TranslationKey.ROOM_NAVIGATOR_BUTTON to "Go to room",

      TranslationKey.ROOM_INFO_LABEL_NAME to "Room name: {roomTitle}",
      TranslationKey.ROOM_INFO_LABEL_ROOM_CODE to "Room Code: {roomCode}",

      TranslationKey.ROOM_INTRO_TEXT to "When you are ready to begin with the room click on the start button.",
      TranslationKey.ROOM_INTRO_START_BUTTON to "Start",

      TranslationKey.ROOM_OUTRO_TEXT to "Thanks for participating, the solution to the room will be available at a later date.",

      TranslationKey.ROOM_IMAGE_HELP_TEXT to "mark a spot with an error with a click on the image",
      TranslationKey.ROOM_ANSWER_INPUT_PLACEHOLDER to "your answer",
      TranslationKey.ROOM_ANSWER_INPUT_PLACEHOLDER_DISABLED to "mark a spot with an error",

      TranslationKey.ROOM_QRCODE_IMAGE_LABEL to "ROOM CODE",
      TranslationKey.ROOM_QRCODE_BUTTON_DOWNLOAD to "Save Code",
      TranslationKey.ROOM_QRCODE_ALERT_DOWNLOAD_SUCCESS to "Successfully generate and downloaded image",
      TranslationKey.ROOM_QRCODE_ALERT_DOWNLOAD_ERROR to "Could not generate and download image",

      TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_LABEL_PARTICIPANTS to "participants: {participants}",
      TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_LABEL_TOTAL_ANSWERS to "total answers: {answers}",
      TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_BUTTON_EXCEL_EXPORT to "Excel Export",
      TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_EXCEL_EXPORT_FILE_NAME to "room-export-{roomCode}.xlsx",

      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_DIALOG_BUTTON to "Create Room",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_DIALOG_TITLE to "Create a new Room",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_CODE_LABEL to "ROOM CODE",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_TITLE_LABEL to "Room Title" ,
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_DESCRIPTION_LABEL to "Room Description",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_QUESTION_LABEL to "Room Question",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_TITLE_LABEL to "Image Title",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_UPLOAD_LABEL to "upload room png image",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_UPLOAD_MISSING_FILE to "No file - require png image",
    ),
    Language.FR to mapOf(

    ),
    Language.IT to mapOf(

    )
  )

  val defaultLanguage = Language.DE
  val availableLanguages = listOf(Language.DE, Language.EN)

  enum class TranslationKey {
    LOADING_SPINNER,

    ROOM_NAVIGATOR_TITLE_INVALID_CODE,
    ROOM_NAVIGATOR_TITLE_WELCOME,
    ROOM_NAVIGATOR_INPUT_LABEL,
    ROOM_NAVIGATOR_INPUT_PLACEHOLDER,
    ROOM_NAVIGATOR_BUTTON,

    ROOM_INFO_LABEL_NAME,
    ROOM_INFO_LABEL_ROOM_CODE,

    ROOM_INTRO_TEXT,
    ROOM_INTRO_START_BUTTON,

    ROOM_OUTRO_TEXT,

    ROOM_IMAGE_HELP_TEXT,
    ROOM_ANSWER_INPUT_PLACEHOLDER,
    ROOM_ANSWER_INPUT_PLACEHOLDER_DISABLED,

    ROOM_QRCODE_IMAGE_LABEL,
    ROOM_QRCODE_BUTTON_DOWNLOAD,
    ROOM_QRCODE_ALERT_DOWNLOAD_SUCCESS,
    ROOM_QRCODE_ALERT_DOWNLOAD_ERROR,

    ROOM_MANAGEMENT_ROOM_LIST_LABEL_PARTICIPANTS,
    ROOM_MANAGEMENT_ROOM_LIST_LABEL_TOTAL_ANSWERS,
    ROOM_MANAGEMENT_ROOM_LIST_BUTTON_EXCEL_EXPORT,
    ROOM_MANAGEMENT_ROOM_LIST_EXCEL_EXPORT_FILE_NAME,

    ROOM_MANAGEMENT_CREATE_ROOM_DIALOG_BUTTON,
    ROOM_MANAGEMENT_CREATE_ROOM_DIALOG_TITLE,
    ROOM_MANAGEMENT_CREATE_ROOM_ROOM_CODE_LABEL,
    ROOM_MANAGEMENT_CREATE_ROOM_ROOM_TITLE_LABEL,
    ROOM_MANAGEMENT_CREATE_ROOM_ROOM_DESCRIPTION_LABEL,
    ROOM_MANAGEMENT_CREATE_ROOM_ROOM_QUESTION_LABEL,
    ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_TITLE_LABEL,
    ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_UPLOAD_LABEL,
    ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_UPLOAD_MISSING_FILE,
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

