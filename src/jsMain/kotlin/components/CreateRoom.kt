package components

import api.RoomManagementApi
import com.fynnian.application.common.I18n
import com.fynnian.application.common.URLS
import com.fynnian.application.common.URLS.replaceParam
import com.fynnian.application.common.room.*
import components.form.FromRoomTitle
import csstype.*
import js.core.jso
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.icons.material.Cached
import mui.icons.material.Clear
import mui.icons.material.Save
import mui.material.*
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.onChange
import react.router.useNavigate
import web.html.InputType

private val scope = MainScope()

val CreateRoomDialog = FC<Props> {

  val api = RoomManagementApi()

  val language by useContext(LanguageContext)
  val navigate = useNavigate()

  var generatedCode: String by useState(Room.genRoomCode())
  var open: Boolean by useState(false)
  val (roomTitle, setRoomTitle) = useState("")
  var successful: Boolean? by useState(null)

  fun close() {
    setRoomTitle("")
    open = false
    successful = null
  }

  fun createRoom() {
    scope.launch {
      api.createRoom(RoomCreation(code = generatedCode, title = roomTitle))
        .let {
          if (it != null) navigate(URLS.MANAGEMENT_ROOM_DETAIL.replaceParam(URLS.ROOM_CODE_PARAM(generatedCode)))
          else successful = false
        }
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
          required = true
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
          Cached()
          onClick = { generatedCode = Room.genRoomCode() }
        }
      }
      FromRoomTitle {
        this.language = language
        this.title = roomTitle
        this.setTitle = setRoomTitle
      }
    }
    DialogActions {
      IconButton {
        Clear()
        color = IconButtonColor.primary
        onClick = { close() }
      }
      IconButton {
        Save()
        disabled = roomTitle.isBlank() && successful != null
        color = IconButtonColor.primary
        onClick = { createRoom() }
      }
    }
  }
}
