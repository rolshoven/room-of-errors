package components

import com.fynnian.application.common.I18n
import com.fynnian.application.common.room.Room
import csstype.rem
import js.core.jso
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.PropsWithChildren
import react.ReactNode
import react.useContext

external interface RoomInfoProps : PropsWithChildren {
  var room: Room
}

val RoomInfo = FC<RoomInfoProps> { props ->
  val (language) = useContext(LanguageContext)

  Card {
    sx {
      padding = 0.5.rem
    }
    CardHeader {
      title = ReactNode(
        I18n.get(
          language,
          I18n.TranslationKey.ROOM_INFO_LABEL_NAME,
          I18n.TemplateProperty.RoomCode(props.room.code),
          I18n.TemplateProperty.RoomTitle(props.room.title)
        )
      )
      titleTypographyProps = jso {
        variant = TypographyVariant.body1
      }
    }
    if (!props.room.description.isNullOrBlank() || !props.room.question.isNullOrBlank()) CardContent {
      Typography {
        variant = TypographyVariant.body1
        +(props.room.description ?: "")
      }
      Spacer { size = SpacerPropsSize.SMALL }
      Typography {
        variant = TypographyVariant.body1
        +(props.room.question ?: "")
      }
    }
    if (props.children != null) CardActions {
      +props.children
    }
  }
}