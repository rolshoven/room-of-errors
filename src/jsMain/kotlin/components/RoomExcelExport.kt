package components

import com.fynnian.application.common.I18n
import com.fynnian.application.common.URLS
import com.fynnian.application.common.URLS.addQuerParams
import com.fynnian.application.common.URLS.replaceParam
import mui.icons.material.FileDownload
import mui.material.Button
import mui.material.ButtonVariant
import react.FC
import react.Props
import react.dom.aria.ariaLabel
import react.useContext
import web.dom.document
import web.html.HTML

external interface RoomExcelExportProps: Props {
  var code: String
}

val RoomExcelExport = FC<RoomExcelExportProps> { props ->

  val (language) = useContext(LanguageContext)

  Button {
    +I18n.get(language, I18n.TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_BUTTON_EXCEL_EXPORT)
    FileDownload()
    variant = ButtonVariant.text
    ariaLabel = "Excel Export Room"
    onClick = {
      document.createElement(HTML.a)
        .apply {
          href = URLS.API_ROOMS_MANAGEMENT_EXCEL_EXPORT
            .replaceParam(URLS.ROOM_CODE_PARAM(props.code))
            .addQuerParams(URLS.LANGUAGE_PARAM(language))
          download = I18n.get(
            language,
            I18n.TranslationKey.ROOM_MANAGEMENT_ROOM_LIST_EXCEL_EXPORT_FILE_NAME,
            I18n.TemplateProperty.RoomCode(props.code)
          )
        }
        .also {
          document.body.appendChild(it)
          it.click()
          document.body.removeChild(it)
        }
    }
  }
}