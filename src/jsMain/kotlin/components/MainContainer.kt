package components

import mui.material.Container
import mui.material.Grid
import mui.system.responsive
import react.FC
import react.PropsWithChildren
import react.dom.html.ReactHTML.main
import workarounds.md
import workarounds.xs

val MainContainer = FC<PropsWithChildren> { props ->
  Container {
    maxWidth = "lg"
    component = main
    disableGutters = true

    Navigation()

    Grid {
      container = true
      spacing = responsive(2)
      Grid {
        item = true
        md = 2
        xs = 0
      }
      Grid {
        item = true
        md = 8
        xs = 12
        + props.children
      }
      Grid {
        item = true
        md = 2
        xs = 0
      }
    }
  }
}