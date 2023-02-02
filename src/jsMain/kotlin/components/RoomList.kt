package components

import api.RoomManagementApi
import com.fynnian.application.common.AppPaths
import com.fynnian.application.common.I18n
import com.fynnian.application.common.room.RoomDetails
import csstype.FlexDirection
import csstype.rem
import mui.icons.material.FileDownload
import mui.icons.material.Launch
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.dom.aria.ariaLabel
import react.router.useNavigate
import react.useContext
import web.dom.document
import web.html.HTML

external interface RoomListProps : Props {
  var rooms: List<RoomDetails>
}

val RoomList = FC<RoomListProps> { props ->
  List {
    props.rooms.map {
      RoomListItem {
        room = it
      }
    }
  }
}

external interface RoomListItemProp : Props {
  var room: RoomDetails
}

val RoomListItem = FC<RoomListItemProp> { props ->
  val room = props.room
  val navigate = useNavigate()
  val (language) = useContext(LanguageContext)

  ListItem {
    sx {
      flexDirection = FlexDirection.row
      gap = 1.rem
    }
    Typography {
      variant = TypographyVariant.body1
      +room.code
    }
    Typography {
      variant = TypographyVariant.body1
      +room.title
    }
    Typography {
      variant = TypographyVariant.body1
      +I18n.get(
        language,
        I18n.TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_LABEL_PARTICIPANTS,
        I18n.TemplateProperty("participants", room.participants.toString())
      )
    }
    Typography {
      variant = TypographyVariant.body1
      +I18n.get(
        language,
        I18n.TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_LABEL_TOTAL_ANSWERS,
        I18n.TemplateProperty("answers", room.answers.toString())
      )
    }
    IconButton {
      Launch()
      color = IconButtonColor.primary
      ariaLabel = "go to room ${room.code}"
      onClick = { navigate(AppPaths.ROOM.path + "/${room.code}") }
    }
    RoomQRCodeDialog {
      roomCode = room.code
    }
    Button {
      +I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_BUTTON_EXCEL_EXPORT)
      FileDownload()
      variant = ButtonVariant.text
      ariaLabel = "Excel Export Room"
      onClick = {
        document.createElement(HTML.a)
          .apply {
            href = RoomManagementApi.roomExportUrl(room.code)
            download = I18n.get(
              language,
              I18n.TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_EXCEL_EXPORT_FILE_NAME,
              I18n.TemplateProperty("roomCode", room.code)
            )
          }
          .also {
            document.body.appendChild(it)
            it.click()
            document.body.removeChild(it)
          }
      }
    }
  }
}