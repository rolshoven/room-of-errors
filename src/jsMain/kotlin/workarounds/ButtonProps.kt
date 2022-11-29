package workarounds

import mui.material.ButtonProps

inline var ButtonProps.component: react.ElementType<*>?
  get() = TODO("Prop is write-only!")
  set(value) {
    asDynamic().component = value
  }