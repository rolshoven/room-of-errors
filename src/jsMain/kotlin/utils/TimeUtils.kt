
fun getCountdownString(remainingTime: Long): String {
  if (remainingTime <= 0) return "00:00"
  val min = remainingTime.div(60).toString().padStart(2, '0')
  val sec = remainingTime.mod(60).toString().padStart(2, '0')
  return "$min:$sec"
}