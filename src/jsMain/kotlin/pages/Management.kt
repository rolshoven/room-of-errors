package pages

import components.MainContainer
import react.FC
import react.Props
import react.dom.html.ReactHTML.p

val Management = FC<Props> {
  MainContainer {
    p {
      + "Management"
    }
  }
}