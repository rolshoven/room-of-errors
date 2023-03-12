package components

import com.fynnian.application.common.APIErrorResponse
import mui.material.Snackbar
import react.*


data class APIResponseSnackbarData(
  val showSnackbar: StateSetter<Boolean>,
  var apiErrorResponse: StateSetter<APIErrorResponse?>
)

val APIResponseSnackbarContext = createContext<APIResponseSnackbarData>()

val APIResponseSnackbar = FC<PropsWithChildren> { props ->

  val (showSnackbar, setShowSnackbar) = useState(false)
  val (apiError, setApiError) = useState<APIErrorResponse?>(null)

  val contextData = APIResponseSnackbarData(
    setShowSnackbar,
    setApiError
  )

  APIResponseSnackbarContext(contextData) {
    APIResponseSnackbarContext.Provider {
      value = contextData
      +props.children
      Snackbar {
        open = showSnackbar
        autoHideDuration = 8000
        message = ReactNode(apiError.toString())
        onClose = { _,_ -> setShowSnackbar(false) }
      }
    }
  }

}