package components

import com.fynnian.application.common.I18n
import csstype.JustifyContent
import csstype.TextAlign
import csstype.pct
import csstype.rem
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.useContext
import react.useState

external interface RoomFinishDialogProps : Props {
  var finishingAction: () -> Unit
}

val RoomFinishDialog = FC<RoomFinishDialogProps> { props ->

  var showConfirmation by useState(false)
  val language by useContext(LanguageContext)

  Box {
    sx {
      padding = 0.5.rem
    }
    if (showConfirmation) {
      Card {
        CardContent {
          sx {
            textAlign = TextAlign.center
          }
          Typography {
            variant = TypographyVariant.body1
            +I18n.get(language, I18n.TranslationKey.ROOM_FINISH_DIALOG_CONFIRMATION_TEXT)
          }
        }
        CardActions {
          sx {
            justifyContent = JustifyContent.spaceAround
          }
          Button {
            +I18n.get(language, I18n.TranslationKey.ROOM_FINISH_DIALOG_CONFIRMATION_BUTTON_YES)
            onClick = { props.finishingAction() }
          }
          Button {
            +I18n.get(language, I18n.TranslationKey.ROOM_FINISH_DIALOG_CONFIRMATION_BUTTON_NO)
            onClick = { showConfirmation = false }
          }
        }
      }
    } else {
      Button {
        sx {
          width = 100.pct
        }
        +I18n.get(language, I18n.TranslationKey.ROOM_FINISH_DIALOG_BUTTON)
        onClick = { showConfirmation = true }
      }
    }
  }
}