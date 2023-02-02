package components

import com.fynnian.application.common.AppPaths
import com.fynnian.application.common.I18n
import csstype.TextAlign
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.responsive
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.onChange
import react.router.useNavigate
import web.html.InputType

external interface RoomNavigatorProps : Props {
  var title: String
  var preNavigation: () -> Unit
}

val RoomNavigator = FC<RoomNavigatorProps> { props ->
  val (language) = useContext(LanguageContext)

  val (roomId, setRoomId) = useState("")
  val navigate = useNavigate()

  Container {
    maxWidth = "xs"

    Stack {
      spacing = responsive(1)
      Spacer {
        size = SpacerPropsSize.LARGE
      }
      Typography {
        variant = TypographyVariant.body1
        sx {
          textAlign = TextAlign.center
        }
        +props.title
      }
      Spacer {
        size = SpacerPropsSize.SMALL
      }
      TextField {
        id = "roomId"
        name = "roomId"
        type = InputType.text
        label = ReactNode(I18n.get(language, I18n.TranslationKey.ROOM_NAVIGATOR_INPUT_LABEL))
        placeholder = I18n.get(language, I18n.TranslationKey.ROOM_NAVIGATOR_INPUT_PLACEHOLDER)
        value = roomId
        onChange = {
          val e = it.target as HTMLInputElement
          setRoomId(e.value)
        }
      }
      Button {
        variant = ButtonVariant.outlined
        disabled = roomId.isBlank()
        onClick = {
          navigate(AppPaths.ROOM.path + "/$roomId")
        }
        +I18n.get(language, I18n.TranslationKey.ROOM_NAVIGATOR_BUTTON)
      }
    }
  }
}