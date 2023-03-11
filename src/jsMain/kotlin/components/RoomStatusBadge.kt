package components

import com.fynnian.application.common.I18n
import com.fynnian.application.common.room.RoomStatus
import csstype.rem
import mui.material.Chip
import mui.material.ChipColor
import mui.material.Size
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import react.useContext

external interface RoomStatusBadgeProps : Props {
  var status: RoomStatus
}

val RoomStatusBadge = FC<RoomStatusBadgeProps> { props ->

  val (language) = useContext(LanguageContext)

  Chip {
    sx {
      minWidth = 7.rem
    }
    val (chipColor, translationKey) = when (props.status) {
      RoomStatus.NOT_READY -> ChipColor.warning to I18n.TranslationKey.ROOM_STATUS_NOT_READY
      RoomStatus.OPEN -> ChipColor.success to I18n.TranslationKey.ROOM_STATUS_OPEN
      RoomStatus.CLOSED -> ChipColor.error to I18n.TranslationKey.ROOM_STATUS_CLOSED
    }
    label = ReactNode(I18n.get(language, translationKey))
    color = chipColor
    size = Size.small
  }
}