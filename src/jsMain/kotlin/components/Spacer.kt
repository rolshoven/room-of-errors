package components

import csstype.Height
import csstype.rem
import mui.material.Box
import mui.system.sx
import react.FC
import react.Props

external interface SpacerProps : Props {
  var size: SpacerPropsSize
}

enum class SpacerPropsSize(val height: Height) {
  SMALL(1.rem),
  MEDIUM(2.rem),
  LARGE(4.rem)
}

val Spacer = FC<SpacerProps> { props ->
  Box {
    sx {
      height = props.size.height
    }
  }
}