package pages

import api.RoomApi
import com.benasher44.uuid.uuid4
import com.fynnian.application.common.I18n
import com.fynnian.application.common.room.*
import components.*
import csstype.rem
import js.core.get
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.icons.material.KeyboardArrowLeft
import mui.icons.material.KeyboardArrowRight
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.*
import react.dom.events.MouseEvent
import react.router.useParams
import web.html.HTMLDivElement
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
  val (currentImage, setCurrentImage) = useState(0)

  val createAnswerRef = createRef<HTMLDivElement>()

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
          imageId = room!!.images[currentImage].id,
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

  fun finishRoom() {
    scope.launch {
      usersRoomStatus = api.finishRoom(roomCode, user!!)
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
      }
      else if (room.roomStatus == RoomStatus.NOT_READY || room.roomStatus == RoomStatus.CLOSED) {
        Spacer {
          size = SpacerPropsSize.MEDIUM
        }
        RoomUnavailableInfo {
          title = room.title
          code = room.code
          status = room.roomStatus
        }
      }
      else {
        Spacer {
          size = SpacerPropsSize.SMALL
        }
        RoomInfo {
          this.room = room
          this.userState = usersRoomStatus?.participationStatus
        }
        when (usersRoomStatus?.participationStatus) {
          UsersRoomParticipationStatus.NOT_STARTED -> {
            Spacer {
              size = SpacerPropsSize.SMALL
            }
            RoomInteractionInfo {
              type = RoomStatementVariant.INTRO
              cardAction = { startRoom() }
              interactionInfo = room.intro
            }
          }

          UsersRoomParticipationStatus.FINISHED -> {
            Spacer {
              size = SpacerPropsSize.SMALL
            }
            RoomInteractionInfo {
              type = RoomStatementVariant.OUTRO
              cardAction = {}
              interactionInfo = room.outro
            }
          }

          UsersRoomParticipationStatus.STARTED -> {
            // safety return when due to some isse there is no image present, dont brake the page
            if (room.images.isEmpty()) return@MainContainer

            val selectedImage = room.images[currentImage]
            val answersForCurrentImage = answers.filter { it.imageId == selectedImage.id }
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
            Box {
              sx {
                padding = 0.5.rem
              }
              Typography {
                variant = TypographyVariant.body1
                +I18n.get(
                  language,
                  I18n.TranslationKey.ROOM_ANSWER_ANSWERS_TOTAL,
                  I18n.TemplateProperty.Answers(answers.size)
                )
              }
              if (room.images.size > 1) room.images.mapIndexed { i, image ->
                Typography {
                  variant = TypographyVariant.body1
                  +I18n.get(
                    language,
                    I18n.TranslationKey.ROOM_ANSWER_ANSWERS_COUNT_PER_IMAGE,
                    I18n.TemplateProperty.Number(i + 1),
                    I18n.TemplateProperty.Answers(answers.filter { it.imageId == image.id }.size)
                  )
                }
              }
            }
            AnswerList {
              this.answers = answersForCurrentImage
              this.reloadAnswers = reloadAnswers
            }
          }

          else -> {}
        }
      }
    }
  }
}