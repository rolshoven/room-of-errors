package components

import com.fynnian.application.common.I18n
import csstype.TextAlign
import csstype.pct
import getCountdownString
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import mui.material.LinearProgress
import mui.material.Paper
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.*
import web.timers.clearInterval
import web.timers.setInterval
import web.timers.setTimeout
import kotlin.time.Duration.Companion.seconds


external interface RoomRestartTimerProps : Props {
  var timeLimitSeconds: Long
  var userFinishTime: Instant
  var actonOnPastLimit: () -> Unit
}

val RoomRestartTimer = FC<RoomRestartTimerProps> { props ->

  val (language) = useContext(LanguageContext)

  val limitInSeconds = props.timeLimitSeconds
  fun getFinishTime() = Clock.System.now().minus(props.userFinishTime).inWholeSeconds

  val (remainingTime, setRemainingTime) = useState(limitInSeconds - getFinishTime())

  useEffect {
    val timer = setInterval(1.seconds) {
      setRemainingTime(limitInSeconds - getFinishTime())
    }
    cleanup { clearInterval(timer) }
  }

  Paper {
    square = true
    sx {
      width = 100.pct
      textAlign = TextAlign.center
    }
    if (remainingTime <= 0) {
      setTimeout(3.seconds) { props.actonOnPastLimit() }
      Typography {
        variant = TypographyVariant.body1
        +I18n.get(language, I18n.TranslationKey.ROOM_RESTART_TIMER_ROOM_RESETTING)
      }
      LinearProgress()
    } else {
      Typography {
        variant = TypographyVariant.body1
        +I18n.get(
          language, I18n.TranslationKey.ROOM_RESTART_TIMER_COUNTING,
          I18n.TemplateProperty.Time(getCountdownString(remainingTime))
        )
      }
    }
  }
}