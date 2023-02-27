package components

import api.UserApi
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import com.benasher44.uuid.uuidFrom
import kotlinx.coroutines.*
import com.fynnian.application.common.user.User
import kotlinx.browser.localStorage
import mui.material.*
import react.*
import react.router.useNavigate

private const val userLocalStoreKey = "USER_ID"

fun getUserIdFromLocalStorage(): Uuid? =
  localStorage.getItem(userLocalStoreKey)?.let { uuidFrom(it) }

fun setUserIdFromLocalStorage(id: Uuid): Uuid {
  localStorage.setItem(userLocalStoreKey, id.toString())
  return getUserIdFromLocalStorage()!!
}

private val scope = MainScope()

val UserContext = createContext<StateInstance<User?>>()

val UserStorage = FC<PropsWithChildren> { props ->

  val api = UserApi()
  val userState = useState<User>()
  val (user, setUser) = userState

  useEffectOnce {
    val id = getUserIdFromLocalStorage() ?: setUserIdFromLocalStorage(uuid4())

    scope.launch {
        api.getUser(id)?.let { setUser(it) } ?: api.upsertUser(User(id, null))?.let { setUser(it) }
    }
  }

  UserContext(userState) {
    UserContext.Provider {
      value = userState
      +props.children
    }
  }
}

val NewUserSessionButton = FC<Props> {

  val api = UserApi()
  val navigate = useNavigate()
  val (user, setUser) = useContext(UserContext)

  Button {
    onClick = {
      val newUserId = setUserIdFromLocalStorage(uuid4())
      scope.launch {
        api.upsertUser(User(newUserId, null))?.let { setUser(it) }
      }
      navigate(0)
    }
    +"New User Session"
  }
}