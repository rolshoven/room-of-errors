package pages

import com.benasher44.uuid.uuid4
import com.fynnian.application.common.room.*
import components.MainContainer
import components.UserContext
import csstype.*
import kotlinx.js.get
import mui.icons.material.Save
import mui.icons.material.Warning
import mui.material.*
import mui.material.Size
import mui.system.sx
import org.w3c.dom.HTMLTextAreaElement
import react.FC
import react.Props
import react.dom.html.ImgLoading
import react.dom.html.InputType
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.main
import react.dom.html.ReactHTML.p
import react.dom.onChange
import react.router.useParams
import react.useContext
import react.useState

val RoomPage = FC<Props> {

  val roomId = useParams()["id"] ?: ""
  val user = useContext(UserContext)
  val (cord, setCord) = useState<Coordinates>()
  val (answers, setAnswers) = useState<MutableList<Answer>>(mutableListOf())
  val (currentAnswer, setCurrentAnswer) = useState("")
  val room = Room(
    roomId,
    RoomStatus.OPEN,
    "Station",
    "",
    "",
    1,
    listOf(RoomImage(
      uuid4(),
      "https://static01.nyt.com/images/2019/03/24/travel/24trending-shophotels1/24trending-shophotels1-jumbo.jpg?quality=75&auto=webp",
      "station image"
    )
    )
  )

  fun addAnswer(answer: Answer) {
    setAnswers { it.add(answer); it }
  }

  MainContainer {
    Box {
      Typography {
        component = p
        + "Room name ${room.title}"
      }
      Typography {
        component = p
        + "Room Code: ${room.code}"
      }
      Typography {
        component = p
        + "mark all the errors on the image"
      }
    }
    Box {
      img {
        id = "room-image"
        src = room.images.first().url
        alt = room.images.first().title
        loading = ImgLoading.lazy
        onClick = {
          setCord(Coordinates(it.clientX, it.clientY))
        }
      }
      answers.forEach {
        Icon {
          sx {
            position = Position.absolute
            top = it.coordinates.vertical.px
            left = it.coordinates.horizontal.px
          }
          Warning {
            sx {
              color = NamedColor.black
            }
          }
        }
      }
      cord?.let {
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
                    roomCode = roomId,
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

      Typography {
        component = p
        + (cord?.let { "clicked ${cord.horizontal} | ${cord.vertical}" } ?: "click on the image")
      }
      answers.map {
        Typography {
          component = p
          + "${it.coordinates} - ${it.answer}"
        }
      }
    }
  }
}