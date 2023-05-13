package com.fynnian.application.common

typealias Localization = Map<I18n.TranslationKey, String>

typealias Localizations = Map<Language, Localization>

object I18n {

  private val localization: Localizations = mapOf(
    Language.DE to mapOf(
      TranslationKey.LOADING_SPINNER to "Lade Daten",

      TranslationKey.YES to "Ja",
      TranslationKey.NO to "Nein",

      TranslationKey.CONFIRMATION_BUTTON_YES to "Ja",
      TranslationKey.CONFIRMATION_BUTTON_NO to "Nein",

      TranslationKey.STAR_NEW_USER_SESSION_BUTTON to "Starte eine neue Session",

      TranslationKey.ROOM_NAVIGATOR_TITLE_WELCOME to "Willkommen im Room of Horrors",
      TranslationKey.ROOM_NAVIGATOR_TITLE_INVALID_CODE to "Es gibt keinen Raum mit Code {roomCode}",
      TranslationKey.ROOM_NAVIGATOR_INPUT_LABEL to "Bitte gib ein 8 stelligen Raum code ein",
      TranslationKey.ROOM_NAVIGATOR_INPUT_PLACEHOLDER to "abcd1234",
      TranslationKey.ROOM_NAVIGATOR_BUTTON to "Zum Raum",

      TranslationKey.ROOM_INFO_LABEL_NAME to "Raum: {roomCode} - {roomTitle}",

      TranslationKey.ROOM_STATUS_NOT_READY to "Unvollständig",
      TranslationKey.ROOM_STATUS_OPEN to "Offen",
      TranslationKey.ROOM_STATUS_CLOSED to "Abgeschlossen",
      TranslationKey.ROOM_STATUS_ALL to "Alle",

      TranslationKey.ROOM_GROUP_INFO_TITLE to "Gruppen Information",
      TranslationKey.ROOM_GROUP_INFO_GROUP_NAME_LABEL to "Gruppenname",
      TranslationKey.ROOM_GROUP_INFO_GROUP_SIZE_LABEL to "Gruppengrösse",
      TranslationKey.ROOM_GROUP_INFO_GROUP_BUTTON to "Weiter",

      TranslationKey.ROOM_INTRO_TEXT to "Wenn du bereit bist um mit dem Raum zu starten klicke auf 'beginnen'.",
      TranslationKey.ROOM_INTRO_START_BUTTON to "Mit dem Raum starten",

      TranslationKey.ROOM_OUTRO_TITLE to "Raum Abgeschlossen",
      TranslationKey.ROOM_OUTRO_TEXT to "",

      TranslationKey.ROOM_UNAVAILABLE_TITLE to "Der Raum ist nicht verfügbar.",
      TranslationKey.ROOM_UNAVAILABLE_ROOM_NOT_READY to "Der Raum is noch nicht beret, er wird gerade eingerichtet.",
      TranslationKey.ROOM_UNAVAILABLE_ROOM_CLOSED to "Der Raum is geschlossen - es kann nicht mehr benutzt werden.",

      TranslationKey.ROOM_FINISH_DIALOG_BUTTON to "Raum Abschliessen",
      TranslationKey.ROOM_FINISH_DIALOG_CONFIRMATION_TEXT to "Bist du bereit den Raum zu beenden?",

      TranslationKey.ROOM_IMAGE_HELP_TEXT to "Markiere die Fehler mit einem Klick auf das Bild",

      TranslationKey.ROOM_IMAGE_SLIDER_NEXT to "Nächstes Bild",
      TranslationKey.ROOM_IMAGE_SLIDER_BACK to "Vorheriges Bild",

      TranslationKey.ROOM_ANSWER_ANSWERS_TOTAL to "Total {answers} Antworten",
      TranslationKey.ROOM_ANSWER_ANSWERS_COUNT_PER_IMAGE to "Bild {number}: {answers} Antworten",

      TranslationKey.ROOM_ANSWER_INPUT_PLACEHOLDER to "Deine Antwort",
      TranslationKey.ROOM_ANSWER_INPUT_PLACEHOLDER_DISABLED to "Bitte markiere einen Fehler",

      TranslationKey.ROOM_TIME_LIMIT_REACHED_DIALOG_TITLE to "Die Zeit is abgelaufen!",
      TranslationKey.ROOM_TIME_LIMIT_REACHED_DIALOG_TEXT to "Deine verfügbare Zeit für den Raum ist aufgebraucht.",

      TranslationKey.ROOM_RESTART_TIMER_COUNTING to "Raum wird in {time} zurückgesetzt.",
      TranslationKey.ROOM_RESTART_TIMER_ROOM_RESETTING to "Raum wird zurückgeesetzt.",

      TranslationKey.ROOM_QRCODE_IMAGE_LABEL to "ROOM CODE",
      TranslationKey.ROOM_QRCODE_BUTTON_DOWNLOAD to "Bild Speichern",
      TranslationKey.ROOM_QRCODE_ALERT_DOWNLOAD_SUCCESS to "Bild erfolgreich generiert und gespeichert.",
      TranslationKey.ROOM_QRCODE_ALERT_DOWNLOAD_ERROR to "Konnte das Bild nicht generieren und speichern.",

      TranslationKey.ROOM_MANAGEMENT_INCOMPLETE_ROOM_ALERT to "Der Raum is unvollständig, er kann nicht geöffnet werden",

      TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_LABEL_PARTICIPANTS to "Teilnehmer: {participants}",
      TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_LABEL_TOTAL_ANSWERS to "Total Antworten: {answers}",
      TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_LABEL_WITH_GROUP_INFORMATION to "Gruppen Information wird gesammelt: {boolean}",
      TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_LABEL_GROUPS to "Gruppen: {number}",
      TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_LABEL_PARTICIPANTS_WITHOUT_ANSWERS to "Teilnehmer / Gruppen die keine Antworten angegeben haben: {participants}",
      TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_BUTTON_EXCEL_EXPORT to "Excel Export",
      TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_EXCEL_EXPORT_FILE_NAME to "room-export-{roomCode}.xlsx",

      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_DIALOG_BUTTON to "Neuer Raum",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_DIALOG_TITLE to "Neuer Raum erstellen",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_CODE_LABEL to "Raum CODE",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_TITLE_LABEL to "Raum Titel",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_DESCRIPTION_LABEL to "Raum Beschreibung",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_DESCRIPTION_NOT_DEFINED to "Es gibt keine Raum Beschreibung",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_QUESTION_LABEL to "Einstiegs Frage",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_QUESTION_NOT_DEFINED to "Es gibt keine Einstiegs Frage",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_WITH_TIME_LIMIT_SWITCH_LABLE to "Mit Zeitlimit",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_TIME_LIMIT_LABLE to "Zeitlimit in Minuten",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_TIME_LIMIT_HELP_TEXT to "Optional, Zeitlimit für das abschliessen des Raumes.",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_TIME_LIMIT_NOT_DEFINED to "Es wurde kein Zeitlimit definiert",

      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_SINGLE_DEVICE_ROOM_SWITCH_LABLE to
          "Raum wird auf einem auf einem Gerät verwendet.",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_SINGLE_DEVICE_ROOM_SWITCH_HELP_TEXT to
          """
          Lege fest, ob der Raum auf einem einzigen Gerät angezeigt werden soll, i.e. der Raum hat einen PC oder ein Tablet, auf dem die Benutzer die Antworten eingeben.
          Wenn diese Option nicht aktiviert ist, sollen die Benutzer ihr eigenes Gerät für die Eingabe der Antworten verwenden.
          Mit dieser dieser Option kannst du steuern ob ein Benutzer eine neue Sitzung beginnen kann wenn der Raum beendet ist.
          Also macht den Raum für den nächsten Benutzer bereit.
          """.trimIndent(),

      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_WITH_GROUP_INFO_SWITCH_LABEL to "Gruppen Information Sammeln",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_WITH_GROUP_INFO_SWITCH_HELP_TEXT to
              "Wenn aktiviert, muss der Benutzer einen Gruppennamen und die Gruppengrösse angeben bevor gestartet werden kann.",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_WITH_GROUP_INFO_TEXT_LABEL to "Hilfe Text für die Gruppen Information Card. Was soll der Benutzer machen etc.",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_WITH_GROUP_INFO_TEXT_NOT_DEFINED to "Es ist kein Hilfe Text für die Gruppen Information Card definiert. Es wird nichts angezeigt.",

      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_TITLE_LABEL to "Bild Titel",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_UPLOAD_LABEL to "Lade ein PNG Bild des Raumes hoch",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_UPLOAD_MISSING_FILE to "Keine Datei - Benötigt ein PNG Bild",

      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_INTERACTION_INFO_FORM_SWITCH to "Upload Video",

      TranslationKey.ROOM_MANAGEMENT_OPEN_ROOM_BUTTON to "Der Raum is bereit - Raum Öffnen",
      TranslationKey.ROOM_MANAGEMENT_OPEN_ROOM_BUTTON_DISABLED to "Der Raum is noch unvollständig.",
      TranslationKey.ROOM_MANAGEMENT_OPEN_ROOM_TEXT to "Ist alles bereit, jedes Bild und Text eingepflegt?",
      TranslationKey.ROOM_MANAGEMENT_CLOSE_ROOM_BUTTON to "Raum schliessen - wir sind fertig.",
      TranslationKey.ROOM_MANAGEMENT_CLOSE_ROOM_TEXT to "Bist du sicher das du den Raum schliessen willst?",
      TranslationKey.ROOM_MANAGEMENT_RE_OPEN_ROOM_BUTTON to "Raum wider öffnen",
      TranslationKey.ROOM_MANAGEMENT_RE_OPEN_ROOM_TEXT to "Bist du sicher das du den Raum wider öffnen willst?",

      TranslationKey.ROOM_MANAGEMENT_OPEN_ROOM_IMAGE_VALIDATION_ERROR to "Der Raum hat kein Bild, kann nicht geöffnet werden.",

      TranslationKey.ROOM_MANAGEMENT_DELETE_ROOM_BUTTON to "Raum Löschen",
      TranslationKey.ROOM_MANAGEMENT_DELETE_ROOM_TEXT to
          "Bist du sucher das du den Raum löschen willst, alles was zum Raum gehört, Videos, Bilder und Antworten werde UNWIDERRUFLICH gelöscht!",

      TranslationKey.ROOM_EXCEL_EXPORT_INFO_CARD_ROOM_CODE to "Raum Code",
      TranslationKey.ROOM_EXCEL_EXPORT_INFO_CARD_TITLE to "Titel",
      TranslationKey.ROOM_EXCEL_EXPORT_INFO_CARD_DESCRIPTION to "Raum Beschreibung",
      TranslationKey.ROOM_EXCEL_EXPORT_INFO_CARD_QUESTION to "Display question",
      TranslationKey.ROOM_EXCEL_EXPORT_INFO_CARD_IMAGES to "Anzahl an Bildern",
      TranslationKey.ROOM_EXCEL_EXPORT_INFO_CARD_PARTICIPANTS to "Teilnehmer",
      TranslationKey.ROOM_EXCEL_EXPORT_INFO_CARD_PARTICIPANTS_WITHOUT_ANSWERS to "Teilnehmer / Gruppen die keine Antworten abgegeben haben",
      TranslationKey.ROOM_EXCEL_EXPORT_INFO_CARD_TOTAL_ANSWERS to "Total Aller Antworten",
      TranslationKey.ROOM_EXCEL_EXPORT_INFO_CARD_COLLECT_GROUP_INFO to "Sammle Gruppen Information",
      TranslationKey.ROOM_EXCEL_EXPORT_INFO_CARD_GROUPS to "Anzahl Gruppen",

      TranslationKey.ROOM_EXCEL_EXPORT_HEADER_USER_NO to "userNr",
      TranslationKey.ROOM_EXCEL_EXPORT_HEADER_ANSWER_NO to "antwortNr",
      TranslationKey.ROOM_EXCEL_EXPORT_HEADER_ANSWER to "antwort",
      TranslationKey.ROOM_EXCEL_EXPORT_HEADER_IMAGE_NO to "bildNr",
      TranslationKey.ROOM_EXCEL_EXPORT_HEADER_TOTAL_ANSWERS_PER_USER to "totalAntwortenProUser",
      TranslationKey.ROOM_EXCEL_EXPORT_HEADER_GROUP_NAME to "gruppenName",
      TranslationKey.ROOM_EXCEL_EXPORT_HEADER_GROUP_SIZE to "gruppenGroesse",
      TranslationKey.ROOM_EXCEL_EXPORT_HEADER_STARTED to "gestartet",
      TranslationKey.ROOM_EXCEL_EXPORT_HEADER_FINISHED to "beendet",
      TranslationKey.ROOM_EXCEL_EXPORT_HEADER_TIME to "zeit",

    ),
    Language.EN to mapOf(
      TranslationKey.LOADING_SPINNER to "Loading Data",

      TranslationKey.YES to "Yes",
      TranslationKey.NO to "No",

      TranslationKey.CONFIRMATION_BUTTON_YES to "Yes",
      TranslationKey.CONFIRMATION_BUTTON_NO to "No",

      TranslationKey.STAR_NEW_USER_SESSION_BUTTON to "Start a new session",

      TranslationKey.ROOM_NAVIGATOR_TITLE_WELCOME to "welcome to the room of horrors",
      TranslationKey.ROOM_NAVIGATOR_TITLE_INVALID_CODE to "There is no room with code {roomCode}",
      TranslationKey.ROOM_NAVIGATOR_INPUT_LABEL to "please enter an 8 char room code",
      TranslationKey.ROOM_NAVIGATOR_INPUT_PLACEHOLDER to "abcd1234",
      TranslationKey.ROOM_NAVIGATOR_BUTTON to "Go to room",

      TranslationKey.ROOM_INFO_LABEL_NAME to "Room: {roomCode} - {roomTitle}",

      TranslationKey.ROOM_STATUS_NOT_READY to "Not Ready",
      TranslationKey.ROOM_STATUS_OPEN to "Open",
      TranslationKey.ROOM_STATUS_CLOSED to "Closed",
      TranslationKey.ROOM_STATUS_ALL to "All",

      TranslationKey.ROOM_GROUP_INFO_TITLE to "Group Information",
      TranslationKey.ROOM_GROUP_INFO_GROUP_NAME_LABEL to "Groupname",
      TranslationKey.ROOM_GROUP_INFO_GROUP_SIZE_LABEL to "Group Size",
      TranslationKey.ROOM_GROUP_INFO_GROUP_BUTTON to "Continue",

      TranslationKey.ROOM_INTRO_TEXT to "When you are ready to begin with the room click on the start button.",
      TranslationKey.ROOM_INTRO_START_BUTTON to "Start with the room",

      TranslationKey.ROOM_OUTRO_TITLE to "Room Completed",
      TranslationKey.ROOM_OUTRO_TEXT to "",

      TranslationKey.ROOM_UNAVAILABLE_TITLE to "The room is unavailable!",
      TranslationKey.ROOM_UNAVAILABLE_ROOM_NOT_READY to "The room is currently being set up and not yet ready.",
      TranslationKey.ROOM_UNAVAILABLE_ROOM_CLOSED to "The room is closed - participation is no longer possible",

      TranslationKey.ROOM_FINISH_DIALOG_BUTTON to "Finish Room",
      TranslationKey.ROOM_FINISH_DIALOG_CONFIRMATION_TEXT to "Are you ready to finish the room?",

      TranslationKey.ROOM_IMAGE_HELP_TEXT to "mark a spot with an error with a click on the image",

      TranslationKey.ROOM_IMAGE_SLIDER_NEXT to "Next Image",
      TranslationKey.ROOM_IMAGE_SLIDER_BACK to "Previous Image",

      TranslationKey.ROOM_ANSWER_ANSWERS_TOTAL to "Total {answers} answers",
      TranslationKey.ROOM_ANSWER_ANSWERS_COUNT_PER_IMAGE to "Image {number}: {answers} answers",

      TranslationKey.ROOM_ANSWER_INPUT_PLACEHOLDER to "your answer",
      TranslationKey.ROOM_ANSWER_INPUT_PLACEHOLDER_DISABLED to "mark a spot with an error",

      TranslationKey.ROOM_TIME_LIMIT_REACHED_DIALOG_TITLE to "Time's up!",
      TranslationKey.ROOM_TIME_LIMIT_REACHED_DIALOG_TEXT to "Your available time for the room is used up.",

      TranslationKey.ROOM_RESTART_TIMER_COUNTING to "Room will be reset in {time}.",
      TranslationKey.ROOM_RESTART_TIMER_ROOM_RESETTING to "Resetting Room.",

      TranslationKey.ROOM_QRCODE_IMAGE_LABEL to "ROOM CODE",
      TranslationKey.ROOM_QRCODE_BUTTON_DOWNLOAD to "Save Code",
      TranslationKey.ROOM_QRCODE_ALERT_DOWNLOAD_SUCCESS to "Successfully generate and downloaded image",
      TranslationKey.ROOM_QRCODE_ALERT_DOWNLOAD_ERROR to "Could not generate and download image",

      TranslationKey.ROOM_MANAGEMENT_INCOMPLETE_ROOM_ALERT to "The room is incomplete an can not be opened",

      TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_LABEL_PARTICIPANTS to "participants: {participants}",
      TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_LABEL_TOTAL_ANSWERS to "total answers: {answers}",
      TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_LABEL_WITH_GROUP_INFORMATION to "collect group information: {boolean}",
      TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_LABEL_GROUPS to "groups: {number}",
      TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_LABEL_PARTICIPANTS_WITHOUT_ANSWERS to "participants / groups without answers: {participants}",
      TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_BUTTON_EXCEL_EXPORT to "Excel Export",
      TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_EXCEL_EXPORT_FILE_NAME to "room-export-{roomCode}.xlsx",

      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_DIALOG_BUTTON to "Create Room",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_DIALOG_TITLE to "Create a new Room",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_CODE_LABEL to "ROOM CODE",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_TITLE_LABEL to "Room Title",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_DESCRIPTION_LABEL to "Room Description",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_DESCRIPTION_NOT_DEFINED to "There is no room description set",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_QUESTION_LABEL to "Room Question",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_QUESTION_NOT_DEFINED to "There is no room question set",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_WITH_TIME_LIMIT_SWITCH_LABLE to "With time limit",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_TIME_LIMIT_LABLE to "Time limit in minutes",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_TIME_LIMIT_HELP_TEXT to "Optional, time limit to complete the room.",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_TIME_LIMIT_NOT_DEFINED to "There is no time limit defined",

      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_SINGLE_DEVICE_ROOM_SWITCH_LABLE to
          "Room is used on a stationary device, on one device",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_SINGLE_DEVICE_ROOM_SWITCH_HELP_TEXT to
          """
          Set if the room is meant to be displayed on a single device i.e. the room has a pc or tablet where the users enter the answers.
          when not checked the users are meant to use their own device to enter the answers.
          With this toggle you can control if an user can start a new session when the room is completed.
          Also set it up for the next user.
          """.trimIndent(),

      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_WITH_GROUP_INFO_SWITCH_LABEL to "Collect Group Information",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_WITH_GROUP_INFO_SWITCH_HELP_TEXT to
              "If active the user must enter a group name and the group size before they can continue with the room.",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_WITH_GROUP_INFO_TEXT_LABEL to "Help Text for the group information card",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_WITH_GROUP_INFO_TEXT_NOT_DEFINED to "There is no text for the group info card defined. There is noting displayed.",

      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_TITLE_LABEL to "Image Title",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_UPLOAD_LABEL to "upload room png image",
      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_UPLOAD_MISSING_FILE to "No file - require png image",

      TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_INTERACTION_INFO_FORM_SWITCH to "Upload Video",

      TranslationKey.ROOM_MANAGEMENT_OPEN_ROOM_BUTTON to "Room is Ready - Open Room",
      TranslationKey.ROOM_MANAGEMENT_OPEN_ROOM_BUTTON_DISABLED to "Room is not Ready can not open the room",
      TranslationKey.ROOM_MANAGEMENT_OPEN_ROOM_TEXT to "Is everything ready, all images set? ",
      TranslationKey.ROOM_MANAGEMENT_CLOSE_ROOM_BUTTON to "Close the room - we are done.",
      TranslationKey.ROOM_MANAGEMENT_CLOSE_ROOM_TEXT to "Are you sure you are done with the room?",
      TranslationKey.ROOM_MANAGEMENT_RE_OPEN_ROOM_BUTTON to "Re open the room",
      TranslationKey.ROOM_MANAGEMENT_RE_OPEN_ROOM_TEXT to "Are you sure you want to open the room again?",

      TranslationKey.ROOM_MANAGEMENT_OPEN_ROOM_IMAGE_VALIDATION_ERROR to "Room has no files, can not open room",

      TranslationKey.ROOM_MANAGEMENT_DELETE_ROOM_BUTTON to "Delete Room",
      TranslationKey.ROOM_MANAGEMENT_DELETE_ROOM_TEXT to "Are you sure you want to delete the room, everything associated with the room, images, answers are permanently deleted!",

      TranslationKey.ROOM_EXCEL_EXPORT_INFO_CARD_ROOM_CODE to "Room Code",
      TranslationKey.ROOM_EXCEL_EXPORT_INFO_CARD_TITLE to "Title",
      TranslationKey.ROOM_EXCEL_EXPORT_INFO_CARD_DESCRIPTION to "Room description",
      TranslationKey.ROOM_EXCEL_EXPORT_INFO_CARD_QUESTION to "Display question",
      TranslationKey.ROOM_EXCEL_EXPORT_INFO_CARD_IMAGES to "Number of images",
      TranslationKey.ROOM_EXCEL_EXPORT_INFO_CARD_PARTICIPANTS to "Participants",
      TranslationKey.ROOM_EXCEL_EXPORT_INFO_CARD_PARTICIPANTS_WITHOUT_ANSWERS to "Participants / Groups that didn't enter an answer",
      TranslationKey.ROOM_EXCEL_EXPORT_INFO_CARD_TOTAL_ANSWERS to "Total answers",
      TranslationKey.ROOM_EXCEL_EXPORT_INFO_CARD_COLLECT_GROUP_INFO to "Collect Group Information",
      TranslationKey.ROOM_EXCEL_EXPORT_INFO_CARD_GROUPS to "Group Count",

      TranslationKey.ROOM_EXCEL_EXPORT_HEADER_USER_NO to "userNo",
      TranslationKey.ROOM_EXCEL_EXPORT_HEADER_ANSWER_NO to "answerNo",
      TranslationKey.ROOM_EXCEL_EXPORT_HEADER_ANSWER to "answer",
      TranslationKey.ROOM_EXCEL_EXPORT_HEADER_IMAGE_NO to "imageNo",
      TranslationKey.ROOM_EXCEL_EXPORT_HEADER_TOTAL_ANSWERS_PER_USER to "totalAnswersPerUser",
      TranslationKey.ROOM_EXCEL_EXPORT_HEADER_GROUP_NAME to "groupName",
      TranslationKey.ROOM_EXCEL_EXPORT_HEADER_GROUP_SIZE to "groupSize",
      TranslationKey.ROOM_EXCEL_EXPORT_HEADER_STARTED to "startedAt",
      TranslationKey.ROOM_EXCEL_EXPORT_HEADER_FINISHED to "finishedAt",
      TranslationKey.ROOM_EXCEL_EXPORT_HEADER_TIME to "time",

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

    YES,
    NO,

    CONFIRMATION_BUTTON_YES,
    CONFIRMATION_BUTTON_NO,

    STAR_NEW_USER_SESSION_BUTTON,

    ROOM_NAVIGATOR_TITLE_INVALID_CODE,
    ROOM_NAVIGATOR_TITLE_WELCOME,
    ROOM_NAVIGATOR_INPUT_LABEL,
    ROOM_NAVIGATOR_INPUT_PLACEHOLDER,
    ROOM_NAVIGATOR_BUTTON,

    ROOM_INFO_LABEL_NAME,

    ROOM_STATUS_NOT_READY,
    ROOM_STATUS_OPEN,
    ROOM_STATUS_CLOSED,
    ROOM_STATUS_ALL,

    ROOM_GROUP_INFO_TITLE,
    ROOM_GROUP_INFO_GROUP_NAME_LABEL,
    ROOM_GROUP_INFO_GROUP_SIZE_LABEL,
    ROOM_GROUP_INFO_GROUP_BUTTON,

    ROOM_INTRO_TEXT,
    ROOM_INTRO_START_BUTTON,

    ROOM_OUTRO_TITLE,
    ROOM_OUTRO_TEXT,

    ROOM_UNAVAILABLE_TITLE,
    ROOM_UNAVAILABLE_ROOM_NOT_READY,
    ROOM_UNAVAILABLE_ROOM_CLOSED,

    ROOM_FINISH_DIALOG_BUTTON,
    ROOM_FINISH_DIALOG_CONFIRMATION_TEXT,

    ROOM_IMAGE_HELP_TEXT,

    ROOM_IMAGE_SLIDER_NEXT,
    ROOM_IMAGE_SLIDER_BACK,

    ROOM_ANSWER_ANSWERS_TOTAL,
    ROOM_ANSWER_ANSWERS_COUNT_PER_IMAGE,

    ROOM_ANSWER_INPUT_PLACEHOLDER,
    ROOM_ANSWER_INPUT_PLACEHOLDER_DISABLED,

    ROOM_TIME_LIMIT_REACHED_DIALOG_TITLE,
    ROOM_TIME_LIMIT_REACHED_DIALOG_TEXT,

    ROOM_RESTART_TIMER_COUNTING,
    ROOM_RESTART_TIMER_ROOM_RESETTING,

    ROOM_QRCODE_IMAGE_LABEL,
    ROOM_QRCODE_BUTTON_DOWNLOAD,
    ROOM_QRCODE_ALERT_DOWNLOAD_SUCCESS,
    ROOM_QRCODE_ALERT_DOWNLOAD_ERROR,

    ROOM_MANAGEMENT_INCOMPLETE_ROOM_ALERT,

    ROOM_MANAGEMENT_ROOM_LIST_LABEL_PARTICIPANTS,
    ROOM_MANAGEMENT_ROOM_LIST_LABEL_TOTAL_ANSWERS,
    ROOM_MANAGEMENT_ROOM_LIST_LABEL_WITH_GROUP_INFORMATION,
    ROOM_MANAGEMENT_ROOM_LIST_LABEL_GROUPS,
    ROOM_MANAGEMENT_ROOM_LIST_LABEL_PARTICIPANTS_WITHOUT_ANSWERS,
    ROOM_MANAGEMENT_ROOM_LIST_BUTTON_EXCEL_EXPORT,
    ROOM_MANAGEMENT_ROOM_LIST_EXCEL_EXPORT_FILE_NAME,

    ROOM_MANAGEMENT_CREATE_ROOM_DIALOG_BUTTON,
    ROOM_MANAGEMENT_CREATE_ROOM_DIALOG_TITLE,
    ROOM_MANAGEMENT_CREATE_ROOM_ROOM_CODE_LABEL,
    ROOM_MANAGEMENT_CREATE_ROOM_ROOM_TITLE_LABEL,
    ROOM_MANAGEMENT_CREATE_ROOM_ROOM_DESCRIPTION_LABEL,
    ROOM_MANAGEMENT_CREATE_ROOM_ROOM_DESCRIPTION_NOT_DEFINED,
    ROOM_MANAGEMENT_CREATE_ROOM_ROOM_QUESTION_LABEL,
    ROOM_MANAGEMENT_CREATE_ROOM_ROOM_QUESTION_NOT_DEFINED,
    ROOM_MANAGEMENT_CREATE_ROOM_ROOM_WITH_TIME_LIMIT_SWITCH_LABLE,
    ROOM_MANAGEMENT_CREATE_ROOM_ROOM_TIME_LIMIT_LABLE,
    ROOM_MANAGEMENT_CREATE_ROOM_ROOM_TIME_LIMIT_HELP_TEXT,
    ROOM_MANAGEMENT_CREATE_ROOM_ROOM_TIME_LIMIT_NOT_DEFINED,
    ROOM_MANAGEMENT_CREATE_ROOM_SINGLE_DEVICE_ROOM_SWITCH_LABLE,
    ROOM_MANAGEMENT_CREATE_ROOM_SINGLE_DEVICE_ROOM_SWITCH_HELP_TEXT,

    ROOM_MANAGEMENT_CREATE_ROOM_WITH_GROUP_INFO_SWITCH_LABEL,
    ROOM_MANAGEMENT_CREATE_ROOM_WITH_GROUP_INFO_SWITCH_HELP_TEXT,
    ROOM_MANAGEMENT_CREATE_ROOM_WITH_GROUP_INFO_TEXT_LABEL,
    ROOM_MANAGEMENT_CREATE_ROOM_WITH_GROUP_INFO_TEXT_NOT_DEFINED,

    ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_TITLE_LABEL,
    ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_UPLOAD_LABEL,
    ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_UPLOAD_MISSING_FILE,

    ROOM_MANAGEMENT_CREATE_ROOM_INTERACTION_INFO_FORM_SWITCH,

    ROOM_MANAGEMENT_OPEN_ROOM_BUTTON,
    ROOM_MANAGEMENT_OPEN_ROOM_BUTTON_DISABLED,
    ROOM_MANAGEMENT_OPEN_ROOM_TEXT,
    ROOM_MANAGEMENT_CLOSE_ROOM_BUTTON,
    ROOM_MANAGEMENT_CLOSE_ROOM_TEXT,
    ROOM_MANAGEMENT_RE_OPEN_ROOM_BUTTON,
    ROOM_MANAGEMENT_RE_OPEN_ROOM_TEXT,

    ROOM_MANAGEMENT_OPEN_ROOM_IMAGE_VALIDATION_ERROR,

    ROOM_MANAGEMENT_DELETE_ROOM_BUTTON,
    ROOM_MANAGEMENT_DELETE_ROOM_TEXT,

    ROOM_EXCEL_EXPORT_INFO_CARD_ROOM_CODE,
    ROOM_EXCEL_EXPORT_INFO_CARD_TITLE,
    ROOM_EXCEL_EXPORT_INFO_CARD_DESCRIPTION,
    ROOM_EXCEL_EXPORT_INFO_CARD_QUESTION,
    ROOM_EXCEL_EXPORT_INFO_CARD_IMAGES,
    ROOM_EXCEL_EXPORT_INFO_CARD_PARTICIPANTS,
    ROOM_EXCEL_EXPORT_INFO_CARD_PARTICIPANTS_WITHOUT_ANSWERS,
    ROOM_EXCEL_EXPORT_INFO_CARD_TOTAL_ANSWERS,
    ROOM_EXCEL_EXPORT_INFO_CARD_COLLECT_GROUP_INFO,
    ROOM_EXCEL_EXPORT_INFO_CARD_GROUPS,

    ROOM_EXCEL_EXPORT_HEADER_USER_NO,
    ROOM_EXCEL_EXPORT_HEADER_ANSWER_NO,
    ROOM_EXCEL_EXPORT_HEADER_ANSWER,
    ROOM_EXCEL_EXPORT_HEADER_IMAGE_NO,
    ROOM_EXCEL_EXPORT_HEADER_TOTAL_ANSWERS_PER_USER,
    ROOM_EXCEL_EXPORT_HEADER_GROUP_NAME,
    ROOM_EXCEL_EXPORT_HEADER_GROUP_SIZE,
    ROOM_EXCEL_EXPORT_HEADER_STARTED,
    ROOM_EXCEL_EXPORT_HEADER_FINISHED,
    ROOM_EXCEL_EXPORT_HEADER_TIME,

  }

  sealed class TemplateProperty(key: String, open val value: String) {
    val propertyKey = "{$key}"

    data class RoomCode(override val value: String) : TemplateProperty("roomCode", value)
    data class RoomTitle(override val value: String) : TemplateProperty("roomTitle", value)
    data class Participants(val number: kotlin.Number) : TemplateProperty("participants", number.toString())
    data class Answers(val number: kotlin.Number) : TemplateProperty("answers", number.toString())
    data class Number(val number: kotlin.Number) : TemplateProperty("number", number.toString())
    data class Time(override val value: String) : TemplateProperty("time", value)
    data class BooleanParam(val bool: Boolean, val language: Language) :
      TemplateProperty("boolean", if (bool) I18n.get(language, TranslationKey.YES) else I18n.get(language, TranslationKey.NO))
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

