package pages


import components.MainContainer
import components.RoomNavigator
import react.FC
import react.Props

external interface LandingpageProps : Props {

}

val Landingpage = FC<LandingpageProps> {
  MainContainer {
    RoomNavigator {
      title = "welcome to the room of horrors"
    }
  }
}