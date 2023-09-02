package lets.play.slotsjackpotcity.activities.loading

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import lets.play.slotsjackpotcity.R
import lets.play.slotsjackpotcity.activities.main.MainActivity
import lets.play.slotsjackpotcity.activities.network.NetworkActivity
import lets.play.slotsjackpotcity.network.CheckLink

class LoadingActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        lifecycleScope.launch {
            val urlFromFile = getSharedPreferences("url_file", MODE_PRIVATE).getString("url", null)
            if(urlFromFile != null) {
                val networkIntent = Intent(this@LoadingActivity, NetworkActivity::class.java)
                networkIntent.putExtra("url", urlFromFile)
                startActivity(networkIntent)
                finish()
            }
            else {
                CheckLink().check(this@LoadingActivity) {
                    Log.i("Loading activity", "${it.get("let_in")} ${it.get("url")}")
                    val letIn = it.getBoolean("let_in")
                    val url = it.get("url")
                    if (letIn && url is String) {
                        val networkIntent =
                            Intent(this@LoadingActivity, NetworkActivity::class.java)
                        getSharedPreferences("url_file", MODE_PRIVATE).edit().putString("url", url).apply()
                        networkIntent.putExtra("url", url)
                        startActivity(networkIntent)
                        finish()
                    } else {
                        val mainIntent = Intent(this@LoadingActivity, MainActivity::class.java)
                        startActivity(mainIntent)
                        finish()
                    }
                }
            }
        }
    }
}