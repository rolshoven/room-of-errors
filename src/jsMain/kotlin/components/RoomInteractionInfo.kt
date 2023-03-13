package components

import com.fynnian.application.common.I18n
import com.fynnian.application.common.room.RoomStatementVariant
import com.fynnian.application.common.room.RoomInteractionInfo
import csstype.TextAlign
import csstype.pct
import csstype.px
import js.core.jso
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.Breakpoint
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import react.dom.html.ReactHTML
import react.useContext
import workarounds.controls

external interface RoomInteractionInfoProps : Props {
  var type: RoomStatementVariant
  var interactionInfo: RoomInteractionInfo
  var cardAction: () -> Unit
}

val RoomInteractionInfo = FC<RoomInteractionInfoProps> { props ->
  val (language) = useContext(LanguageContext)

  Card {
    if (props.type == RoomStatementVariant.OUTRO) CardHeader {
      sx {
        textAlign = TextAlign.center
      }
      title = ReactNode(I18n.get(language, I18n.TranslationKey.ROOM_OUTRO_TITLE))
      titleTypographyProps = jso {
        variant = TypographyVariant.body1
      }
    }
    Spacer {
      size = SpacerPropsSize.SMALL
    }
    if (props.type == RoomStatementVariant.INTRO) CardContent {
      sx {
        textAlign = TextAlign.center
      }
      Typography {
        variant = TypographyVariant.body1
        +(props.interactionInfo.text ?: I18n.get(language, I18n.TranslationKey.ROOM_INTRO_TEXT))
      }
    }
    if (props.interactionInfo.videoURl != null) {
      Spacer {
        size = SpacerPropsSize.SMALL
      }
      CardMedia {
        component = ReactHTML.video
        src = props.interactionInfo.videoURl
        sx {
          maxHeight = responsive(
            Breakpoint.md to 500.px,
            Breakpoint.xs to 300.px
          )
        }
        controls = true
      }
    }
    Spacer { size = SpacerPropsSize.MEDIUM }
    if (props.type == RoomStatementVariant.INTRO) {
      CardActions {
        Button {
          sx {
            width = 100.pct
          }
          +I18n.get(language, I18n.TranslationKey.ROOM_INTRO_START_BUTTON)
          onClick = { props.cardAction() }
        }
      }
    }
    if (props.type == RoomStatementVariant.OUTRO) NewUserSessionButton()
  }
}