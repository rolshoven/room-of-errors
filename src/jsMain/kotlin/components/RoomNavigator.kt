package components

import com.fynnian.application.common.AppPaths
import csstype.TextAlign
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.responsive
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.ReactNode
import react.dom.html.InputType
import react.dom.onChange
import react.router.useNavigate
import react.useState

external interface RoomNavigatorProps : Props {
  var title: String
  var preNavigation: () -> Unit
}

val RoomNavigator = FC<RoomNavigatorProps> { props ->
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
        + props.title
      }
      Spacer {
        size = SpacerPropsSize.SMALL
      }
      TextField {
        id = "roomId"
        name = "roomId"
        type = InputType.text
        label = ReactNode("please enter an 8 char room code")
        placeholder = "abcd1234"
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
        +"Go to room"
      }
    }
  }
}