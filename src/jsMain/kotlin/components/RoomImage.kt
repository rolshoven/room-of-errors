package components

import browser.document
import com.benasher44.uuid.Uuid
import com.fynnian.application.common.room.Answer
import com.fynnian.application.common.room.Coordinates
import com.fynnian.application.common.room.RoomImage
import csstype.*
import dom.html.HTMLImageElement
import emotion.react.css
import mui.icons.material.Warning
import mui.material.Box
import mui.system.sx
import react.FC
import react.Props
import react.PropsWithChildren
import react.dom.events.MouseEventHandler
import react.dom.html.ReactHTML.img

external interface RoomImageProps : PropsWithChildren {
  var image: RoomImage
  var onImageClick: MouseEventHandler<HTMLImageElement>
}

val RoomImage = FC<RoomImageProps> { props ->

  Box {
    sx {
      position = Position.relative
    }
    img {
      id = props.image.id.toRoomImageId()
      src = props.image.url
      alt = props.image.title
      onClick = props.onImageClick
      css {
        width = 100.pct
      }
    }

    +props.children
  }
}

fun Answer.getMarker() = document.getElementById(id.toMarkerId())
fun Uuid.toRoomImageId() = "room-image-${this}"
fun Uuid.toMarkerId() = "marker-${this}"
external interface ImageMarkerProps : Props {
  var coordinates: Coordinates
  var id: Uuid?
}

val ImageMarker = FC<ImageMarkerProps> { props ->
  Warning {
    id = props.id?.toMarkerId() ?: "new-marker"
    sx {
      position = Position.absolute
//      top = (props.coordinates.vertical - 12.5).px
//      left = (props.coordinates.horizontal - 12.5).px
      top = props.coordinates.vertical.pct
      left = props.coordinates.horizontal.pct
      transform = translate((-50).pct, (-50).pct)
      color = NamedColor.black
      zIndex = integer(1)
    }
  }
}