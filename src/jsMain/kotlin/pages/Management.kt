package pages

import api.RoomManagementApi
import com.fynnian.application.common.room.RoomManagementDetail
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
  var rooms by useState<List<RoomManagementDetail>>(mutableListOf())
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
        CreateRoomDialog()
      }
      RoomManagementList {
        this.rooms = rooms
      }
    }
  }
}