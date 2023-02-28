package components

import com.fynnian.application.common.I18n
import com.fynnian.application.common.room.RoomStatements
import csstype.TextAlign
import csstype.pct
import csstype.px
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.Breakpoint
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.useContext
import workarounds.controls

enum class RoomStatementVariant {
  INTRO,
  OUTRO
}

external interface RoomStatementProps : Props {
  var type: RoomStatementVariant
  var statement: RoomStatements
  var cardAction: () -> Unit
}

val RoomStatement = FC<RoomStatementProps> { props ->
  val (language) = useContext(LanguageContext)

  Card {
    if (props.statement.videoURl != null) {
      Spacer {
        size = SpacerPropsSize.SMALL
      }
      CardMedia {
        component = ReactHTML.video
        src = props.statement.videoURl
        sx {
          maxHeight = responsive(
            Breakpoint.md to 500.px,
            Breakpoint.xs to 300.px
          )
        }
        controls = true
      }
    }
    Spacer {
      size = SpacerPropsSize.SMALL
    }
    CardContent {
      sx {
        textAlign = TextAlign.center
      }
      Typography {
        val text = props.statement.text ?: when (props.type) {
          RoomStatementVariant.INTRO -> I18n.get(language, I18n.TranslationKey.ROOM_INTRO_TEXT)
          RoomStatementVariant.OUTRO -> I18n.get(language, I18n.TranslationKey.ROOM_OUTRO_TEXT)
        }
        variant = TypographyVariant.body1
        +text
      }
    }

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
  }
}