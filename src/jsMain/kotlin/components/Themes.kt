package components

import js.core.jso
import mui.material.*
import mui.material.PaletteMode.dark
import mui.material.PaletteMode.light
import mui.material.styles.Theme
import mui.material.styles.ThemeProvider
import mui.material.styles.createTheme
import react.*

object Themes {
  val Light = createTheme(
    jso {
      palette = jso { mode = light }
    }
  )
  val Dark = createTheme(
    jso {
      palette = jso { mode = dark }
    }
  )
}

typealias ThemeState = StateInstance<Theme>

val ThemeContext = createContext<ThemeState>()

val ThemeModule = FC<PropsWithChildren> { props ->
  val state = useState(Themes.Dark)
  val (theme) = state

  ThemeContext(state) {
    ThemeProvider {
      this.theme = theme

      CssBaseline()
      + props.children
    }
  }
}

val ThemeSwitch = FC<Props> {
  val (theme, setTheme) = useContext(ThemeContext)

  FormControl {
    variant = FormControlVariant.standard
    FormControlLabel {
      label = ReactNode(theme.palette.mode.toString())
      control = Switch.create() {
        name = "theme"
        checked = theme == Themes.Dark
        onChange = { _, _ ->
          if (theme == Themes.Dark) setTheme(Themes.Light)
          else setTheme(Themes.Dark)
        }
      }
    }
  }
}