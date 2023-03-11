import csstype.px
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props

external interface MissingContentProps : Props {
  var text: String
}

val MissingContent = FC<MissingContentProps> { props ->
  Typography {
    sx {
      margin = 0.px
    }
    paragraph = true
    variant = TypographyVariant.overline
    +props.text
  }
}