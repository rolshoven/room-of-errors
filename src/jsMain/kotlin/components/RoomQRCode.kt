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
import wrappers.QRCodeSVG

external interface RoomQRCodeDialogProps : Props {
  var roomCode: String
}

val RoomQRCodeDialog = FC<RoomQRCodeDialogProps> { props ->
  var open: Boolean by useState(false)

  IconButton {
    onClick = { open = true }
    QrCode()
  }
  Dialog {
    this.open = open
    maxWidth = "sm"
    onClose = { _, _ ->
      open = false
    }

    DialogContent {
      RoomQRCode {
        roomCode = props.roomCode
      }
    }
    DialogActions {
      IconButton {
        color = IconButtonColor.primary
        onClick = { open = false }
        Clear()
      }
    }
  }
}

external interface RoomQRCodeProps : Props {
  var roomCode: String
}

val RoomQRCode = FC<RoomQRCodeProps> {props ->
  val url = window.location.origin + "/room/${props.roomCode}"

  Box {
    sx {
      display = Display.flex
      flexDirection = FlexDirection.column
      padding = 1.rem
      backgroundColor = NamedColor.white
    }
    QRCodeSVG {
      value = url
    }
    Typography {
      sx {
        color = NamedColor.black
        textAlign = TextAlign.center
      }
      variant = TypographyVariant.subtitle1
      + "ROOM CODE"
    }
    Typography {
      sx {
        color = NamedColor.black
        textAlign = TextAlign.center
      }
      variant = TypographyVariant.subtitle1
      + props.roomCode
    }
  }
}