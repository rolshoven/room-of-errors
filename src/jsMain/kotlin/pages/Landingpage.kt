package pages


import com.fynnian.application.common.AppPaths
import csstype.TextAlign
import mui.material.*
import mui.system.responsive
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.ReactNode
import react.dom.html.InputType
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.main
import react.dom.onChange
import react.router.useNavigate
import react.useState

external interface LandingpageProps : Props {

}

val Landingpage = FC<LandingpageProps> {
  val (roomId, setRoomId) = useState("")
  val navigate = useNavigate()

  Container {
    component = main
    maxWidth = "xs"
    CssBaseline
    Box {
      Stack {
        spacing = responsive(1)

        Typography {
          component = h2
          sx {
            textAlign = TextAlign.center
          }
          + "welcome to the room of horrors"
        }
        TextField {
          id = "roomId"
          name = "roomId"
          type = InputType.text
          label = ReactNode("please enter a room code")
          placeholder = "room code"
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
            console.log(roomId)
            navigate(AppPaths.ROOM.path + roomId)
          }
          +"Go to room"
        }
      }
    }
  }
}