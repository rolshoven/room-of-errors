package components

import MissingContent
import com.fynnian.application.common.I18n
import com.fynnian.application.common.room.RoomManagementDetail
import com.fynnian.application.common.room.RoomPatch
import components.form.FromRoomTitle
import csstype.rem
import js.core.jso
import mui.icons.material.Close
import mui.icons.material.Edit
import mui.icons.material.Save
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.onChange
import web.html.HTMLTextAreaElement
import web.html.InputType

external interface RoomManagementRoomInfoProps : PropsWithChildren {
  var room: RoomManagementDetail
  var editRoomAction: (room: RoomPatch) -> Unit
}

val RoomManagementRoomInfo = FC<RoomManagementRoomInfoProps> { props ->

  val (language) = useContext(LanguageContext)
  val (edit, setEdit) = useState(false)
  val (roomTitle, setRoomTitle) = useState(props.room.title)
  val (description, setDescription) = useState(props.room.description)
  val (question, setQuestion) = useState(props.room.question)


  fun setEditState(state: Boolean) {
    if (!state) {
      setRoomTitle(props.room.title)
      setDescription(props.room.description)
      setQuestion(props.room.question)
    }
    setEdit(state)
  }

  Card {
    sx {
      padding = 0.5.rem
    }
    CardHeader {
      avatar = createElement(RoomStatusBadge, jso { status = props.room.roomStatus } )
      title = ReactNode(
        I18n.get(
          language,
          I18n.TranslationKey.ROOM_INFO_LABEL_NAME,
          I18n.TemplateProperty.RoomCode(props.room.code),
          I18n.TemplateProperty.RoomTitle(props.room.title)
        )
      )
      titleTypographyProps = jso {
        variant = TypographyVariant.body1
      }
      action = createElement(
        IconButton,
        jso {
          color = IconButtonColor.primary
          onClick = { setEditState(!edit) }
        },
        createElement(if (edit) Close else Edit)
      )
    }
    if (!edit) CardContent {
      Typography {
        variant = TypographyVariant.caption
        + I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_TITLE_LABEL)
      }
      Typography {
        variant = TypographyVariant.body1
        +props.room.title
      }
      Typography {
        variant = TypographyVariant.caption
        + I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_DESCRIPTION_LABEL)
      }
      if (props.room.description != null) Typography {
        variant = TypographyVariant.body1
        +props.room.description!!
      }
      else MissingContent { text = "There is no room description" }
      Typography {
        variant = TypographyVariant.caption
        + I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_QUESTION_LABEL)
      }
      if (props.room.question != null) Typography {
        variant = TypographyVariant.body1
        +props.room.question!!
      }
      else MissingContent { text = "There is no room question" }
    }
    else CardContent {
      FromRoomTitle {
        this.language = language
        this.title = roomTitle
        this.setTitle = setRoomTitle
      }
      Spacer { size = SpacerPropsSize.VERY_SMALL }
      FormGroup {
        TextField {
          id = "description"
          name = "description"
          type = InputType.text
          multiline = true
          label = ReactNode(I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_DESCRIPTION_LABEL))
          placeholder = "..."
          value = description ?: ""
          onChange = {
            val e = it.target as HTMLTextAreaElement
            setDescription(e.value.ifBlank { null })
          }
        }
      }
      Spacer { size = SpacerPropsSize.VERY_SMALL }
      FormGroup {
        TextField {
          id = "question"
          name = "question"
          type = InputType.text
          multiline = true
          label = ReactNode(I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_QUESTION_LABEL))
          placeholder = "..."
          value = question ?: ""
          onChange = {
            val e = it.target as HTMLTextAreaElement
            setQuestion(e.value.ifBlank { null })
          }
        }
      }
      IconButton {
        Save()
        disabled = roomTitle.isBlank()
        onClick = {
          setEditState(false)
          props.editRoomAction(
            RoomPatch(
              props.room.code,
              roomTitle,
              description,
              question,
              null // ToDo: time
            )
          )
        }
      }
      IconButton {
        Close()
        onClick = { setEditState(false) }
      }
    }
    CardActions {
      +props.children
    }
  }
}