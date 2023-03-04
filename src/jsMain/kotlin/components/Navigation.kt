package components

import com.fynnian.application.common.URLS
import com.fynnian.application.common.URLS.replaceParam
import csstype.*
import mui.icons.material.*
import mui.material.*
import mui.system.sx
import react.*
import react.dom.html.ReactHTML.nav
import react.router.useNavigate


val Navigation = FC<PropsWithChildren> { props ->
  val (theme, setTheme) = useContext(ThemeContext)
  val navigate = useNavigate()

  Box {
    component = nav
    sx {
      flexGrow = number(1.0)
    }
    AppBar {
      position = AppBarPosition.static
      sx {
        alignItems = AlignItems.end
        flexDirection = FlexDirection.row
        justifyContent = JustifyContent.spaceBetween
        gap = 1.rem
      }
      Button {
        id = "home"
        onClick = { navigate(URLS.HOME) }
        House {
          sx {
            color = if (theme == Themes.Dark) NamedColor.white else NamedColor.black
          }
        }
      }
      Box {
        sx {
          gap = 1.rem
        }
        NewUserSessionButton() // ToDo: check with data or after finishing room
        ThemeSwitch()
        LanguageSwitch()
      }
    }
  }
  +props.children
}


external interface ToPageProps : Props {
  var text: String?
  var url: String

  var alternativeIcon: SvgIconComponent?
}

val ToPage = FC<ToPageProps> { props ->
  val navigate = useNavigate()

  if (props.text != null)
    Button {
      endIcon = createElement(Launch)
      onClick = { navigate(props.url) }
      +props.text!!
    }
  else
    IconButton {
      props.alternativeIcon?.let{ it() } ?: Launch()
      color = IconButtonColor.primary
      onClick = { navigate(props.url) }
    }
}

val ToManagementPage = FC<Props> {
  ToPage {
    text = "Back to Overview Page"
    url = URLS.MANAGEMENT
  }
}

external interface ToRoomProps : Props {
  var code: String
}

val ToRoom = FC<ToRoomProps> { props ->
  ToPage {
    url = URLS.ROOM.replaceParam(URLS.ROOM_CODE_PARAM(props.code))
    alternativeIcon = MeetingRoom
  }
}

val ToRoomManagementDetail = FC<ToRoomProps> { props ->
  ToPage {
    url = URLS.MANAGEMENT_ROOM_DETAIL.replaceParam(URLS.ROOM_CODE_PARAM(props.code))
    alternativeIcon = Settings
  }
}