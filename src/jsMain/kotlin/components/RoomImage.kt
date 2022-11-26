package components

import com.fynnian.application.common.room.Answer
import com.fynnian.application.common.room.Coordinates
import com.fynnian.application.common.room.RoomImage
import csstype.NamedColor
import csstype.Position
import csstype.pct
import csstype.px
import dom.html.HTMLImageElement
import emotion.react.css
import mui.icons.material.Warning
import mui.material.Box
import mui.material.Icon
import mui.system.sx
import react.FC
import react.Props
import react.PropsWithChildren
import react.dom.events.MouseEventHandler
import react.dom.html.ReactHTML.img

external interface RoomImageProps : PropsWithChildren {
  var image: RoomImage
  var onImageClick: MouseEventHandler<HTMLImageElement>
  var answers: List<Answer>
}

val RoomImage = FC<RoomImageProps> { props ->

  Box {
    img {
      id = "room-image-${props.image.id}"
      src = props.image.url
      alt = props.image.title
      onClick = props.onImageClick
      css {
        width = 100.pct
      }
    }
    props.answers.forEach {
      ImageMarker {
        coordinates = it.coordinates
      }
    }
    +props.children
  }
}

external interface ImageMarkerProps : Props {
  var coordinates: Coordinates
}

val ImageMarker = FC<ImageMarkerProps> { props ->
  Icon {
    sx {
      position = Position.absolute
      top = (props.coordinates.vertical - 12.5).px
      left = (props.coordinates.horizontal - 12.5).px
    }
    Warning {
      sx {
        color = NamedColor.black
      }
    }
  }
}