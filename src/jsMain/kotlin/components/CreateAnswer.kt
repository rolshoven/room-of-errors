package components

import com.fynnian.application.common.I18n
import com.fynnian.application.common.room.Coordinates
import csstype.AlignItems
import csstype.Display
import csstype.rem
import mui.icons.material.Clear
import mui.icons.material.Save
import mui.material.*
import mui.system.sx
import react.*
import react.dom.onChange
import web.html.HTMLDivElement
import web.html.HTMLTextAreaElement
import web.html.InputType

external interface CreateAnswerProps : Props {
  var currentCoordinates: Coordinates?
  var currentAnswerCount: Int
  var createAnswer: (answer: String) -> Unit
  var resetCoordinates: () -> Unit
  var inputFiledRef: RefObject<HTMLDivElement>
}

val CreateAnswer = FC<CreateAnswerProps> { props ->
  val (language) = useContext(LanguageContext)

  var currentAnswer by useState("")

  Box {
    sx {
      padding = 0.5.rem
      display = Display.flex
      alignItems = AlignItems.center
    }
    TextField {
      inputRef = props.inputFiledRef
      id = "answer"
      name = "answer"
      type = InputType.text
      placeholder =
        if (props.currentCoordinates == null) I18n.get(language, I18n.TranslationKey.ROOM_ANSWER_INPUT_PLACEHOLDER_DISABLED)
        else I18n.get(language, I18n.TranslationKey.ROOM_ANSWER_INPUT_PLACEHOLDER)
      multiline = true
      minRows = 2
      fullWidth = true
      // disabled = props.currentCoordinates == null - commented the check as the disabled field doesn't get correctly focused on a state change
      value = currentAnswer
      onChange = {
        val e = it.target as HTMLTextAreaElement
        currentAnswer = e.value
      }
    }
    IconButton {
      Save()
      size = Size.medium
      color = IconButtonColor.primary
      disabled = (currentAnswer.isBlank() && props.currentCoordinates == null) || currentAnswer.isBlank()
      onClick = {
        props.createAnswer(currentAnswer)
        currentAnswer = ""
      }
    }
    IconButton {
      Clear()
      color = IconButtonColor.primary
      disabled = props.currentCoordinates == null || currentAnswer.isBlank()
      onClick = {
        currentAnswer = ""
        props.resetCoordinates()
      }
    }
  }
}