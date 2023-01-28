package pages

import api.RoomManagementApi
import com.fynnian.application.common.room.RoomDetails
import components.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.material.Box
import react.FC
import react.Props
import react.useEffect
import react.useState

private val scope = MainScope()

val Management = FC<Props> {

  val api = RoomManagementApi()
  var rooms by useState<List<RoomDetails>>(mutableListOf())
  var loading by useState(true)

  useEffect {
    scope.launch {
      if (loading) {
        rooms = api.getRooms()
        loading = false
      }
    }
  }

  val reloadRooms = {
    scope.launch {
      rooms = api.getRooms()
    }
    Unit
  }

  MainContainer {
    if (loading) {
      LoadingSpinner { }
    } else {
      Spacer {
        size = SpacerPropsSize.SMALL
      }
      Box {
        CreateRoomDialog {
          this.reloadRooms = reloadRooms
        }
      }
      RoomList {
        this.rooms = rooms
      }
    }
  }
}