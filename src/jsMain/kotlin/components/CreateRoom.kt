package components

import com.benasher44.uuid.uuid4
import csstype.Display
import csstype.FlexDirection
import csstype.number
import csstype.rem
import js.core.jso
import mui.icons.material.Cached
import mui.icons.material.Clear
import mui.icons.material.Save
import mui.material.*
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import react.dom.html.InputType
import react.dom.html.ReactHTML.form
import react.useState

external interface CreateRoomDialogProps : Props {

}

val CreateRoomDialog = FC<CreateRoomDialogProps> {

  var generatedCode: String by useState(genRoomCode())
  var open: Boolean by useState(false)

  Button {
    variant = ButtonVariant.outlined
    onClick = { open = true }
    +"Create Room"
  }
  Dialog {
    this.open = open
    maxWidth = "sm"
    fullWidth = true

    DialogTitle {
      +"Create a new Room"
    }

    DialogContent {
      Box {
        component = form
        onSubmit = {
          console.log(it)
        }
        sx {
          display = Display.flex
          flexDirection = FlexDirection.column
          gap = 1.rem

        }

        Spacer {
          size = SpacerPropsSize.SMALL
        }
        FormGroup {
          row = true
          sx {
            gap = 1.rem
          }
          TextField {
            id = "roomCode"
            name = "roomCode"
            type = InputType.text
            label = ReactNode("ROOM CODE")
            value = generatedCode
            inputProps = jso {
              this as InputBaseProps
              readOnly = true
            }
            sx {
              flex = number(2.0)
            }
          }
          IconButton {
            onClick = {
              generatedCode = genRoomCode()
            }
            Cached { }
          }
        }
        FormGroup {
          TextField {
            id = "title"
            name = "title"
            type = InputType.text
            label = ReactNode("Title")
            placeholder = "my room"
          }
        }
        FormGroup {
          TextField {
            id = "description"
            name = "description"
            type = InputType.text
            multiline = true
            label = ReactNode("Description")
            placeholder = "..."
          }
        }
        FormGroup {
          TextField {
            id = "question"
            name = "question"
            type = InputType.text
            multiline = true
            label = ReactNode("Question")
            placeholder = "..."
          }
        }
        FormGroup {
          TextField {
            id = "imageTitle"
            name = "imageTitle"
            type = InputType.text
            label = ReactNode("Image Title")
            placeholder = "room 1"
          }
          TextField {
            id = "imageUrl"
            name = "imageUrl"
            type = InputType.url
            label = ReactNode("Image Url")
            placeholder = "https://..."
          }
// toDo file upload
//          TextField {
//            id = "imageTitle"
//            name = "imageTitle"
//            type = InputType.file
//          }
        }
      }
      DialogActions {
        IconButton {
          color = IconButtonColor.primary
          onClick = { open = false }
          Clear()
        }
        IconButton {
          color = IconButtonColor.primary
          onClick = { open = false }
          Save()
        }
      }
    }
  }
}

fun genRoomCode(): String {
  return uuid4().toString().split("-")[0]
}