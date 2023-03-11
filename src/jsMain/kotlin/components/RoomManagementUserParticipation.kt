package components

import com.fynnian.application.common.I18n
import csstype.rem
import mui.material.Card
import mui.material.CardContent
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.useContext

external interface RoomManagementUserParticipationProps : Props {
  var participants: Int
  var answers: Int
}

val RoomManagementUserParticipation = FC<RoomManagementUserParticipationProps> { props ->

  val (language) = useContext(LanguageContext)

  Card {
    sx {
      padding = 0.5.rem
    }
    CardContent {
      Typography {
        variant = TypographyVariant.body1
        +I18n.get(
          language,
          I18n.TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_LABEL_PARTICIPANTS,
          I18n.TemplateProperty.Participants(props.participants)
        )
      }
      Typography {
        variant = TypographyVariant.body1
        +I18n.get(
          language,
          I18n.TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_LABEL_TOTAL_ANSWERS,
          I18n.TemplateProperty.Answers(props.answers)
        )
      }
    }
  }
}