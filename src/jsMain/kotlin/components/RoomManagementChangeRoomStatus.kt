package components

import api.RoomManagementApi
import com.fynnian.application.common.I18n
import com.fynnian.application.common.room.RoomManagementDetail
import com.fynnian.application.common.room.RoomStatus
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
import react.useContext
import react.useState

private val scope = MainScope()

external interface RoomManagementChangeRoomStatusProps : Props {
  var code: String
  var isRoomReadyForOpening: Boolean
  var roomStatus: RoomStatus
  var setRoom: (room: RoomManagementDetail?) -> Unit
}

val RoomManagementChangeRoomStatus = FC<RoomManagementChangeRoomStatusProps> { props ->

  val api = RoomManagementApi()
  val (language) = useContext(LanguageContext)
  val (showConfirmation, setShowConfirmation) = useState(false)

  val isDisabled = props.roomStatus == RoomStatus.NOT_READY && props.isRoomReadyForOpening.not()
  val (buttonText, confirmationQuestion) = when {
    isDisabled -> Pair(
      I18n.TranslationKey.ROOM_MANAGEMENT_OPEN_ROOM_BUTTON_DISABLED,
      I18n.TranslationKey.ROOM_MANAGEMENT_OPEN_ROOM_TEXT
    )

    props.roomStatus == RoomStatus.NOT_READY -> Pair(
      I18n.TranslationKey.ROOM_MANAGEMENT_OPEN_ROOM_BUTTON,
      I18n.TranslationKey.ROOM_MANAGEMENT_OPEN_ROOM_TEXT
    )

    props.roomStatus == RoomStatus.OPEN -> Pair(
      I18n.TranslationKey.ROOM_MANAGEMENT_CLOSE_ROOM_BUTTON,
      I18n.TranslationKey.ROOM_MANAGEMENT_CLOSE_ROOM_TEXT
    )

    else -> Pair(
      I18n.TranslationKey.ROOM_MANAGEMENT_RE_OPEN_ROOM_BUTTON,
      I18n.TranslationKey.ROOM_MANAGEMENT_RE_OPEN_ROOM_TEXT
    )
  }

  fun roomAction() {
    scope.launch {
      val result = when (props.roomStatus) {
        RoomStatus.NOT_READY -> api.openRoom(props.code)
        RoomStatus.OPEN -> api.closeRoom(props.code)
        RoomStatus.CLOSED -> api.openRoom(props.code)
      }
      setShowConfirmation(false)
      props.setRoom(result)
    }
  }

  Box {
    sx {
      width = 100.pct
      padding = 0.5.rem
    }
    if (showConfirmation) {
      Card {
        CardContent {
          sx {
            textAlign = TextAlign.center
          }
          Typography {
            variant = TypographyVariant.body1
            +I18n.get(language, confirmationQuestion)
          }
        }
        CardActions {
          sx {
            justifyContent = JustifyContent.spaceAround
          }
          Button {
            +I18n.get(language, I18n.TranslationKey.CONFIRMATION_BUTTON_YES)
            onClick = { roomAction() }
          }
          Button {
            +I18n.get(language, I18n.TranslationKey.CONFIRMATION_BUTTON_NO)
            onClick = { setShowConfirmation(false) }
          }
        }
      }
    } else {
      Button {
        sx {
          width = 100.pct
        }
        onClick = { setShowConfirmation(true) }
        disabled = isDisabled
        +I18n.get(language, buttonText)

      }
    }
  }


}