package pages

import api.RoomApi
import com.benasher44.uuid.uuid4
import com.fynnian.application.common.room.Answer
import com.fynnian.application.common.room.Coordinates
import com.fynnian.application.common.room.Room
import components.*
import csstype.*
import dom.html.HTMLTextAreaElement
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.js.get
import mui.icons.material.Save
import mui.material.*
import mui.material.Size
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.*
import react.dom.html.InputType
import react.dom.onChange
import react.router.useParams

private val scope = MainScope()

val RoomPage = FC<Props> {

  val api = RoomApi()

  val roomCodeParam = useParams()["id"] ?: ""
  val (roomCode, setRoomCode) = useState("")
  val (room, setRoom) = useState<Room>()
  val (loading, setLoading) = useState(true)
  val user = useContext(UserContext)
  val (cord, setCord) = useState<Coordinates>()
  val (answers, setAnswers) = useState<MutableList<Answer>>(mutableListOf())
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
      if (loading && roomCode.isNotBlank()) {
        api.getRoom(roomCode).let {
          setRoom(it)
          setLoading(false)
        }
      }
    }
  }

  fun addAnswer(answer: Answer) {
    setAnswers { it.add(answer); it }
  }

  MainContainer {
    if (loading) {
      Box {
        CircularProgress { }
      }
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
          onImageClick = { setCord(Coordinates(it.clientX, it.clientY)) }
          this.answers = answers

          cord?.let {
            ImageMarker {
              coordinates = it
            }
            Box {
              sx {
                position = Position.absolute
                top = it.vertical.px
                left = it.horizontal.px
                overflow = Overflow.hidden
                after {
                  content = Content()
                  position = Position.absolute
                  top = 0.px
                  margin = (-10).px
                  width = 20.px
                  height = 20.px
                  transform = rotate(45.deg)
                  backgroundColor = NamedColor.white
                }
              }
              Paper {
                //elevation = 1
                variant = PaperVariant.outlined
                sx {
                  padding = 0.5.rem
                }
                TextField {
                  id = "answer"
                  name = "answer"
                  type = InputType.text
                  placeholder = "your anwser"
                  multiline = true
                  value = currentAnswer
                  onChange = {
                    val e = it.target as HTMLTextAreaElement
                    setCurrentAnswer(e.value)
                  }

                }
                IconButton {
                  size = Size.medium
                  disabled = currentAnswer.isBlank()
                  onClick = {
                    addAnswer(
                      Answer(
                        id = uuid4(),
                        no = answers.size + 1,
                        imageId = uuid4(),
                        userId = uuid4(),
                        roomCode = roomCode,
                        coordinates = cord,
                        answer = currentAnswer
                      )
                    )
                    setCurrentAnswer("")
                    setCord(null)
                  }
                  Save {}
                }
              }
            }
          }
        }
        Typography {
          variant = TypographyVariant.body1
          +(cord?.let { "clicked ${cord.horizontal} | ${cord.vertical}" } ?: "click on the image")
        }
        answers.map {
          Typography {
            variant = TypographyVariant.body1
            +"${it.coordinates} - ${it.answer}"
          }
        }
      }
    }
  }
}