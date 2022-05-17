package top.ckxgzxa.networkandsensor

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import top.ckxgzxa.networkandsensor.databinding.ActivityEnterBinding

class EnterActivity : BaseActivity() {

    // viewBinding
    private lateinit var binding: ActivityEnterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("app_config", MODE_PRIVATE)
        prefs.apply {
            binding.appIdEdit.setText(getString("app_id", ""))
            binding.appSecretEdit.setText(getString("app_secret", ""))
        }


        binding.btnSave.setOnClickListener {
            if (binding.appIdEdit.text.isNullOrEmpty() || binding.appSecretEdit.text.isNullOrEmpty()) {
                Toast.makeText(this, "请输入正确的app_id和app_secret", Toast.LENGTH_SHORT).show()
            } else {
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
            }
        }

        binding.btnEnter.setOnClickListener {
            MainActivity.actionStart(this)
        }

        binding.getSecretBtn.setOnClickListener {
            WebViewActivity.actionStart(this, "http://blog.mxnzp.com/?p=59#toc-4")
        }
    }
}