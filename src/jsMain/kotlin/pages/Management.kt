package pages

import api.RoomManagementApi
import com.fynnian.application.common.room.Room
import components.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.material.Box
import mui.material.Button
import mui.material.ButtonVariant
import react.FC
import react.Props
import react.useEffect
import react.useState

private val scope = MainScope()

val Management = FC<Props> {

  val api = RoomManagementApi()
  var rooms by useState<List<Room>>(mutableListOf())
  var loading by useState(true)

  useEffect {
    scope.launch {
      if (loading) {
        rooms = api.getRooms()
        loading = false
      }
    }
  }

  MainContainer {
    if (loading) {
      LoadingSpinner { }
    } else {
      Spacer {
        size = SpacerPropsSize.SMALL
      }
      Box {
        CreateRoomDialog { }
      }
      RoomList {
        this.rooms = rooms
      }
    }
  }
}