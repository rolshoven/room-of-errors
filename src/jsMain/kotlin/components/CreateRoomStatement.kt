package components

import api.RoomManagementApi
import com.fynnian.application.common.I18n
import com.fynnian.application.common.room.RoomStatementVariant
import com.fynnian.application.common.room.RoomStatements
import csstype.AlignItems
import csstype.Display
import csstype.FlexDirection
import csstype.rem
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
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

external interface CreateRoomStatementProps : Props {
  var code: String
  var variant: RoomStatementVariant
  var setStatement: StateSetter<RoomStatements?>
}

val CreateRoomStatementDialog = FC<CreateRoomStatementProps> { props ->

  val api = RoomManagementApi()

  val language by useContext(LanguageContext)

  val (open, setOpen) = useState(false)
  val (successful, setSuccessful) = useState<Boolean?>(null)
  val (title, setTitle) = useState<String?>(null)
  val (text, setText) = useState<String?>(null)
  val (file, setFile) = useState<File?>(null)

  fun close() {
    setFile(null)
    setTitle(null)
    setText(null)
    setSuccessful(null)
    setOpen(false)
  }

  fun isIncomplete() = title == null && file == null && successful != null

  fun addStatement() {
    scope.launch {
      api.addRoomStatementWithUpload(
        props.variant,
        props.code,
        text,
        title!!,
        file!!
      ).let {
        if (it != null) {
          close()
          props.setStatement(
            when (props.variant) {
              RoomStatementVariant.INTRO -> it.startingStatements
              RoomStatementVariant.OUTRO -> it.endingStatements
            }
          )
        } else setSuccessful(false)
      }
    }
  }

  Button {
    variant = ButtonVariant.outlined
    onClick = { setOpen(true) }
    //+ I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_DIALOG_BUTTON)
    +"Update ${props.variant}" // Todo
  }
  Dialog {
    this.open = open
    maxWidth = "sm"
    fullWidth = true

    DialogTitle {
      +"Update ${props.variant}" // Todo
    }

    DialogContent {
      sx {
        display = Display.flex
        flexDirection = FlexDirection.column
        gap = 0.5.rem
      }
      FormGroup {
        TextField {
          id = "description"
          name = "description"
          type = InputType.text
          placeholder = "some description or other ${props.variant.toString().lowercase()} text"
          helperText = ReactNode(
            """
            Optional text, additional ${props.variant.toString().lowercase()} text to guide the user. 
            """.trimIndent()
          )
          multiline = true
          minRows = 2
          fullWidth = true
          value = text ?: ""
          onChange = {
            val e = it.target as HTMLTextAreaElement
            setText(e.value.ifEmpty { null })
          }
        }
      }
      FormGroup {
        TextField {
          id = "videoTitle"
          name = "videoTitle"
          type = InputType.text
          required = true
          label =
            ReactNode("Video Title") // ReactNode(I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_TITLE_LABEL))
          placeholder = "intro"
          value = title ?: ""
          onChange = {
            val e = it.target as HTMLInputElement
            setTitle(e.value.ifEmpty { null })
          }
        }
      }
      FormGroup {
        Button {
          variant = ButtonVariant.contained
          component = label
          +I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_UPLOAD_LABEL) // Todo
          input {
            hidden = true
            accept = "video/mp4"
            type = InputType.file
            onChange = { setFile(it.target.files?.item(0)) }
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
              I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_UPLOAD_MISSING_FILE // Todo
            ))
          }
          IconButton {
            color = IconButtonColor.primary
            onClick = { setFile(null) }
            Clear()
          }
        }
      }
    }
    DialogActions {
      IconButton {
        color = IconButtonColor.primary
        onClick = { close() }
        Clear()
      }
      IconButton {
        disabled = isIncomplete()
        color = IconButtonColor.primary
        onClick = { addStatement() }
        Save()
      }
    }
  }
}
