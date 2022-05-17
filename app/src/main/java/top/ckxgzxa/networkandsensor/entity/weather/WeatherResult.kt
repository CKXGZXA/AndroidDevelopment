package top.ckxgzxa.networkandsensor.entity.weather

data class WeatherResult(
    val msg: String,
    val result: Weather,
    val status: Int
)