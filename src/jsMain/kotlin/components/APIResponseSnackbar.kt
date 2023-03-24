package components

import com.fynnian.application.common.APIErrorResponse
import mui.material.Snackbar
import react.*


data class APIResponseSnackbarData(
  val showSnackbar: StateSetter<Boolean>,
  val apiErrorSate: StateInstance<APIErrorResponse?>,
  val apiErrorResponse: StateSetter<APIErrorResponse?>
)

val APIResponseSnackbarContext = createContext<APIResponseSnackbarData>()

val APIResponseSnackbar = FC<PropsWithChildren> { props ->

  val (showSnackbar, setShowSnackbar) = useState(false)
  val apiErrorState = useState<APIErrorResponse?>(null)
  val (apiError, setApiError) = apiErrorState

  val contextData = APIResponseSnackbarData(
    setShowSnackbar,
    apiErrorState,
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