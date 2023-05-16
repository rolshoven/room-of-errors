package components.form

import com.fynnian.application.common.I18n
import com.fynnian.application.common.Language
import mui.material.FormControlLabel
import mui.material.FormGroup
import mui.material.FormHelperText
import mui.material.Switch
import react.FC
import react.Props
import react.ReactNode
import react.create
import react.dom.events.ChangeEvent
import web.html.HTMLInputElement

external interface FormSwitchElementProps : Props {
  var language: Language
  var labelKey: I18n.TranslationKey
  var helpTextKey: I18n.TranslationKey
  var fieldName: String
  var value: Boolean
  var onChange: (event: ChangeEvent<HTMLInputElement>, checked: Boolean) -> Unit
}

val FormSwitchElement = FC<FormSwitchElementProps> { props ->
  FormGroup {
    FormControlLabel {
      label = ReactNode(I18n.get(props.language, props.labelKey))
      control = Switch.create {
        name = props.fieldName
        checked = props.value
        onChange = props.onChange
      }
    }
    FormHelperText {
      +I18n.get(props.language, props.helpTextKey)
    }
  }
}