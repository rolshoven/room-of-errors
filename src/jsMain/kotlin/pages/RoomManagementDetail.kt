package pages

import api.RoomManagementApi
import com.benasher44.uuid.Uuid
import com.fynnian.application.common.room.Room
import com.fynnian.application.common.room.RoomImage
import com.fynnian.application.common.room.RoomStatementVariant
import com.fynnian.application.common.room.RoomStatements
import components.*
import csstype.*
import js.core.get
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.icons.material.Delete
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.*
import react.dom.html.ReactHTML.img
import react.router.useParams

private val scope = MainScope()

val RoomManagementDetail = FC<Props> {

  val api = RoomManagementApi()
  val (language) = useContext(LanguageContext)

  val roomCodeParam = useParams()["id"] ?: ""
  val (roomCode, setRoomCode) = useState("")
  val (room, setRoom) = useState<Room>()
  val (images, setImages) = useState<List<RoomImage>>(emptyList())
  val (intro, setIntro) = useState<RoomStatements>()
  val (outro, setOutro) = useState<RoomStatements>()
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
          setIntro(it?.startingStatements)
          setOutro(it?.endingStatements)
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
      Box {
        Spacer { size = SpacerPropsSize.SMALL}
        Typography { variant = TypographyVariant.body1; + "There is no room with code $roomCode" } }
        ToManagementPage()
    // ToDo: i18n
    } else {
      Spacer {
        size = SpacerPropsSize.SMALL
      }
      RoomInfo {
        this.room = room
      }
      ToRoom {
        code = room.code
      }
      RoomQRCodeDialog {
        this.roomCode = room.code
      }
      RoomExcelExport {
        code = room.code
      }
      ToManagementPage()
      Spacer { size = SpacerPropsSize.SMALL }
      Box {
        Typography {
          variant = TypographyVariant.h5
          + "Room Intro & Outro" // ToDo: i18n
        }
        Spacer { size = SpacerPropsSize.VERY_SMALL }
        Box {
          sx {
            display = Display.flex
            flexDirection = FlexDirection.row
            flexWrap = FlexWrap.wrap
            justifyContent = JustifyContent.spaceEvenly
            alignContent = AlignContent.stretch
            gap = 1.rem
          }
          RoomManagementStatement {
            variant = RoomStatementVariant.INTRO
            code = room.code
            statement = intro!!
            setStatement = setIntro
          }
          RoomManagementStatement {
            variant = RoomStatementVariant.OUTRO
            code = room.code
            statement = outro!!
            setStatement = setOutro
          }
        }
      }
      Spacer { size = SpacerPropsSize.SMALL }
      Box {
        Typography {
          variant = TypographyVariant.h5
          + "Room Angles / Views" // ToDo: i18n
        }
        Spacer { size = SpacerPropsSize.VERY_SMALL }
        CreateRoomImageDialog {
          this.code = room.code
          this.reloadImages = reloadImages
        }
        Spacer { size = SpacerPropsSize.VERY_SMALL }
        Box {
          sx {
            display = Display.flex
            flexDirection = FlexDirection.row
            flexWrap = FlexWrap.wrap
            justifyContent = JustifyContent.left
            gap = 1.rem
          }
          if (images.isEmpty()) Typography {
            +"No Angles / Views yet" // ToDo: i18n
          }
          images.map { image ->
            RoomManagementRoomImage {
              this.image = image
              this.deleteImageAction = { deleteImage(room.code, image.id) }
            }
          }
        }
      }
    }
  }
}