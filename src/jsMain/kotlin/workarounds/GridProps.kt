package workarounds

import mui.material.GridProps

// mui version  5.9.1-pre.442 is missing som properties for the grid
// https://github.com/JetBrains/kotlin-wrappers/issues/1856
// https://github.com/karakum-team/kotlin-mui-showcase/blob/main/src/main/kotlin/team/karakum/common/MissedWrappers.kt

inline var GridProps.xs: Int
  get() = TODO("Prop is write-only!")
  set(value) {
    asDynamic().xs = value
  }
inline var GridProps.md: Int
  get() = TODO("Prop is write-only!")
  set(value) {
    asDynamic().md = value
  }
