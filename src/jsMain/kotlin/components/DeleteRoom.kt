package components

import api.RoomManagementApi
import com.fynnian.application.common.I18n
import com.fynnian.application.common.URLS
import csstype.JustifyContent
import csstype.TextAlign
import csstype.pct
import csstype.rem
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.router.useNavigate
import react.useContext
import react.useState

private val scope = MainScope()

external interface DeleteRoomProps : Props {
  var code: String
}

val DeleteRoom = FC<DeleteRoomProps> { props ->

  val api = RoomManagementApi()
  val (language) = useContext(LanguageContext)
  val (showConfirmation, setShowConfirmation) = useState(false)
  val navigate = useNavigate()


  fun deleteRoom() {

    scope.launch {

      val result = api.deleteRoom(props.code)

      if (result == null) {
        setShowConfirmation(false)
        navigate(URLS.MANAGEMENT)
      } else {

      }
    }
  }

  if (showConfirmation) {
    Card {
      sx {
        width = 100.pct
        padding = 0.5.rem
      }
      CardContent {
        sx {
          textAlign = TextAlign.center
        }
        Typography {
          variant = TypographyVariant.body1
          +I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_DELETE_ROOM_TEXT)
        }
      }
      CardActions {
        sx {
          justifyContent = JustifyContent.spaceAround
        }
        Button {
          +I18n.get(language, I18n.TranslationKey.CONFIRMATION_BUTTON_YES)
          onClick = { deleteRoom() }
        }
        Button {
          +I18n.get(language, I18n.TranslationKey.CONFIRMATION_BUTTON_NO)
          onClick = { setShowConfirmation(false) }
        }
      }
    }
  } else {
    Button {
      color = ButtonColor.error
      variant = ButtonVariant.outlined
      onClick = { setShowConfirmation(true) }
      +I18n.get(language,I18n.TranslationKey.ROOM_MANAGEMENT_DELETE_ROOM_BUTTON)

    }
  }
}