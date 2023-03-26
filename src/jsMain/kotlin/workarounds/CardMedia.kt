package workarounds

import mui.material.CardMediaProps

inline var CardMediaProps.controls: Boolean
  get() = asDynamic().controls as Boolean
  set(value) {
    asDynamic().controls = value
  }

inline var CardMediaProps.ref: react.Ref<*>?
  get() = asDynamic().ref as? react.Ref<*>
  set(value) {
    asDynamic().ref = value
  }