package components

import csstype.AlignItems
import csstype.Display
import csstype.FlexDirection
import mui.material.Box
import mui.material.CircularProgress
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props

val LoadingSpinner = FC<Props> {
  Box {
    sx {
      display = Display.flex
      flexDirection = FlexDirection.column
      alignItems = AlignItems.center
    }
    Spacer {
      size = SpacerPropsSize.MEDIUM
    }
    CircularProgress { }
    Spacer {
      size = SpacerPropsSize.SMALL
    }
    Typography {
      variant = TypographyVariant.body1
      +"Loading Data"
    }
  }
}