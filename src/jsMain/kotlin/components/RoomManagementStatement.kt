package components

import com.fynnian.application.common.room.RoomStatementVariant
import com.fynnian.application.common.room.RoomStatements
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

external interface RoomManagementStatementProps : Props {
  var variant: RoomStatementVariant
  var code: String
  var statement: RoomStatements
  var setStatement: StateSetter<RoomStatements?>
}

val RoomManagementStatement = FC<RoomManagementStatementProps> { props ->
  Card {
    sx {
      maxWidth = 21.rem
      padding = 0.5.rem
    }
    CardHeader {
      title = ReactNode(props.variant.toString())
    }
    if (props.statement.videoURl != null) CardMedia {
      sx {
        width = 20.rem
        height = 20.rem
      }
      component = ReactHTML.video
      src = props.statement.videoURl
      controls = true
    }
    CardContent {
      sx {
        textAlign = TextAlign.center
      }
      if (props.statement.videoURl == null) Typography {
        variant = TypographyVariant.overline
        +"There is no video"
      }
      if (props.statement.text != null) Typography {
        variant = TypographyVariant.body1
        +props.statement.text!!
      }
      else Typography {
        variant = TypographyVariant.overline
        +"There is no description"
      }
    }
    CardActions {
      CreateRoomStatementDialog {
        code = props.code
        variant = props.variant
        setStatement = props.setStatement
      }
    }
  }
}