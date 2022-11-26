package components

import com.fynnian.application.common.AppPaths
import csstype.*
import mui.icons.material.House
import mui.material.*
import mui.system.sx
import react.FC
import react.PropsWithChildren
import react.dom.html.ReactHTML.nav
import react.router.useNavigate
import react.useContext


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
        onClick = { navigate(AppPaths.HOME.path) }
        House {
          sx {
            color = if(theme == Themes.Dark) NamedColor.white else NamedColor.black
          }
        }
      }
      ThemeSwitch()
    }
  }
  + props.children
}
