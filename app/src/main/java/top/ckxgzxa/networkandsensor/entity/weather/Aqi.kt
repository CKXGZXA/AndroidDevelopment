package top.ckxgzxa.networkandsensor.entity.weather

data class Aqi(
    val aqi: String,
    val aqiinfo: Aqiinfo,
    val co: String,
    val co24: String,
    val ico: String,
    val ino2: String,
    val io3: String,
    val io38: String,
    val ipm10: String,
    val ipm2_5: String,
    val iso2: String,
    val no2: String,
    val no224: String,
    val o3: String,
    val o324: String,
    val o38: String,
    val pm10: String,
    val pm1024: String,
    val pm2_5: String,
    val pm2_524: String,
    val primarypollutant: String,
    val quality: String,
    val so2: String,
    val so224: String,
    val timepoint: String
)