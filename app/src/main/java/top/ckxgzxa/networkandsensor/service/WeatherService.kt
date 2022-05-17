package top.ckxgzxa.networkandsensor.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import top.ckxgzxa.networkandsensor.entity.weather.WeatherResult

interface WeatherService {

    @GET("/weather/query")
    @Headers("Authorization:APPCODE 3eca59e22c85413cb3798167a61a904f")
    fun getWeatherInfo(@Query("city") city: String): Call<WeatherResult>

}