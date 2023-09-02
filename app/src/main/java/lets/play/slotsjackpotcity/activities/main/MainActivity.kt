package lets.play.slotsjackpotcity.activities.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import lets.play.slotsjackpotcity.R
import lets.play.slotsjackpotcity.activities.game.GameActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ImageButton>(R.id.play_button).setOnClickListener {
            val gameIntent = Intent(this, GameActivity::class.java)
            startActivity(gameIntent)
        }
    }
}