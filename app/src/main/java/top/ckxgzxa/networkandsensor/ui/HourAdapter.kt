package top.ckxgzxa.networkandsensor.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import top.ckxgzxa.networkandsensor.R
import top.ckxgzxa.networkandsensor.databinding.HourItemBinding
import top.ckxgzxa.networkandsensor.entity.weather.Hourly

class HourAdapter(val hours: List<Hourly>): RecyclerView.Adapter<HourAdapter.ViewHolder>(){

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

    // viewBinding
    private var _binding: HourItemBinding? = null
    // get binding
    val binding get() = _binding!!

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val time: TextView = view.findViewById(R.id.time)
        val icon: ImageView = view.findViewById(R.id.weather_icon)
        val weather: TextView = view.findViewById(R.id.weather)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = HourItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hour = hours[position]
        holder.time.text = hour.time
        holder.icon.setImageResource(icons[hour.img]!!)
        holder.weather.text = hour.weather

    }

    override fun getItemCount() = hours.size

}