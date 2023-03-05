package components

import csstype.vh
import mui.material.Container
import mui.system.sx
import react.FC
import react.Props
import react.dom.html.ReactHTML

val Footer = FC<Props> {
  Container {
    maxWidth = "lg"
    component = ReactHTML.footer
    disableGutters = true
    sx {
      minHeight = 10.vh
    }
  }
}