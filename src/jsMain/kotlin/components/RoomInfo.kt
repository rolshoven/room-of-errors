package components

import com.fynnian.application.common.I18n
import com.fynnian.application.common.room.Room
import csstype.rem
import mui.material.Box
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.useContext

external interface RoomInfoProps : Props {
  var room: Room
}

val RoomInfo = FC<RoomInfoProps> { props ->
  val (language) = useContext(LanguageContext)

  Box {
    sx {
      padding = 0.5.rem
    }
    Typography {
      variant = TypographyVariant.body1
      +I18n.get(
        language,
        I18n.TranslationKey.ROOM_INFO_LABEL_NAME,
        I18n.TemplateProperty("roomTitle", props.room.title)
      )
    }
    Typography {
      variant = TypographyVariant.body1
      +I18n.get(
        language,
        I18n.TranslationKey.ROOM_INFO_LABEL_ROOM_CODE,
        I18n.TemplateProperty("roomCode", props.room.code)
      )
    }
    Typography {
      variant = TypographyVariant.body1
      +props.room.description
    }
    Typography {
      variant = TypographyVariant.body1
      +props.room.question
    }
  }
}