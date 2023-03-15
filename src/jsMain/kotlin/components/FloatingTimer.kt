package components

import com.fynnian.application.common.I18n
import csstype.*
import kotlinx.coroutines.MainScope
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.*
import react.dom.aria.ariaDescribedBy
import react.dom.aria.ariaLabelledBy
import web.timers.clearInterval
import web.timers.setInterval
import web.timers.setTimeout
import kotlin.time.Duration.Companion.seconds


private val scope = MainScope()

external interface FloatingTimerProps : Props {
  var timeLimitMinutes: Int
  var userStartTime: Instant
  var actonOnPastLimit: () -> Unit
}

val FloatingTimer = FC<FloatingTimerProps> { props ->

  val (language) = useContext(LanguageContext)

  val limitInSeconds = props.timeLimitMinutes * 60
  fun getRoomTime() = Clock.System.now().minus(props.userStartTime).inWholeSeconds

  val (remainingTime, setRemainingTime) = useState(limitInSeconds - getRoomTime())

  useEffect {
    val timer = setInterval(1.seconds) {
      setRemainingTime(limitInSeconds - getRoomTime())
    }
    cleanup { clearInterval(timer) }
  }

  fun getCountdownString(): String {
    if (remainingTime <= 0) return "00:00"
    val min = remainingTime.div(60).toString().padStart(2, '0')
    val sec = remainingTime.mod(60).toString().padStart(2, '0')
    return "$min:$sec"
  }

  fun getRemainingPercent() = remainingTime.div(limitInSeconds.div(100.0))

  if (remainingTime <= 0) {
    Dialog {
      open = true
      ariaLabelledBy = "alert-dialog-title"
      ariaDescribedBy = "alert-dialog-description"
      DialogTitle {
        +I18n.get(language, I18n.TranslationKey.ROOM_TIME_LIMIT_REACHED_DIALOG_TITLE)
      }
      DialogContent {
        DialogContentText {
          +I18n.get(language, I18n.TranslationKey.ROOM_TIME_LIMIT_REACHED_DIALOG_TEXT)
        }
        Spacer { size = SpacerPropsSize.MEDIUM }
        LinearProgress()
      }
    }
    setTimeout(5.seconds) { props.actonOnPastLimit() }
  }

  Paper {
    sx {
      position = Position.sticky
      top = (-1).px
      width = 100.pct
      padding = 0.5.rem
      zIndex = integer(10)
      textAlign = TextAlign.center
    }
    square = true
    LinearProgress {
      variant = LinearProgressVariant.determinate
      value = getRemainingPercent()
    }
    Spacer { size = SpacerPropsSize.SMALL }
    Typography {
      variant = TypographyVariant.body1
      +getCountdownString()
    }
  }
}