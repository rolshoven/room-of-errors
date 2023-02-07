package components

import com.fynnian.application.common.I18n
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
import react.useContext

val LoadingSpinner = FC<Props> {
  val (language) = useContext(LanguageContext)

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
      +I18n.get(language, I18n.TranslationKey.LOADING_SPINNER)
    }
  }
}