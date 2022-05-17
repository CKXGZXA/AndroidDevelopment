package top.ckxgzxa.networkandsensor.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import top.ckxgzxa.networkandsensor.databinding.FragmentWeatherJsonBinding
import top.ckxgzxa.networkandsensor.entity.weather.WeatherResult
import top.ckxgzxa.networkandsensor.service.WeatherService
import kotlin.concurrent.thread

class WeatherJsonFragment : Fragment() {

    // viewBinding
    private var _binding: FragmentWeatherJsonBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://jisutqybmf.market.alicloudapi.com/weather/query?city=邵东市")
                    .addHeader("Authorization", "APPCODE 3eca59e22c85413cb3798167a61a904f")
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherJsonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}