package components

import api.RoomManagementApi
import com.fynnian.application.common.I18n
import csstype.*
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
import web.html.InputType
import workarounds.component

private val scope = MainScope()

external interface CreateRoomImageDialogProps : Props {
  var code: String
  var reloadImages: () -> Unit
}

val CreateRoomImageDialog = FC<CreateRoomImageDialogProps> { props ->

  val api = RoomManagementApi()

  val language by useContext(LanguageContext)

  val (open, setOpen) = useState(false)
  val (successful, setSuccessful) = useState<Boolean?>(null)
  val (title, setTitle) = useState("")
  val (file, setFile)= useState<File?>(null)

  fun close() {
    setFile(null)
    setTitle("")
    setSuccessful(null)
    setOpen(false)
  }

  fun addImage() {
    scope.launch {
      api.addRoomImageWithUpload(
        props.code,
        title,
        file!!
      ).let {
        if (it != null) {
          close()
          props.reloadImages()
        } else setSuccessful(false)
      }
    }
  }

  Button {
    variant = ButtonVariant.outlined
    onClick = { setOpen(true) }
    //+ I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_DIALOG_BUTTON)
    +" add image" // Todo
  }
  Dialog {
    this.open = open
    maxWidth = "sm"
    fullWidth = true

    DialogTitle {
      +"add image" // Todo
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
        TextField {
          id = "imageTitle"
          name = "imageTitle"
          type = InputType.text
          required = true
          label = ReactNode(I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_TITLE_LABEL))
          placeholder = "room 1"
          value = title
          onChange = {
            val e = it.target as HTMLInputElement
            setTitle(e.value)
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
            accept = "image/png"
            type = InputType.file
            onChange = {
              setFile(it.target.files?.item(0))
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
        onClick = {
          close()
        }
        Clear()
      }
      IconButton {
        disabled = title.isEmpty() && file == null && successful != null
        color = IconButtonColor.primary
        onClick = { addImage() }
        Save()
      }
    }
  }
}
