package top.ckxgzxa.networkandsensor.entity.weather

data class Weather(
    val aqi: Aqi,
    val city: String,
    val citycode: Int,
    val cityid: Int,
    val daily: List<Daily>,
    val date: String,
    val hourly: List<Hourly>,
    val humidity: String,
    val img: String,
    val index: List<Index>,
    val pressure: String,
    val temp: String,
    val temphigh: String,
    val templow: String,
    val updatetime: String,
    val weather: String,
    val week: String,
    val winddirect: String,
    val windpower: String,
    val windspeed: String
)