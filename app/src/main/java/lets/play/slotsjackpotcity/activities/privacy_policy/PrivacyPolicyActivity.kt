package lets.play.slotsjackpotcity.activities.privacy_policy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import lets.play.slotsjackpotcity.R
import lets.play.slotsjackpotcity.activities.game.GameActivity

class PrivacyPolicyActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_privacy_policy)
        findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            finish()
        }
    }
}