package components

import csstype.AlignItems
import csstype.number
import mui.material.AppBar
import mui.material.AppBarPosition
import mui.material.Box
import mui.system.sx
import react.FC
import react.PropsWithChildren
import react.dom.html.ReactHTML.nav


val Navigation = FC<PropsWithChildren> { props ->
  Box {
    component = nav
    sx {
      flexGrow = number(1.0)
    }
    AppBar {
      position = AppBarPosition.static
      sx {
        alignItems = AlignItems.end
      }
      ThemeSwitch()
    }
  }
  + props.children
}
