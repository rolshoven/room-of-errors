package components

import com.fynnian.application.common.AppPaths
import com.fynnian.application.common.room.Room
import csstype.FlexDirection
import csstype.rem
import mui.icons.material.Launch
import mui.material.IconButton
import mui.material.ListItem
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.router.useNavigate

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
  val navigate = useNavigate()

  ListItem {
    sx {
      flexDirection = FlexDirection.row
      gap = 1.rem
    }
    Typography {
      variant = TypographyVariant.subtitle1
      + room.code
    }
    Typography {
      variant = TypographyVariant.body1
      + room.title
    }
    IconButton {
      onClick = { navigate(AppPaths.ROOM.path + "/${room.code}")}
      Launch { }
    }
  }
}