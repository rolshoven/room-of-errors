package components

import com.fynnian.application.common.I18n
import com.fynnian.application.common.room.RoomStatus
import csstype.TextAlign
import js.core.jso
import mui.material.Card
import mui.material.CardContent
import mui.material.CardHeader
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import react.useContext

external interface RoomUnavailableInfoProps : Props {
  var title: String
  var code: String
  var status: RoomStatus
}

val RoomUnavailableInfo = FC<RoomUnavailableInfoProps> { props ->
  val (language) = useContext(LanguageContext)

  Card {
    CardHeader {
      title = ReactNode(
        I18n.get(
          language,
          I18n.TranslationKey.ROOM_INFO_LABEL_NAME,
          I18n.TemplateProperty.RoomCode(props.code),
          I18n.TemplateProperty.RoomTitle(props.title)
        )
      )
      titleTypographyProps = jso {
        variant = TypographyVariant.body1
      }
    }
    CardContent {
      sx {
        textAlign = TextAlign.center
      }
      Typography {
        variant = TypographyVariant.h6
        +I18n.get(language, I18n.TranslationKey.ROOM_UNAVAILABLE_TITLE)
      }
      Typography {
        val text = when (props.status) {
          RoomStatus.NOT_READY -> I18n.get(language, I18n.TranslationKey.ROOM_UNAVAILABLE_ROOM_NOT_READY)
          RoomStatus.CLOSED -> I18n.get(language, I18n.TranslationKey.ROOM_UNAVAILABLE_ROOM_CLOSED)
          else -> ""
        }
        variant = TypographyVariant.body1
        +text
      }
    }
  }
}