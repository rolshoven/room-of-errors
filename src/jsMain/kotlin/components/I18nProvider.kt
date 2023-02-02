package components

import com.fynnian.application.common.I18n
import com.fynnian.application.common.Language
import mui.material.MenuItem
import mui.material.Select
import mui.material.SelectVariant
import react.*
import react.dom.events.ChangeEvent
import web.html.HTMLInputElement

val LanguageContext = createContext<StateInstance<Language>>()

val I18nProvider = FC<PropsWithChildren> { props ->
  val state = useState(I18n.defaultLanguage)
  LanguageContext(state) {
    LanguageContext.Provider {
      value = state
      +props.children
    }
  }
}

val LanguageSwitch = FC<Props> {
  val (language, setLanguage) = useContext(LanguageContext)
  Select {
    variant = SelectVariant.standard
    value = language
    onChange = { event: ChangeEvent<HTMLInputElement>, _ -> setLanguage(event.target.value as Language) }
    I18n.availableLanguages.map {
      MenuItem {
        value = it
        +it.toString()
      }
    }
  }
}