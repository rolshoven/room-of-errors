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
  var participantsWithoutAnswers: Int
  var answers: Int
  var withGroupInformation: Boolean
  var groups: Int?
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
          I18n.TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_LABEL_WITH_GROUP_INFORMATION,
          I18n.TemplateProperty.BooleanParam(props.withGroupInformation, language)
        )
      }
      if (props.withGroupInformation) {
        Typography {
          variant = TypographyVariant.body1
          +I18n.get(
            language,
            I18n.TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_LABEL_GROUPS,
            I18n.TemplateProperty.Number(props.groups ?: 0)
          )
        }
      }
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
          I18n.TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_LABEL_PARTICIPANTS_WITHOUT_ANSWERS,
          I18n.TemplateProperty.Participants(props.participantsWithoutAnswers)
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