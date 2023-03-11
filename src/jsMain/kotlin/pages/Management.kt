package pages

import api.RoomManagementApi
import com.fynnian.application.common.I18n
import com.fynnian.application.common.room.RoomManagementDetail
import com.fynnian.application.common.room.RoomStatus
import components.*
import csstype.Display
import csstype.JustifyContent
import csstype.rem
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.material.Box
import mui.material.MenuItem
import mui.material.Select
import mui.material.SelectVariant
import mui.system.sx
import react.*
import react.dom.events.ChangeEvent
import web.html.HTMLInputElement

private val scope = MainScope()

val Management = FC<Props> {

  val api = RoomManagementApi()
  val (language) = useContext(LanguageContext)
  val (rooms, setRoom) = useState<List<RoomManagementDetail>>(emptyList())
  val (loading, setLoading) = useState(true)
  val (statusFilter, setStatusFilter) = useState<RoomStatus?>(null)

  useEffect {
    scope.launch {
      if (loading) {
        setRoom(api.getRooms(statusFilter))
        setLoading(false)
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
      Box {
        sx {
          padding = 0.5.rem
          display = Display.flex
          justifyContent = JustifyContent.flexEnd
        }
        Select {
          variant = SelectVariant.standard
          value = statusFilter ?: "ALL"
          onChange = { event: ChangeEvent<HTMLInputElement>, _ ->
            if(event.target.value == "ALL") setStatusFilter(null)
            else setStatusFilter(RoomStatus.valueOf(event.target.value))
            setLoading(true)
          }
          MenuItem {
            value = "ALL"
            +I18n.get(language, I18n.TranslationKey.ROOM_STATUS_ALL)
          }
          RoomStatus.values().map {
            MenuItem {
              value = it.toString()
              +I18n.get(language, I18n.TranslationKey.valueOf("ROOM_STATUS_$it"))
            }
          }

        }
      }
      RoomManagementList {
        this.rooms = rooms
      }
    }
  }
}