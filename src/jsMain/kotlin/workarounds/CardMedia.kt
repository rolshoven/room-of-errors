package workarounds

import mui.material.CardMediaProps

inline var CardMediaProps.controls: Boolean
  get() = asDynamic().controls as Boolean
  set(value) {
    asDynamic().controls = value
  }