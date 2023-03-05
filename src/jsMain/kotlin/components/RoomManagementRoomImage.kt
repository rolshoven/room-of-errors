package components

import com.fynnian.application.common.room.RoomImage
import csstype.px
import js.core.jso
import mui.icons.material.Delete
import mui.material.*
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import react.createElement
import react.dom.events.MouseEventHandler
import react.dom.html.ReactHTML

external interface RoomManagementRoomImageProps : Props {
  var deleteImageAction: MouseEventHandler<*>
  var image: RoomImage
}

val RoomManagementRoomImage = FC<RoomManagementRoomImageProps> { props ->

  Card {
    sx {
      width = 250.px
    }
    CardHeader {
      title = ReactNode(props.image.title)
      action = createElement(
        IconButton,
        jso {
          color = IconButtonColor.primary
          onClick = props.deleteImageAction
        },
        createElement(Delete)
      )
    }
    CardMedia {
      sx {
        height = 250.px
      }
      component = ReactHTML.img
      src = props.image.url
    }
  }
}