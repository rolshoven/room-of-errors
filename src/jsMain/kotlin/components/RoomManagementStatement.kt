package components

import com.fynnian.application.common.room.RoomStatementVariant
import com.fynnian.application.common.room.RoomInteractionInfo
import csstype.TextAlign
import csstype.rem
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import react.StateSetter
import react.dom.html.ReactHTML
import workarounds.controls

external interface RoomManagementInteractionInfoProps : Props {
  var variant: RoomStatementVariant
  var code: String
  var interactionInfo: RoomInteractionInfo
  var setStatement: StateSetter<RoomInteractionInfo?>
}

val RoomManagementInteractionInfo = FC<RoomManagementInteractionInfoProps> { props ->
  Card {
    sx {
      maxWidth = 21.rem
      padding = 0.5.rem
    }
    CardHeader {
      title = ReactNode(props.variant.toString())
    }
    if (props.interactionInfo.videoURl != null) CardMedia {
      sx {
        width = 20.rem
        height = 20.rem
      }
      component = ReactHTML.video
      src = props.interactionInfo.videoURl
      controls = true
    }
    CardContent {
      sx {
        textAlign = TextAlign.center
      }
      if (props.interactionInfo.videoURl == null) Typography {
        variant = TypographyVariant.overline
        +"There is no video"
      }
      if (props.interactionInfo.text != null) Typography {
        variant = TypographyVariant.body1
        +props.interactionInfo.text!!
      }
      else Typography {
        variant = TypographyVariant.overline
        +"There is no description"
      }
    }
    CardActions {
      CreateRoomInteractionInfoDialog {
        code = props.code
        variant = props.variant
        setStatement = props.setStatement
      }
    }
  }
}