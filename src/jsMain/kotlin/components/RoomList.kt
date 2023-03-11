package components

import com.fynnian.application.common.I18n
import com.fynnian.application.common.room.RoomManagementDetail
import com.fynnian.application.common.room.RoomStatus
import csstype.*
import mui.material.*
import mui.material.Size
import mui.material.styles.TypographyVariant
import mui.system.Breakpoint
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import react.router.useNavigate
import react.useContext

external interface RoomListProps : Props {
  var rooms: List<RoomManagementDetail>
}

val RoomList = FC<RoomListProps> { props ->
  List {
    props.rooms.map {
      RoomListItem {
        room = it
      }
    }
  }
}

external interface RoomListItemProp : Props {
  var room: RoomManagementDetail
}

val RoomListItem = FC<RoomListItemProp> { props ->
  val room = props.room

  ListItem {
    sx {
      gap = 0.2.rem
      flexWrap = responsive(
        Breakpoint.md to FlexWrap.nowrap,
        Breakpoint.xs to FlexWrap.wrap
      )
    }
    divider = true

    ListItemText {
      sx {
        width = responsive(
          Breakpoint.md to Auto.auto,
          Breakpoint.xs to 100.pct
        )
      }
      primary = ReactNode(room.title)
      secondary = ReactNode(room.code)
    }

    RoomStatusBadge {
      status = room.roomStatus
    }
    ToRoomManagementDetail {
      code = room.code
    }
    ToRoom {
      code = room.code
    }
    RoomQRCodeDialog {
      roomCode = room.code
    }
  }
}