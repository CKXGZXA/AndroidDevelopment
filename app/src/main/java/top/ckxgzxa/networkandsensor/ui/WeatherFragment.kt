package top.ckxgzxa.networkandsensor.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import top.ckxgzxa.networkandsensor.R
import top.ckxgzxa.networkandsensor.databinding.FragmentWeatherBinding
import top.ckxgzxa.networkandsensor.entity.ip.IpResult
import top.ckxgzxa.networkandsensor.entity.weather.Hourly
import top.ckxgzxa.networkandsensor.entity.weather.WeatherResult
import top.ckxgzxa.networkandsensor.service.IpService
import top.ckxgzxa.networkandsensor.service.WeatherService


/**
 * A simple [Fragment] subclass.
 * Use the [WeatherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WeatherFragment : Fragment() {

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
        "9" to R.drawable.dayu,
        "10" to R.drawable.baoyu,
        "11" to R.drawable.dabaoyu,
        "12" to R.drawable.tedabaoyu,
        "13" to R.drawable.zhenxue,
        "14" to R.drawable.xiaoxue,
        "15" to R.drawable.zhongxue,
        "16" to R.drawable.daxue,
        "17" to R.drawable.baoxue,
        "18" to R.drawable.wu,
        "19" to R.drawable.dongyu,
        "20" to R.drawable.shachenbao,
        "21" to R.drawable.xiaoyu_zhongyu,
        "22" to R.drawable.zhongyu_dayu,
        "23" to R.drawable.dayu_baoyu,
        "24" to R.drawable.baoyu_dabaoyu,
        "25" to R.drawable.dabaoyu_tedabaoyu,
        "26" to R.drawable.xiaoxue_zhongxue,
        "27" to R.drawable.zhongxue_daxue,
        "28" to R.drawable.daxue_baoxue,
        "29" to R.drawable.fuchen,
        "30" to R.drawable.yangsha,
        "31" to R.drawable.qiangshachenbao,
        "32" to R.drawable.nongwu,
        "49" to R.drawable.qiangnongwu,
        "53" to R.drawable.mai,
        "54" to R.drawable.zhongdumai01,
        "55" to R.drawable.zhongdumai02,
        "56" to R.drawable.yanzhongmai,
        "57" to R.drawable.dawu,
        "58" to R.drawable.teqiangnongwu,
        "99" to R.drawable.no_state,
        "301" to R.drawable.yu,
        "302" to R.drawable.xue
    )

    private var _binding: FragmentWeatherBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        icons["3"]
        Log.d("WeatherFragment", "onCreate")



        // 通过当前ip获取所在市
        // 从sharedPreferences中获取密钥
        val prefs = activity?.getSharedPreferences("app_config", Context.MODE_PRIVATE)
        if ((prefs != null) && prefs.contains("app_id") && prefs.contains("app_secret")) {
            val appId = prefs.getString("app_id", "").toString()
            val appSecret = prefs.getString("app_secret", "").toString()

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

            // 协程操作
            // 异步获取ip

            ipService.getRealAddress(appId, appSecret)
                .enqueue(object : Callback<IpResult> {
                    override fun onResponse(call: Call<IpResult>, response: Response<IpResult>) {
                        val jsonData = response.body()
                        var city = ""
                        // 输出jsonData
                        println(jsonData.toString())
                        if (jsonData?.code == 1) {
                            city = jsonData.data.city
                        }
                        // 获取天气
                        weatherService.getWeatherInfo(city)
                            .enqueue(object : Callback<WeatherResult> {
                                override fun onResponse(
                                    call: Call<WeatherResult>,
                                    response: Response<WeatherResult>
                                ) {
                                    val jsonWeather = response.body()
                                    // 输出jsonWeather
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
                                }
                                override fun onFailure(call: Call<WeatherResult>, t: Throwable) {
                                    t.printStackTrace()
                                }
                            })
                    }

                    override fun onFailure(call: Call<IpResult>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}