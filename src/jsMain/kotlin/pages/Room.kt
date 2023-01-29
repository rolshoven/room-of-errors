package pages

import api.RoomApi
import com.benasher44.uuid.uuid4
import com.fynnian.application.common.room.Answer
import com.fynnian.application.common.room.Coordinates
import com.fynnian.application.common.room.Room
import components.*
import csstype.*
import js.core.get
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.icons.material.Clear
import mui.icons.material.Save
import mui.material.*
import mui.material.Size
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.*
import react.dom.events.MouseEvent
import react.dom.onChange
import react.router.useParams
import web.html.HTMLImageElement
import web.html.HTMLTextAreaElement
import web.html.InputType

private val scope = MainScope()

val RoomPage = FC<Props> {

  val api = RoomApi()

  val roomCodeParam = useParams()["id"] ?: ""
  val (roomCode, setRoomCode) = useState("")
  val (room, setRoom) = useState<Room>()
  val (loading, setLoading) = useState(true)
  val user = useContext(UserContext)
  val (cord, setCord) = useState<Coordinates>()
  var answers by useState<List<Answer>>(mutableListOf())
  val (currentAnswer, setCurrentAnswer) = useState("")

  // workaround for the missing router support in the wrapper
  // check the stored code against the param to trigger rerender
  // when the navigation is made with RoomNavigator in this component
  useEffect {
    if (roomCodeParam != roomCode) {
      setRoomCode(roomCodeParam)
      setLoading(true)
    }
  }

  useEffect {
    scope.launch {
      if (loading && roomCode.isNotBlank() && user != null) {
        api.getRoom(roomCode).let {
          setRoom(it)
          setLoading(false)
        }
        answers = api.getAnswers(roomCode, user)
      }
    }
  }

  val reloadAnswers = {
    scope.launch {
      answers = api.getAnswers(roomCode, user!!)
    }
    Unit
  }

  fun addAnswer(answer: Answer) {
    scope.launch {
      api.upsertAnswer(roomCode, answer)
      reloadAnswers()
    }
  }

  fun calculateCoordinates(event: MouseEvent<HTMLImageElement, *>): Coordinates {
    val image = event.target as HTMLImageElement
    // get te image position, the offset to add or subtract
    val offsetX = image.getBoundingClientRect().left
    val offsetY = image.getBoundingClientRect().top

    // calculate the relational percent of the X and Y positions so that we can handle different resolutions
    return Coordinates(
      (event.clientX - offsetX) / image.width * 100,
      (event.clientY - offsetY) / image.height * 100
    )
  }

  MainContainer {
    if (loading) {
      LoadingSpinner { }
    } else {
      if (room == null) {
        RoomNavigator {
          title = "There is no room with code $roomCodeParam "
          preNavigation = { setLoading(true) }
        }
      } else {
        Spacer {
          size = SpacerPropsSize.SMALL
        }
        RoomInfo {
          this.room = room
        }
        Spacer {
          size = SpacerPropsSize.SMALL
        }
        RoomImage {
          image = room.images.first()
          onImageClick = { event -> setCord(calculateCoordinates(event)) }

          answers.map {
            ImageMarker {
              id = it.id
              coordinates = it.coordinates
            }
          }
          cord?.let {
            ImageMarker {
              coordinates = it
              selected = true
            }
          }
        }
        Box {
          sx {
            padding = 0.5.rem
          }
          Typography {
            variant = TypographyVariant.body1
            +  "mark a spot with an error with a click on the image"
          }
        }
        Box {
          sx {
            padding = 0.5.rem
            display = Display.flex
            alignItems = AlignItems.center
          }
          TextField {
            id = "answer"
            name = "answer"
            type = InputType.text
            placeholder = if (cord == null) "mark a spot with an error" else "your answer"
            multiline = true
            minRows = 2
            fullWidth = true
            disabled = cord == null
            value = currentAnswer
            onChange = {
              val e = it.target as HTMLTextAreaElement
              setCurrentAnswer(e.value)
            }
          }
          IconButton {
            Save()
            size = Size.medium
            color = IconButtonColor.primary
            disabled = currentAnswer.isBlank()
            onClick = {
              addAnswer(
                Answer(
                  id = uuid4(),
                  no = answers.maxOfOrNull { it.no }?.plus(1) ?: 1,
                  imageId = room.images.first().id,
                  userId = user!!.id,
                  roomCode = roomCode,
                  coordinates = cord ?: Coordinates(0.0, 0.0),
                  answer = currentAnswer
                )
              )
              setCurrentAnswer("")
              setCord(null)
            }
          }
          IconButton {
            Clear()
            color = IconButtonColor.primary
            disabled = cord == null
            onClick = {
              setCurrentAnswer("")
              setCord(null)
            }
          }
        }
        AnswerList {
          this.answers = answers
          this.reloadAnswers = reloadAnswers
        }
      }
    }
  }
}