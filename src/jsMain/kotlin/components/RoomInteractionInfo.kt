package components

import com.fynnian.application.common.I18n
import com.fynnian.application.common.room.RoomInteractionInfo
import com.fynnian.application.common.room.RoomStatementVariant
import csstype.FlexWrap
import csstype.TextAlign
import csstype.px
import js.core.jso
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.Breakpoint
import mui.system.responsive
import mui.system.sx
import react.*
import react.dom.html.ReactHTML
import web.html.HTMLVideoElement
import workarounds.controls
import workarounds.ref

external interface RoomInteractionInfoProps : PropsWithChildren {
  var type: RoomStatementVariant
  var interactionInfo: RoomInteractionInfo
  var videoRef: Ref<HTMLVideoElement>?
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
        ref = props.videoRef
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
    if (props.children != null) {
      Spacer { size = SpacerPropsSize.MEDIUM }
      CardActions {
        sx {
          flexWrap = FlexWrap.wrap
        }
        +props.children
      }
    }
  }
}