package pages

import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.p

val Management = FC<Props> {
  div {
    p {
      + "Management"
    }
  }
}