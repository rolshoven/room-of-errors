package components

import api.RoomApi
import com.fynnian.application.common.I18n
import com.fynnian.application.common.room.Answer
import com.fynnian.application.common.room.RoomImage
import csstype.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.icons.material.Clear
import mui.icons.material.Delete
import mui.icons.material.Edit
import mui.icons.material.Save
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.dom.onChange
import react.useContext
import react.useState
import web.html.HTMLTextAreaElement
import web.html.InputType

private val scope = MainScope()

external interface AnswerListProps : Props {
  var images: List<RoomImage>
  var answers: List<Answer>
  var reloadAnswers: () -> Unit
  var readOnly: Boolean?
  var totalAnswers: Int?
}

val AnswerList = FC<AnswerListProps> { props ->
  val (language) = useContext(LanguageContext)

  Box {
    sx {
      padding = 0.5.rem
    }
    Typography {
      variant = TypographyVariant.body1
      +I18n.get(
        language,
        I18n.TranslationKey.ROOM_ANSWER_ANSWERS_TOTAL,
        I18n.TemplateProperty.Answers(props.totalAnswers ?: props.answers.size)
      )
    }
    if (props.images.size > 1 && props.readOnly != true) props.images.mapIndexed { i, image ->
      Typography {
        variant = TypographyVariant.body1
        +I18n.get(
          language,
          I18n.TranslationKey.ROOM_ANSWER_ANSWERS_COUNT_PER_IMAGE,
          I18n.TemplateProperty.Number(i + 1),
          I18n.TemplateProperty.Answers(props.answers.filter { it.imageId == image.id }.size)
        )
      }
    }
  }
  List {
    props.answers.map {
      AnswerListItem {
        answer = it
        reloadAnswers = props.reloadAnswers
        readOnly = props.readOnly ?: false
      }
    }
  }
}

external interface AnswerListItemProps : Props {
  var answer: Answer
  var reloadAnswers: () -> Unit
  var readOnly: Boolean
}

val AnswerListItem = FC<AnswerListItemProps> { props ->
  val api = RoomApi()
  val answer = props.answer
  var edit by useState(false)
  var editedAnswer by useState(answer.answer)

  ListItem {
    sx {
      flexDirection = FlexDirection.row
      gap = 1.rem
    }

    Typography {
      variant = TypographyVariant.body1
      + answer.no.toString()
    }

    if (edit) {
      TextField {
        id = "edit-answer"
        name = "edit-answer"
        type = InputType.text
        multiline = true
        value = editedAnswer
        onChange = {
          val e = it.target as HTMLTextAreaElement
          editedAnswer = e.value
        }
        sx {
          flexGrow = number(2.0)
        }
      }
      IconButton {
        color = IconButtonColor.primary
        onClick = {
          editedAnswer = answer.answer
          edit = false
        }
        Clear()
      }
      IconButton {
        color = IconButtonColor.primary
        onClick = {
          scope.launch {
            api.upsertAnswer(
              answer.roomCode,
              answer.copy(answer = editedAnswer)
            )
            edit = false
            props.reloadAnswers()
          }
        }
        Save()
      }

    } else {
      onMouseOver = {
        answer.getMarker()?.sx?.color = NamedColor.blue
      }
      onMouseLeave = {
        answer.getMarker()?.sx?.color = NamedColor.black
      }

      Typography {
        variant = TypographyVariant.body1
        sx {
          flexGrow = number(2.0)
        }
        +answer.answer
      }
      if (!props.readOnly){
        IconButton {
          color = IconButtonColor.primary
          onClick = { edit = true }
          Edit()
        }
        IconButton {
          color = IconButtonColor.primary
          onClick = {
            scope.launch {
              api.deleteAnswer(answer.roomCode, answer.id)
              props.reloadAnswers()
            }
          }
          Delete()
        }
      }
    }
  }
}