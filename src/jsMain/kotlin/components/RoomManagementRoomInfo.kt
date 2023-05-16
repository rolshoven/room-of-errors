package components

import BinaryLabel
import LabelWithHelpText
import MissingContent
import com.fynnian.application.common.I18n
import com.fynnian.application.common.room.RoomManagementDetail
import com.fynnian.application.common.room.RoomPatch
import components.form.FormSwitchElement
import components.form.FromRoomTitle
import csstype.FlexWrap
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
  var isRoomReadyForOpening: Boolean
  var editRoomAction: (room: RoomPatch) -> Unit
}

val RoomManagementRoomInfo = FC<RoomManagementRoomInfoProps> { props ->

  val (language) = useContext(LanguageContext)
  val (edit, setEdit) = useState(false)
  val (roomTitle, setRoomTitle) = useState(props.room.title)
  val (description, setDescription) = useState(props.room.description)
  val (question, setQuestion) = useState(props.room.question)
  val (withTimeLimit, setWithTimeLimit) = useState(props.room.timeLimitMinutes != null)
  val (timeLimitMinutes, setTimeLimitMinutes) = useState(props.room.timeLimitMinutes)
  val (singleDeviceRoom, setSingleDeviceRoom) = useState(props.room.singleDeviceRoom)
  val (autoStartNextRoom, setAutoStartNextRoom) = useState(props.room.autoStartNextRoom)
  val (withGroupInfo, setWithGroupInfo) = useState(props.room.withGroupInformation)
  val (withGroupInfoText, setWithGroupInfoText) = useState(props.room.withGroupInformationText)

  fun setEditState(state: Boolean) {
    if (!state) {
      setRoomTitle(props.room.title)
      setDescription(props.room.description)
      setQuestion(props.room.question)
      setWithTimeLimit(props.room.timeLimitMinutes != null)
      setTimeLimitMinutes(props.room.timeLimitMinutes)
      setSingleDeviceRoom(props.room.singleDeviceRoom)
      setAutoStartNextRoom(props.room.autoStartNextRoom)
      setWithGroupInfo(props.room.withGroupInformation)
      setWithGroupInfoText(props.room.withGroupInformationText)
    }
    setEdit(state)
  }

  Card {
    sx {
      padding = 0.5.rem
    }
    CardHeader {
      avatar = RoomStatusBadge.create {
        status = props.room.roomStatus
      }
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
    if (!props.isRoomReadyForOpening) Alert {
      severity = AlertColor.warning
      +I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_INCOMPLETE_ROOM_ALERT)
    }
    if (!edit) CardContent {
      // title
      Typography {
        variant = TypographyVariant.caption
        +I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_TITLE_LABEL)
      }
      Typography {
        variant = TypographyVariant.body1
        +props.room.title
      }

      // description
      //Divider { variant = DividerVariant.middle}
      Spacer{ size = SpacerPropsSize.SMALL}
      Typography {
        variant = TypographyVariant.caption
        +I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_DESCRIPTION_LABEL)
      }
      if (props.room.description != null) Typography {
        variant = TypographyVariant.body1
        +props.room.description!!
      }
      else MissingContent {
        text = I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_DESCRIPTION_NOT_DEFINED)
      }

      // question
      //Divider { variant = DividerVariant.middle}
      Spacer{ size = SpacerPropsSize.SMALL}
      Typography {
        variant = TypographyVariant.caption
        +I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_QUESTION_LABEL)
      }
      if (props.room.question != null) Typography {
        variant = TypographyVariant.body1
        +props.room.question!!
      }
      else MissingContent {
        text = I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_QUESTION_NOT_DEFINED)
      }

      // time limit
      //Divider { variant = DividerVariant.middle}
      Spacer{ size = SpacerPropsSize.SMALL}
      Typography {
        variant = TypographyVariant.caption
        +I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_TIME_LIMIT_LABLE)
      }
      if (props.room.timeLimitMinutes != null) Typography {
        variant = TypographyVariant.body1
        +props.room.timeLimitMinutes.toString()
      }
      else MissingContent {
        text = I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_TIME_LIMIT_NOT_DEFINED)
      }

      // single device room
      Spacer { size = SpacerPropsSize.SMALL }
      LabelWithHelpText {
        this.language = language
        this.labelKey = I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_SINGLE_DEVICE_ROOM_SWITCH_LABLE
        this.helpTextKey = I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_SINGLE_DEVICE_ROOM_SWITCH_HELP_TEXT
      }
      BinaryLabel {
        this.state = singleDeviceRoom
        this.language = language
      }
      // auto start next room
      if (singleDeviceRoom) {
        Spacer { size = SpacerPropsSize.SMALL }
        LabelWithHelpText {
          this.language = language
          this.labelKey = I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_AUTO_START_NEXT_ROOM_SWITCH_LABLE
          this.helpTextKey = I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_AUTO_START_NEXT_ROOM_SWITCH_HELP_TEXT
        }
        BinaryLabel {
          this.state = autoStartNextRoom
          this.language = language
        }
      }
      // Group configuration
      Spacer { size = SpacerPropsSize.SMALL }
      Typography {
        variant = TypographyVariant.caption
        +I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_WITH_GROUP_INFO_SWITCH_LABEL)
      }
      Typography {
        variant = TypographyVariant.body1
        +if (withGroupInfo) I18n.get(language, I18n.TranslationKey.YES)
        else I18n.get(language, I18n.TranslationKey.NO)
      }
      // only show the withGroupInfoText if the option is enabled
      if (withGroupInfo) {
        Typography {
          variant = TypographyVariant.caption
          +I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_WITH_GROUP_INFO_TEXT_LABEL)
        }
        if (withGroupInfoText != null) Typography {
          variant = TypographyVariant.body1
          +withGroupInfoText.toString()
        }
        else MissingContent {
          text = I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_WITH_GROUP_INFO_TEXT_NOT_DEFINED)
        }
      }
    }
    else CardContent {
      FromRoomTitle {
        this.language = language
        this.title = roomTitle
        this.setTitle = setRoomTitle
      }
      Spacer { size = SpacerPropsSize.SMALL }
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
      Spacer { size = SpacerPropsSize.SMALL }
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
      Spacer { size = SpacerPropsSize.SMALL }
      FormGroup {
        FormControlLabel {
          label = ReactNode(
            I18n.get(
              language,
              I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_WITH_TIME_LIMIT_SWITCH_LABLE
            )
          )
          control = Switch.create {
            name = "withTimeLimit"
            checked = withTimeLimit
            onChange = { _, value -> setWithTimeLimit(value) }
          }
        }
        TextField {
          id = "timeLimit"
          name = "timeLimit"
          disabled = withTimeLimit.not()
          type = InputType.number
          label = ReactNode(I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_TIME_LIMIT_LABLE))
          helperText = ReactNode(I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_ROOM_TIME_LIMIT_HELP_TEXT))
          placeholder = "30"
          value = timeLimitMinutes ?: ""
          onChange = { setTimeLimitMinutes(it.target.unsafeCast<HTMLInputElement>().value.toInt()) }
        }
      }
      Spacer { size = SpacerPropsSize.SMALL }
      FormSwitchElement {
        this.language = language
        this.labelKey = I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_SINGLE_DEVICE_ROOM_SWITCH_LABLE
        this.helpTextKey = I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_SINGLE_DEVICE_ROOM_SWITCH_HELP_TEXT
        this.fieldName = "singleDeviceRoom"
        this.value = singleDeviceRoom
        this.onChange = { _, value ->
          setSingleDeviceRoom(value)
          if (value.not()) setAutoStartNextRoom(false)
        }
      }
      if (singleDeviceRoom) {
        Spacer { size = SpacerPropsSize.SMALL }
        FormSwitchElement {
          this.language = language
          this.labelKey = I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_AUTO_START_NEXT_ROOM_SWITCH_LABLE
          this.helpTextKey = I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_AUTO_START_NEXT_ROOM_SWITCH_HELP_TEXT
          this.fieldName = "autoStartNextRoom"
          this.value = autoStartNextRoom
          this.onChange = { _, value -> setAutoStartNextRoom(value) }
        }
      }
      Spacer { size = SpacerPropsSize.SMALL }
      FormGroup {
        FormControlLabel {
          label = ReactNode(
            I18n.get(
              language,
              I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_WITH_GROUP_INFO_SWITCH_LABEL
            )
          )
          control = Switch.create {
            name = "withGroupInfo"
            checked = withGroupInfo
            onChange = { _, value -> setWithGroupInfo(value) }
          }
        }
        TextField {
          id = "withGroupInfoText"
          name = "withGroupInfoText"
          disabled = withGroupInfo.not()
          type = InputType.text
          label = ReactNode(I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_CREATE_ROOM_WITH_GROUP_INFO_TEXT_LABEL))
          value = withGroupInfoText ?: ""
          onChange = {
            setWithGroupInfoText(it.target.unsafeCast<HTMLTextAreaElement>().value.ifBlank { null })
          }
        }
      }
      Spacer { size = SpacerPropsSize.SMALL }
      IconButton {
        Save()
        disabled = roomTitle.isBlank()
        onClick = {
          setEdit(false)
          props.editRoomAction(
            RoomPatch(
              props.room.code,
              roomTitle,
              description,
              question,
              if (withTimeLimit) timeLimitMinutes else null,
              singleDeviceRoom,
              autoStartNextRoom,
              withGroupInfo,
              withGroupInfoText
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
      sx {
        flexWrap = FlexWrap.wrap
      }
      +props.children
    }
  }
}