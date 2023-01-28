package components

import csstype.*
import kotlinx.browser.window
import mui.icons.material.Clear
import mui.icons.material.QrCode
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
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
    onClick = { open = true }
    QrCode()
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
            +"Could not generate and download image"
          } else {
            severity = AlertColor.info
            +"Successfully generate and downloaded image"
          }
        }
      }
    }
    DialogActions {
      Button {
        onClick = { saveCode() }
        +"Save Code"
      }
      IconButton {
        color = IconButtonColor.primary
        onClick = { onClose() }
        Clear()
      }
    }
  }
}

external interface RoomQRCodeProps : Props {
  var roomCode: String
}

val RoomQRCode = FC<RoomQRCodeProps> { props ->
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
      +"ROOM CODE"
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