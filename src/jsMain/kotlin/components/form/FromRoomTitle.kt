package components.form

import com.fynnian.application.common.I18n
import com.fynnian.application.common.Language
import com.fynnian.application.common.room.Room
import mui.material.FormGroup
import mui.material.TextField
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.ReactNode
import react.StateSetter
import react.dom.onChange
import web.html.InputType

external interface FromRoomTitleProps : Props {
  var language: Language
  var title: String
  var setTitle: StateSetter<String>
}

val FromRoomTitle = FC<FromRoomTitleProps> {props ->
  FormGroup {
    TextField {
      id = "title"
      name = "title"
      type = InputType.text
      required = true
      label = ReactNode(I18n.get(props.language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_TITLE_LABEL))
      placeholder = "my room"
      helperText = ReactNode("${props.title.length} / ${Room.titleMaxChars}")
      error = (props.title.length > Room.titleMaxChars) || props.title.isBlank()
      value = props.title
      onChange = { props.setTitle(it.target.unsafeCast<HTMLInputElement>().value) }
    }
  }
}