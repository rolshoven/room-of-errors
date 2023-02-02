package pages


import com.fynnian.application.common.I18n
import components.LanguageContext
import components.MainContainer
import components.RoomNavigator
import react.FC
import react.Props
import react.useContext

external interface LandingpageProps : Props {

}

val Landingpage = FC<LandingpageProps> {
  val (language) = useContext(LanguageContext)
  MainContainer {
    RoomNavigator {
      title = I18n.get(language, I18n.TranslationKey.ROOM_NAVIGATOR_TITLE_WELCOME)
    }
  }
}