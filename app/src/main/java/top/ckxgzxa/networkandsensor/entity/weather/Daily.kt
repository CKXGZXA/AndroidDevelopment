package top.ckxgzxa.networkandsensor.entity.weather

data class Daily(
    val date: String,
    val day: Day,
    val night: Night,
    val sunrise: String,
    val sunset: String,
    val week: String
)