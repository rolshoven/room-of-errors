package components

import api.RoomManagementApi
import com.fynnian.application.common.I18n
import com.fynnian.application.common.room.RoomInteractionInfo
import com.fynnian.application.common.room.RoomStatementVariant
import csstype.AlignItems
import csstype.Display
import csstype.FlexDirection
import csstype.rem
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.icons.material.Clear
import mui.icons.material.Save
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.responsive
import mui.system.sx
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.dom.onChange
import web.file.File
import web.html.HTMLTextAreaElement
import web.html.InputType
import workarounds.component

private val scope = MainScope()

external interface CreateRoomInteractionInfoProps : Props {
  var code: String
  var variant: RoomStatementVariant
  var interactionInfo: RoomInteractionInfo
  var setStatement: StateSetter<RoomInteractionInfo?>
}

val CreateRoomInteractionInfoDialog = FC<CreateRoomInteractionInfoProps> { props ->

  val api = RoomManagementApi()

  val language by useContext(LanguageContext)

  val (open, setOpen) = useState(false)
  val (changeVideo, setChangeVideo) = useState(false)
  val (successful, setSuccessful) = useState<Boolean?>(null)
  val (text, setText) = useState(props.interactionInfo.text)
  val (title, setTitle) = useState<String?>(null)
  val (file, setFile) = useState<File?>(null)

  fun close() {
    setFile(null)
    setTitle(null)
    setText(props.interactionInfo.text)
    setSuccessful(null)
    setChangeVideo(false)
    setOpen(false)
  }

  fun addInteractionInfo() {
    scope.launch {
      if (changeVideo) {
        api.upsertRoomInteractionInfoWithUpload(
          props.variant,
          props.code,
          text,
          title!!,
          file!!
        )
      } else {
        api.upsertRoomInteractionInfo(props.variant, props.code, text)
      }.let {
        if (it != null) {
          close()
          props.setStatement(
            when (props.variant) {
              RoomStatementVariant.INTRO -> it.intro
              RoomStatementVariant.OUTRO -> it.outro
            }
          )
        } else setSuccessful(false)
      }
    }
  }

  Button {
    variant = ButtonVariant.outlined
    onClick = { setOpen(true) }
    //+ I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_DIALOG_BUTTON)
    +"Update ${props.variant}" // Todo
  }
  Dialog {
    this.open = open
    maxWidth = "sm"
    fullWidth = true

    DialogTitle {
      +"Update ${props.variant}" // Todo
    }

    DialogContent {
      sx {
        display = Display.flex
        flexDirection = FlexDirection.column
        gap = 0.5.rem
      }
      FormGroup {
        FormControlLabel {
          label = ReactNode(I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_INTERACTION_INFO_FORM_SWITCH))
          control = Switch.create {
            onChange = { _, value -> setChangeVideo(value) }
          }
        }
      }
      Divider()
      Spacer { size = SpacerPropsSize.SMALL }
      FormGroup {
        TextField {
          id = "description"
          name = "description"
          type = InputType.text
          placeholder = "some description or other ${props.variant.toString().lowercase()} text"
          helperText = ReactNode(
            """
            Optional text, additional ${props.variant.toString().lowercase()} text to guide the user. 
            """.trimIndent()
          )
          multiline = true
          minRows = 2
          fullWidth = true
          value = text ?: ""
          onChange = {
            val e = it.target as HTMLTextAreaElement
            setText(e.value.ifEmpty { null })
          }
        }
      }
      if (changeVideo) {
        FormGroup {
          TextField {
            id = "videoTitle"
            name = "videoTitle"
            type = InputType.text
            required = true
            label =
              ReactNode("Video Title") // ReactNode(I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_TITLE_LABEL))
            placeholder = "intro"
            value = title ?: ""
            onChange = {
              val e = it.target as HTMLInputElement
              setTitle(e.value.ifEmpty { null })
            }
          }
        }
        FormGroup {
          Button {
            variant = ButtonVariant.contained
            component = label
            +I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_UPLOAD_LABEL) // Todo
            input {
              hidden = true
              accept = "video/mp4"
              type = InputType.file
              onChange = {
                val newFile = it.target.files?.item(0)
                setFile(newFile)
                if (title == null) setTitle(newFile?.name)
              }
            }
          }
          Stack {
            direction = responsive(StackDirection.row)
            spacing = responsive(2)
            sx {
              alignItems = AlignItems.center
            }
            Typography {
              variant = TypographyVariant.subtitle1
              +(file?.name ?: I18n.get(
                language,
                I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_IMAGE_UPLOAD_MISSING_FILE // Todo
              ))
            }
            IconButton {
              color = IconButtonColor.primary
              onClick = { setFile(null) }
              Clear()
            }
          }
        }
      }
    }
    DialogActions {
      IconButton {
        color = IconButtonColor.primary
        onClick = { close() }
        Clear()
      }
      IconButton {
        disabled = if (changeVideo) file == null || title == null else false
        color = IconButtonColor.primary
        onClick = { addInteractionInfo() }
        Save()
      }
    }
  }
}
