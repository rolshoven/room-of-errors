import com.fynnian.application.common.AppPaths
import components.I18nProvider
import components.ThemeModule
import components.UserStorage
import pages.Landingpage
import pages.Management
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
  createRoot(
    document.createElement("div").also {
      document.body.appendChild(it)
    }
  ).render(App.create())
}

private val App = FC<Props> {
  I18nProvider {
    ThemeModule {
      UserStorage {
        Routing()
      }
    }
  }
}

private val Routing = FC<Props> {
  BrowserRouter {
    Routes {
      Route {
        index = true
        element = createElement(Landingpage)
      }
      Route {
        path = AppPaths.ROOM.path + "/:id"
        element = createElement(RoomPage)
      }
      Route {
        path = AppPaths.MANAGEMENT.path
        element = createElement(Management)
      }
    }
  }
}
