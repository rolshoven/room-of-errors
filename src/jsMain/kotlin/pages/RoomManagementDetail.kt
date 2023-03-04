package pages

import api.RoomManagementApi
import com.benasher44.uuid.Uuid
import com.fynnian.application.common.room.Room
import com.fynnian.application.common.room.RoomImage
import components.*
import csstype.Display
import csstype.FlexDirection
import csstype.rem
import js.core.get
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.icons.material.Delete
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.*
import react.dom.html.ReactHTML.img
import react.router.useNavigate
import react.router.useParams

private val scope = MainScope()

val RoomManagementDetail = FC<Props> {

  val api = RoomManagementApi()
  val (language) = useContext(LanguageContext)

  val roomCodeParam = useParams()["id"] ?: ""
  val (roomCode, setRoomCode) = useState("")
  val (room, setRoom) = useState<Room>()
  val (images, setImages) = useState<List<RoomImage>>(emptyList())
  val (loading, setLoading) = useState(true)

  useEffect {
    if (roomCodeParam != roomCode) {
      setRoomCode(roomCodeParam)
      setLoading(true)
    }
  }

  useEffect {
    scope.launch {
      if (loading && roomCode.isNotBlank()) {
        api.getRoom(roomCode).let {
          setRoom(it)
          setImages(it?.images ?: emptyList())
          setLoading(false)
        }
      }
    }
  }

  val reloadImages: () -> Unit = { scope.launch { setImages(api.getRoomImages(roomCode)) } }

  fun deleteImage(code: String, imageId: Uuid) {
    scope.launch {
      api.deleteRoomImage(code, imageId)?.let { error -> console.log(error) } ?: reloadImages()
    }
  }

  MainContainer {
    if (loading) {
      LoadingSpinner()
    } else if (room == null) {
      Box { Typography { variant = TypographyVariant.body1; +"not found" } }
    } else {
      Spacer {
        size = SpacerPropsSize.SMALL
      }
      ToManagementPage()
      RoomInfo {
        this.room = room
      }
      Box {
        Box {
          sx {
            display = Display.flex
            flexDirection = FlexDirection.row
          }
          if (room.images.isEmpty()) Typography {
            +"No Images yet"
          }
          images.map { image ->
            Card {
              sx {
                maxWidth = 10.rem
              }
              CardMedia {
                component = img
                src = image.url
              }
              CardContent {
                Typography {
                  variant = TypographyVariant.body1
                  +image.title
                }
              }
              CardActions {
                IconButton {
                  Delete()
                  color = IconButtonColor.primary
                  onClick = { deleteImage(room.code, image.id) }
                }
              }
            }
          }
        }
        CreateRoomImageDialog {
          this.code = room.code
          this.reloadImages = reloadImages
        }
      }
      ListItem {
        sx {
          flexDirection = FlexDirection.row
          gap = 1.rem
        }
        Typography {
          variant = TypographyVariant.body1
          +room.code
        }
        Typography {
          variant = TypographyVariant.body1
          +room.title
        }
        ToRoom {
          code = room.code
        }
        RoomQRCodeDialog {
          this.roomCode = room!!.code
        }
      }
    }
  }
}