import com.fynnian.application.common.I18n
import com.fynnian.application.common.Language
import csstype.Display
import csstype.FlexDirection
import csstype.px
import csstype.rem
import mui.icons.material.HelpOutline
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode

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

external interface BinaryLabelProps : Props {
  var state: Boolean
  var language: Language
}

val BinaryLabel = FC<BinaryLabelProps> { props ->
  Typography {
    variant = TypographyVariant.body1
    +if (props.state) I18n.get(props.language, I18n.TranslationKey.YES)
    else I18n.get(props.language, I18n.TranslationKey.NO)
  }
}

external interface LabelWithHelpTextProps : Props {
  var language: Language
  var labelKey: I18n.TranslationKey
  var helpTextKey: I18n.TranslationKey
}

val LabelWithHelpText = FC<LabelWithHelpTextProps> { props ->
  Box {
    sx {
      display = Display.flex
      flexDirection = FlexDirection.row
      gap = 0.1.rem
    }
    Typography {
      variant = TypographyVariant.caption
      +I18n.get(props.language, props.labelKey)
    }
    Tooltip {
      title = ReactNode(I18n.get(props.language, props.helpTextKey))
      placement = TooltipPlacement.bottom
      HelpOutline {
        fontSize = SvgIconSize.small
      }
    }
  }
}