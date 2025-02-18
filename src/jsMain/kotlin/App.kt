import com.fynnian.application.common.URLS
import com.fynnian.application.common.URLS.ROOM_CODE_PARAM
import com.fynnian.application.common.URLS.replaceParam
import components.*
import pages.Landingpage
import pages.Management
import pages.RoomManagementDetail
import pages.RoomPage
import react.FC
import react.Props
import react.create
import react.createElement
import react.dom.client.createRoot
import react.router.Route
import react.router.Routes
import react.router.dom.BrowserRouter
import web.dom.document

fun main() {
  createRoot(document.body).render(App.create())
}

private val App = FC<Props> {
  I18nProvider {
    ThemeModule {
      APIResponseSnackbar {
        UserStorage {
          Routing()
          Footer()
        }
      }
    }
  }
}

private val Routing = FC<Props> {
  BrowserRouter {
    Routes {
      Route {
        path = URLS.ROOT
        element = createElement(Landingpage)
      }
      Route {
        path = URLS.ROOM.replaceParam(ROOM_CODE_PARAM(":id"))
        element = createElement(RoomPage)
      }
      Route {
        path =  URLS.MANAGEMENT
        element = createElement(Management)
      }
      Route {
        path = URLS.MANAGEMENT_ROOM_DETAIL.replaceParam(ROOM_CODE_PARAM(":id"))
        element = createElement(RoomManagementDetail)
      }
    }
  }
}

// load and enable the additional time module
@JsModule("@js-joda/timezone")
@JsNonModule
external object JsJodaTimeZoneModule

private val jsJodaTz = JsJodaTimeZoneModule