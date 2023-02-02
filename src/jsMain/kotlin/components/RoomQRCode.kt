package components

import com.fynnian.application.common.I18n
import csstype.*
import kotlinx.browser.window
import mui.icons.material.Clear
import mui.icons.material.QrCode
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.dom.aria.ariaLabel
import react.useContext
import react.useState
import web.dom.document
import web.html.HTML
import wrappers.QRCodeSVG
import wrappers.toPng

fun genRoomQRCodeContainerId(roomCode: String) = "qr-code-container-$roomCode"

external interface RoomQRCodeDialogProps : Props {
  var roomCode: String
}

val RoomQRCodeDialog = FC<RoomQRCodeDialogProps> { props ->
  val (language) = useContext(LanguageContext)

  var open: Boolean by useState(false)
  var showSaveMessage: Boolean by useState(false)
  var error: Boolean by useState(false)

  fun onClose() {
    open = false
    error = false
    showSaveMessage = false
  }

  fun showError() {
    showSaveMessage = true
    error = true
  }

  fun saveCode() {
    document.getElementById(genRoomQRCodeContainerId(props.roomCode))
      ?.let { qrCodeContainer ->
        toPng(qrCodeContainer)
          .then { dataUrl ->
            document.createElement(HTML.a)
              .apply {
                href = dataUrl
                download = "room-${props.roomCode}.png"
              }
              .also {
                document.body.appendChild(it)
                it.click()
                document.body.removeChild(it)
                showSaveMessage = true
              }
          }
          .catch { showError() }
      }
      ?: showError()
  }

  IconButton {
    QrCode()
    color = IconButtonColor.primary
    ariaLabel = "Show QrCode"
    onClick = { open = true }
  }
  Dialog {
    this.open = open
    maxWidth = "sm"
    onClose = { _, _ -> onClose() }

    DialogContent {
      sx {
        display = Display.flex
        flexDirection = FlexDirection.column
        alignItems = AlignItems.center
      }
      Box {
        RoomQRCode {
          roomCode = props.roomCode
        }
      }
      if (showSaveMessage) {
        Spacer { size = SpacerPropsSize.SMALL }
        Alert {
          if (error) {
            severity = AlertColor.error
            +I18n.get(language, I18n.TranslationKey.ROOM_QRCODE_ALERT_DOWNLOAD_ERROR)
          } else {
            severity = AlertColor.info
            +I18n.get(language, I18n.TranslationKey.ROOM_QRCODE_ALERT_DOWNLOAD_SUCCESS)
          }
        }
      }
    }
    DialogActions {
      Button {
        onClick = { saveCode() }
        +I18n.get(language, I18n.TranslationKey.ROOM_QRCODE_BUTTON_DOWNLOAD)
      }
      IconButton {
        Clear()
        color = IconButtonColor.primary
        ariaLabel = "Close dialog"
        onClick = { onClose() }
      }
    }
  }
}

external interface RoomQRCodeProps : Props {
  var roomCode: String
}

val RoomQRCode = FC<RoomQRCodeProps> { props ->
  val (language) = useContext(LanguageContext)
  val url = window.location.origin + "/room/${props.roomCode}"

  Box {
    id = genRoomQRCodeContainerId(props.roomCode)
    sx {
      display = Display.flex
      flexDirection = FlexDirection.column
      padding = 1.rem
      backgroundColor = NamedColor.white
      maxWidth = Length.minContent
    }
    QRCodeSVG {
      value = url
      size = 256
    }
    Spacer { size = SpacerPropsSize.SMALL }
    Typography {
      sx {
        color = NamedColor.black
        textAlign = TextAlign.center
      }
      variant = TypographyVariant.subtitle1
      +I18n.get(language, I18n.TranslationKey.ROOM_QRCODE_IMAGE_LABEL)
    }
    Typography {
      sx {
        color = NamedColor.black
        textAlign = TextAlign.center
      }
      variant = TypographyVariant.subtitle1
      +props.roomCode
    }
  }
}