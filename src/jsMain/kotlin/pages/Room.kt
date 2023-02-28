package pages

import api.RoomApi
import com.benasher44.uuid.uuid4
import com.fynnian.application.common.I18n
import com.fynnian.application.common.room.*
import components.*
import csstype.*
import js.core.get
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.*
import react.dom.events.MouseEvent
import react.router.useParams
import web.html.HTMLImageElement

private val scope = MainScope()

val RoomPage = FC<Props> {

  val api = RoomApi()

  val (user, setUser) = useContext(UserContext)
  val (language) = useContext(LanguageContext)

  val roomCodeParam = useParams()["id"] ?: ""
  val (roomCode, setRoomCode) = useState("")
  val (room, setRoom) = useState<Room>()
  val (loading, setLoading) = useState(true)
  var cord by useState<Coordinates>()
  var answers by useState<List<Answer>>(mutableListOf())
  var usersRoomStatus by useState<UsersRoomStatus>()

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
        usersRoomStatus = api.getUsersRoomStatus(roomCode, user)
        answers = api.getAnswers(roomCode, user)
      }
    }
  }

  val reloadAnswers: () -> Unit = {
    scope.launch {
      answers = api.getAnswers(roomCode, user!!)
    }
  }

  fun addAnswer(answer: String) {
    scope.launch {
      api.upsertAnswer(
        roomCode,
        Answer(
          id = uuid4(),
          no = answers.maxOfOrNull { it.no }?.plus(1) ?: 1,
          imageId = room!!.images.first().id,
          userId = user!!.id,
          roomCode = roomCode,
          coordinates = cord ?: Coordinates(0.0, 0.0),
          answer = answer
        )
      )
      cord = null
      answers = api.getAnswers(roomCode, user)
    }
  }

  fun startRoom() {
    scope.launch {
      usersRoomStatus = api.startRoom(roomCode, user!!)
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
      LoadingSpinner()
    } else {
      if (room == null) {
        RoomNavigator {
          title = I18n.get(
            language,
            I18n.TranslationKey.ROOM_NAVIGATOR_TITLE_INVALID_CODE,
            I18n.TemplateProperty("roomCode", roomCodeParam)
          )
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
        when (usersRoomStatus?.participationStatus) {
          UsersRoomParticipationStatus.NOT_STARTED -> {
            RoomStatement {
              type = RoomStatementVariant.INTRO
              cardAction = { startRoom() }
              statement = room.startingStatements
            }
          }

          UsersRoomParticipationStatus.FINISHED -> {
            RoomStatement {
              type = RoomStatementVariant.OUTRO
              cardAction = {}
              statement = room.endingStatements
            }
          }

          else -> {
            RoomImage {
              image = room.images.first()
              onImageClick = { event -> cord = calculateCoordinates(event) }

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
                +I18n.get(language, I18n.TranslationKey.ROOM_IMAGE_HELP_TEXT)
              }
            }
            CreateAnswer {
              currentCoordinates = cord
              currentAnswerCount = answers.size
              createAnswer = ::addAnswer
              resetCoordinates = { cord = null }
            }
            AnswerList {
              this.answers = answers
              this.reloadAnswers = reloadAnswers
            }
          }
        }
      }
    }
  }
}