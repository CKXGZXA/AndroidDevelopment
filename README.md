# 作业二：网络/传感器应用实践

本应用采用`Kotlin`编写，主要依赖库有`viewPager2`, `Retrofit` 库,可通过以下代码引入：

```
    // viewPager2
    implementation 'androidx.viewpager2:viewpager2:1.0.0'
    // Retrofit
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
```

使用了`视图绑定`功能，在app目录下的`build.gradle`添加:

```
android {
    viewBinding {
        enabled = true
    }
}
```



### 1. EnterActivity

这是应用的入口Activity，初始化视图后，调用`getSharedPreferences`方法获取数据，若设备中存在`app_id`和`app_secret`，则将其显示到`EditText`中

```kotlin
val prefs = getSharedPreferences("app_config", MODE_PRIVATE)
prefs.apply {
    binding.appIdEdit.setText(getString("app_id", ""))
    binding.appSecretEdit.setText(getString("app_secret", ""))
}
```

随后给保存按钮设置监听器，其中首先进行判断，防止将空的id和secret存入设备，用户试图保存，使用`Toast`进行提示:

```kotlin
Toast.makeText(this, "请输入正确的app_id和app_secret", Toast.LENGTH_SHORT).show()
```

如果已经存入id和secret，用户点击保存，使用`AlertDialog`弹出警告对话框，提示是否要覆盖已有数据，点击覆盖之后，将新的数据存入设备：

```kotlin
// 判断是否已经存在
if (prefs.contains("app_id") || prefs.contains("app_secret")) {
    // 弹出Dialog, 提示是否要覆盖
    AlertDialog.Builder(this)
        .setTitle("Warning")
        .setMessage("已经存在app_id和app_secret, 是否覆盖?")
        .setPositiveButton("覆盖") { _, _ ->
            // 保存
            prefs.edit {
                putString("app_id", binding.appIdEdit.text.toString())
                putString("app_secret", binding.appSecretEdit.text.toString())
            }
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show()
        }
        .setNegativeButton("取消") { _, _ ->
            Toast.makeText(this, "取消覆盖", Toast.LENGTH_SHORT).show()
        }
        .show()
} else {
    prefs.edit {
        putString("app_id", binding.appIdEdit.text.toString())
        putString("app_secret", binding.appSecretEdit.text.toString())
    }
    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show()
}
```

设置进入按钮和获取密钥按钮的监听器，此两个按钮将调用对应 Activity 中的`actionStart`以切换Activity。

```kotlin
binding.btnEnter.setOnClickListener {
    MainActivity.actionStart(this)
}
binding.getSecretBtn.setOnClickListener {
    WebViewActivity.actionStart(this, "http://blog.mxnzp.com/?p=59#toc-4")
}
```

其中`WebViewActivity`的`actionStart`接收一个url.

### 2. WebViewActivity

此Activity中仅将Intent中传来的url在WebView中进行可视化。

```kotlin
binding.webView.settings.javaScriptEnabled = true
binding.webView.webViewClient = WebViewClient()
intent.getStringExtra("url")?.let { binding.webView.loadUrl(it) }
```

### 3. MainActivity

`activity_main.xml` 中包含一个`TabLayout`和一个`ViewPager2`

设置视图之后，给viewPager2设置`FragmentStateAdapter`, 实现左右滑动切换不同功能:

```kotlin
binding.viewPager2.adapter = object : FragmentStateAdapter(this) {
    override fun getItemCount() = 2
    override fun createFragment(position: Int) = when (position) {
        0 -> WeatherJsonFragment()
        else -> WeatherFragment()
    }
}
TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
    tab.text = when (position) {
        0 -> "天气Json数据"
        else -> "天气数据展示"
    }
}.attach()
```

MainActivity中的companion object :

```kotlin
companion object {
    fun actionStart(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }
}
```

### 4. WeatherFragment

在`WeatherFragment`中，首先通过getSharedPreferences获取到app_id 和 app_secret 后, 发起http请求通过ip地址获取到当前所在的城市, 之后以城市为查询条件发起请求,得到具体的天气数据.

```kotlin
val retrofit1 = Retrofit.Builder()
    .baseUrl("https://www.mxnzp.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
val retrofit2 = Retrofit.Builder()
    .baseUrl("https://jisutqybmf.market.alicloudapi.com")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
val ipService = retrofit1.create(IpService::class.java)
val weatherService = retrofit2.create(WeatherService::class.java)
```

```kotlin
interface WeatherService {
    @GET("/weather/query")
    fun getWeatherInfo(@Query("city") city: String): Call<WeatherResult>
}
```

```kotlin
interface IpService {
    @GET("/api/ip/self")
    fun getRealAddress(
        @Query("app_id") app_id: String, @Query("app_secret") app_secret: String): Call<IpResult>
}
```

返回的JSON数据通过Gson自动转换成数据类对象之后将数据显示到界面上:

```kotlin
if (jsonWeather?.msg == "ok") {
    val weather = jsonWeather.result
    binding.City.text = weather.city
    binding.currentWeather.text = weather.weather
    binding.currentTemperature.text = "${weather.temp}℃"
    // 修改binding.currentWeatherIcon的图标
    binding.currentWeatherIcon.setImageResource(icons[weather.img]!!)
    val week = weather.daily
    binding.dailyForecastInfoList1Icon.setImageResource(icons[week[0].day.img]!!)
    binding.dailyForecastInfoList1Weather.text = week[0].day.weather
    binding.dailyForecastInfoList1Temperature.text =
        "${week[0].day.temphigh}°/${week[0].night.templow}°"
    binding.dailyForecastInfoList2Icon.setImageResource(icons[week[1].day.img]!!)
    binding.dailyForecastInfoList2Weather.text = week[1].day.weather
    binding.dailyForecastInfoList2Temperature.text =
        "${week[1].day.temphigh}°/${week[1].night.templow}°"
    binding.dailyForecastInfoList3Icon.setImageResource(icons[week[2].day.img]!!)
    binding.dailyForecastInfoList3Date.text = week[2].week
    binding.dailyForecastInfoList3Weather.text = week[2].day.weather
    binding.dailyForecastInfoList3Temperature.text =
        "${week[2].day.temphigh}°/${week[2].night.templow}°"
}
```

图标icons对应关系通过map存储到类属性中

```kotlin
private val icons = mapOf(
    "0" to R.drawable.qing,
    "1" to R.drawable.duoyun,
    "2" to R.drawable.yin,
    "3" to R.drawable.zhenyu,
    "4" to R.drawable.leizhenwu,
    "5" to R.drawable.leizhenyubanbingbao,
    "6" to R.drawable.yujiaxue,
    "7" to R.drawable.xiaoyu,
    "8" to R.drawable.zhongyu,
    ...
    )
```

### 5. WeatherJsonFragment

此Fragment中开了一个线程进行http请求之后将返回结果显示到屏幕上，作为返回示例

```kotlin
thread {
    try {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://jisutqybmf.market.alicloudapi.com/weather/query?city=邵东市")
            .build()
        val response = client.newCall(request).execute()
        val result = response.body()?.string()
        result?.let {
            binding.jsonText.text = it
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
```

### 6. entity

数据类：

![](https://zxastaticpages.oss-cn-beijing.aliyuncs.com/blogpictures/202205181132985.png)

### 7. JSON返回数据格式

ip查询：

```json
{
    "code": 1,
    "msg": "数据返回成功",
    "data": {
        "ip": "119.123.72.166",
        "province": "广东省",
        "provinceId": 440000,
        "city": "深圳市",
        "cityId": 440300,
        "isp": "电信",
        "desc": "广东省深圳市 电信"
    }
}
```

天气查询：

```json
{
  "status": "0",
  "msg": "ok",
  "result": {
    "city": "安顺",
    "cityid": "111",
    "citycode": "101260301",
    "date": "2015-12-22",
    "week": "星期二",
    "weather": "多云",
    "temp": "16",
    "temphigh": "18",
    "templow": "9",
    "img": "1",
    "humidity": "55",
    "pressure": "879",
    "windspeed": "14.0",
    "winddirect": "南风",
    "windpower": "2级",
    "updatetime": "2015-12-22 15:37:03",
    "index": [
      {
        "iname": "空调指数",
        "ivalue": "较少开启",
        "detail": "您将感到很舒适，一般不需要开启空调。"
      },
      {
        "iname": "运动指数",
        "ivalue": "较适宜",
        "detail": "天气较好，无雨水困扰，较适宜进行各种运动，但因气温较低，在户外运动请注意增减衣物。"
      }
    ],
    "aqi": {
      "so2": "37",
      "so224": "43",
      "no2": "24",
      "no224": "21",
      "co": "0.647",
      "co24": "0.675",
      "o3": "26",
      "o38": "14",
      "o324": "30",
      "pm10": "30",
      "pm1024": "35",
      "pm2_5": "23",
      "pm2_524": "24",
      "iso2": "13",
      "ino2": "13",
      "ico": "7",
      "io3": "9",
      "io38": "7",
      "ipm10": "35",
      "ipm2_5": "35",
      "aqi": "35",
      "primarypollutant": "PM10",
      "quality": "优",
      "timepoint": "2015-12-09 16:00:00",
      "aqiinfo": {
        "level": "一级",
        "color": "#00e400",
        "affect": "空气质量令人满意，基本无空气污染",
        "measure": "各类人群可正常活动"
      }
    },
    "daily": [
      {
        "date": "2015-12-22",
        "week": "星期二",
        "sunrise": "07:39",
        "sunset": "18:09",
        "night": {
          "weather": "多云",
          "templow": "9",
          "img": "1",
          "winddirect": "无持续风向",
          "windpower": "微风"
        },
        "day": {
          "weather": "多云",
          "temphigh": "18",
          "img": "1",
          "winddirect": "无持续风向",
          "windpower": "微风"
        }
      }
    ],
    "hourly": [
      {
        "time": "16:00",
        "weather": "多云",
        "temp": "14",
        "img": "1"
      },
      {
        "time": "17:00",
        "weather": "多云",
        "temp": "13",
        "img": "1"
      }
    ]
  }
}
```
