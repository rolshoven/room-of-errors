package components

import api.RoomManagementApi
import com.benasher44.uuid.uuid4
import com.fynnian.application.common.I18n
import com.fynnian.application.common.room.Room
import com.fynnian.application.common.room.RoomImage
import com.fynnian.application.common.room.RoomStatus
import csstype.*
import js.core.jso
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.icons.material.Cached
import mui.icons.material.Clear
import mui.icons.material.Save
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.responsive
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.dom.onChange
import web.file.File
import web.html.HTMLTextAreaElement
import web.html.InputType
import workarounds.component

private val scope = MainScope()

external interface CreateRoomDialogProps : Props {
  var reloadRooms: () -> Unit
}

val CreateRoomDialog = FC<CreateRoomDialogProps> { props ->

  val api = RoomManagementApi()

  val (language) = useContext(LanguageContext)

  var generatedCode: String by useState(genRoomCode())
  var open: Boolean by useState(false)
  var roomTitle: String by useState("")
  var description: String by useState("")
  var question: String by useState("")
  var imageTitle: String by useState("")
  var file: File? by useState(null)

  fun resetAll() {
    roomTitle = ""
    description = ""
    question = ""
    imageTitle = ""
    file = null
  }

  fun createRoom() {
    scope.launch {
      api.createRoomWithUpload(
        Room(
          code = generatedCode,
          roomStatus = RoomStatus.OPEN,
          title = roomTitle,
          description = description,
          question = question,
          timeLimitMinutes = 0, // ToDo,
          startingText = null,
          startingVideoTitle = null,
          startingVideoUrl = null,
          endingText = null,
          endingVideoTitle = null,
          endingVideoUrl = null,
          images = listOf(
            RoomImage(
              id = uuid4(),
              title = imageTitle,
              url = "ignored"
            )
          )
        ),
        file!! // ToDO: proper check
      )
      props.reloadRooms()
    }
  }

  Button {
    variant = ButtonVariant.outlined
    onClick = { open = true }
    +I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_DIALOG_BUTTON)
  }
  Dialog {
    this.open = open
    maxWidth = "sm"
    fullWidth = true

    DialogTitle {
      +I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_DIALOG_TITLE)
    }

    DialogContent {
      sx {
        display = Display.flex
        flexDirection = FlexDirection.column
        gap = 0.5.rem
      }
      Spacer {
        size = SpacerPropsSize.SMALL
      }
      FormGroup {
        row = true
        sx {
          gap = 1.rem
        }
        TextField {
          id = "roomCode"
          name = "roomCode"
          type = InputType.text
          label = ReactNode(I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_CODE_LABEL))
          value = generatedCode
          inputProps = jso {
            this as InputBaseProps
            readOnly = true
          }
          sx {
            flex = number(2.0)
          }
        }
        IconButton {
          onClick = {
            generatedCode = genRoomCode()
          }
          Cached { }
        }
      }
      FormGroup {
        TextField {
          id = "title"
          name = "title"
          type = InputType.text
          required = true
          label = ReactNode(I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_TITLE_LABEL))
          placeholder = "my room"
          value = roomTitle
          onChange = {
            val e = it.target as HTMLInputElement
            roomTitle = e.value
          }
        }
      }
      FormGroup {
        TextField {
          id = "description"
          name = "description"
          type = InputType.text
          required = true
          multiline = true
          label = ReactNode(I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_DESCRIPTION_LABEL))
          placeholder = "..."
          value = description
          onChange = {
            val e = it.target as HTMLTextAreaElement
            description = e.value
          }
        }
      }
      FormGroup {
        TextField {
          id = "question"
          name = "question"
          type = InputType.text
          required = true
          multiline = true
          label = ReactNode(I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_QUESTION_LABEL))
          placeholder = "..."
          value = question
          onChange = {
            val e = it.target as HTMLTextAreaElement
            question = e.value
          }
        }
      }
      FormGroup {
        TextField {
          id = "imageTitle"
          name = "imageTitle"
          type = InputType.text
          required = true
          label = ReactNode(I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_TITLE_LABEL))
          placeholder = "room 1"
          value = imageTitle
          onChange = {
            val e = it.target as HTMLInputElement
            imageTitle = e.value
          }
        }
      }
// ToDo: allow web url
//          TextField {
//            id = "imageUrl"
//            name = "imageUrl"
//            type = InputType.url
//            label = ReactNode("Image Url")
//            placeholder = "https://..."
//          }
      FormGroup {
        Button {
          variant = ButtonVariant.contained
          component = label
          +I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_UPLOAD_LABEL)
          input {
            hidden = true
            accept = "image/png"
            type = InputType.file
            onChange = {
              file = it.target.files?.item(0)
            }
          }
        }
        Stack {
          direction = responsive(StackDirection.row)
          spacing = responsive(2)
          sx {
            alignItems = AlignItems.center
          }
          Typography {
            variant = TypographyVariant.subtitle1
            +(file?.name ?: I18n.get(
              language,
              I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_UPLOAD_MISSING_FILE
            ))
          }
          IconButton {
            color = IconButtonColor.primary
            onClick = { file = null }
            Clear()
          }
        }
      }
    }
    DialogActions {
      IconButton {
        color = IconButtonColor.primary
        onClick = {
          resetAll()
          open = false
        }
        Clear()
      }
      IconButton {
        disabled = file == null
        color = IconButtonColor.primary
        onClick = {
          createRoom()
          open = false
        }
        Save()
      }
    }
  }
}

fun genRoomCode(): String {
  return uuid4().toString().split("-")[0]
}