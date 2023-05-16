package pages

import api.RoomApi
import api.UserApi
import com.benasher44.uuid.uuid4
import com.fynnian.application.common.I18n
import com.fynnian.application.common.room.*
import com.fynnian.application.common.user.User
import components.*
import csstype.pct
import csstype.rem
import js.core.get
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import mui.icons.material.KeyboardArrowLeft
import mui.icons.material.KeyboardArrowRight
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.*
import react.dom.events.MouseEvent
import react.router.useNavigate
import react.router.useParams
import web.html.HTMLDivElement
import web.html.HTMLImageElement
import web.html.HTMLVideoElement

private val scope = MainScope()

val RoomPage = FC<Props> {

  val roomApi = RoomApi()
  val userApi = UserApi()

  val (user, setUser) = useContext(UserContext)
  val (language) = useContext(LanguageContext)
  val navigate = useNavigate()

  val roomCodeParam = useParams()["id"] ?: ""
  val (roomCode, setRoomCode) = useState("")
  val (room, setRoom) = useState<Room>()
  val (loading, setLoading) = useState(true)
  var cord by useState<Coordinates>()
  var answers by useState<List<Answer>>(mutableListOf())
  var usersRoomStatus by useState<UsersRoomStatus>()
  val (groupInfo, setGroupInfo) = useState<RoomGroupInformation>()
  val (currentImage, setCurrentImage) = useState(0)

  val createAnswerRef = createRef<HTMLDivElement>()
  val outroVideoRef = createRef<HTMLVideoElement>()

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
        roomApi.getRoom(roomCode).let {
          setRoom(it)
          setLoading(false)
        }
        usersRoomStatus = roomApi.getUsersRoomStatus(roomCode, user)
        answers = roomApi.getAnswers(roomCode, user)
      }
    }
  }

  val reloadAnswers: () -> Unit = {
    scope.launch {
      answers = roomApi.getAnswers(roomCode, user!!)
    }
  }

  val (roomResetTimeLimit, setRoomResetTimeLimit) = useState(300L)
  useEffect {
    if (outroVideoRef.current != null) {
      outroVideoRef.current!!.onloadeddata = { _ ->
        // use video plus 5 min for the reset
        setRoomResetTimeLimit(outroVideoRef.current!!.duration.toLong().plus(300L))
      }
    }
  }

  fun addAnswer(answer: String) {
    scope.launch {
      roomApi.upsertAnswer(
        roomCode,
        Answer(
          id = uuid4(),
          no = answers.maxOfOrNull { it.no }?.plus(1) ?: 1,
          imageId = room!!.images[currentImage].id,
          userId = user!!.id,
          roomCode = roomCode,
          coordinates = cord ?: Coordinates(0.0, 0.0),
          answer = answer
        )
      )
      cord = null
      answers = roomApi.getAnswers(roomCode, user)
    }
  }

  fun startRoom() { scope.launch { usersRoomStatus = roomApi.startRoom(roomCode, user!!) } }
  fun finishRoom() { scope.launch { usersRoomStatus = roomApi.finishRoom(roomCode, user!!) } }
  fun closeRoom() { scope.launch { usersRoomStatus = roomApi.closeRoom(roomCode, user!!) } }
  fun saveGroupInfo(name: String, size: Int) {
    scope.launch {
      setGroupInfo(roomApi.saveRoomGroupInformation(RoomGroupInformation(user!!.id, roomCode, size, name)))
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
            I18n.TemplateProperty.RoomCode(roomCodeParam)
          )
          preNavigation = { setLoading(true) }
        }
      } else if (room.roomStatus == RoomStatus.NOT_READY || room.roomStatus == RoomStatus.CLOSED) {
        Spacer {
          size = SpacerPropsSize.MEDIUM
        }
        RoomUnavailableInfo {
          title = room.title
          code = room.code
          status = room.roomStatus
        }
      } else {
        Spacer {
          size = SpacerPropsSize.SMALL
        }
        RoomInfo {
          this.room = room
          this.groupInfo = groupInfo
          this.userState = usersRoomStatus?.participationStatus
        }
        when (usersRoomStatus?.participationStatus) {
          UsersRoomParticipationStatus.NOT_STARTED -> {
            Spacer {
              size = SpacerPropsSize.SMALL
            }
            if (room.withGroupInformation && groupInfo == null) RoomGroupInfo {
              text = room.withGroupInformationText
              onGroupInfoSubmit = ::saveGroupInfo
            } else RoomInteractionInfo {
              type = RoomStatementVariant.INTRO
              interactionInfo = room.intro
              Button {
                sx {
                  width = 100.pct
                }
                +I18n.get(language, I18n.TranslationKey.ROOM_INTRO_START_BUTTON)
                onClick = { startRoom() }
              }
            }
          }

          UsersRoomParticipationStatus.FINISHED -> {
            Spacer {
              size = SpacerPropsSize.SMALL
            }
            RoomInteractionInfo {
              type = RoomStatementVariant.OUTRO
              videoRef = outroVideoRef
              interactionInfo = room.outro
              if (room.singleDeviceRoom) {
                if (room.autoStartNextRoom) {
                  RoomRestartTimer {
                    timeLimitSeconds = roomResetTimeLimit
                    userFinishTime = usersRoomStatus?.finishedAt ?: Instant.parse("1970-01-01T00:00:00Z")
                    actonOnPastLimit = {
                      scope.launch {
                        userApi.upsertUser(User(setUserIdFromLocalStorage(uuid4()), null))?.let { setUser(it) }
                      }
                      navigate(0)
                    }
                  }
                  Spacer { size = SpacerPropsSize.MEDIUM }
                }
                NewUserSessionButton()
              }
            }
            AnswerList {
              this.images = room.images
              this.answers = answers
              this.reloadAnswers = reloadAnswers
              readOnly = true
            }
          }

          UsersRoomParticipationStatus.STARTED -> {
            // safety return when due to some isse there is no image present, dont brake the page
            if (room.images.isEmpty()) return@MainContainer

            val selectedImage = room.images[currentImage]
            val answersForCurrentImage = answers.filter { it.imageId == selectedImage.id }

            if (room.timeLimitMinutes != null) FloatingTimer {
              timeLimitMinutes = room.timeLimitMinutes
              // use unix epoch as fallback for the edge case when the date would not be set, is set in the app when calling the API
              userStartTime = usersRoomStatus?.startedAt ?: Instant.parse("1970-01-01T00:00:00Z")
              actonOnPastLimit = { finishRoom() }

            }

            Spacer {
              size = SpacerPropsSize.SMALL
            }
            RoomImage {
              image = selectedImage
              onImageClick = { event ->
                cord = calculateCoordinates(event)
                createAnswerRef.current?.focus()
              }

              answersForCurrentImage.map {
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
            if (room.images.size > 1) MobileStepper {
              //ToDo: some gray stiling in dark mode, maybe card?
              elevation = 2
              variant = MobileStepperVariant.dots
              steps = room.images.size
              position = MobileStepperPosition.static
              activeStep = currentImage
              nextButton = Button.create {
                size = Size.small
                onClick = { setCurrentImage(currentImage + 1) }
                disabled = currentImage == room.images.size - 1
                +I18n.get(language, I18n.TranslationKey.ROOM_IMAGE_SLIDER_NEXT)
                KeyboardArrowRight()
              }
              backButton = Button.create {
                size = Size.small
                onClick = { setCurrentImage(currentImage - 1) }
                disabled = currentImage == 0
                KeyboardArrowLeft()
                +I18n.get(language, I18n.TranslationKey.ROOM_IMAGE_SLIDER_BACK)
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
              inputFiledRef = createAnswerRef
              currentCoordinates = cord
              currentAnswerCount = answers.size
              createAnswer = ::addAnswer
              resetCoordinates = { cord = null }
            }
            RoomFinishDialog {
              finishingAction = { finishRoom() }
            }
            AnswerList {
              this.images = room.images
              this.answers = answersForCurrentImage
              this.reloadAnswers = reloadAnswers
              this.totalAnswers = answers.size
            }
          }

          else -> {}
        }
      }
    }
  }
}