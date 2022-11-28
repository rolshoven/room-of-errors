package components

import com.fynnian.application.common.room.Room
import csstype.FlexDirection
import csstype.rem
import mui.material.ListItem
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props

external interface RoomListProps : Props {
  var rooms: List<Room>
}

val RoomList = FC<RoomListProps> {props ->
  mui.material.List {
    sx {

    }
    props.rooms.map {
      RoomListItem {
        room = it
      }
    }
  }
}

external interface RoomListItemProp : Props {
  var room: Room
}

val RoomListItem = FC<RoomListItemProp> { props ->
  val room = props.room
  ListItem {
    sx {
      flexDirection = FlexDirection.row
      gap = 1.rem
    }
    Typography {
      variant = TypographyVariant.caption
      + room.code
    }
    Typography {
      variant = TypographyVariant.body1
      + room.title
    }
  }
}