package lets.play.slotsjackpotcity.activities.game

import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import lets.play.slotsjackpotcity.R
import lets.play.slotsjackpotcity.activities.main.MainActivity
import lets.play.slotsjackpotcity.activities.privacy_policy.PrivacyPolicyActivity
import kotlin.random.Random

class GameActivity: AppCompatActivity() {
    private val bet = MutableLiveData(1)
    private val maxBet = MutableLiveData(1)
    private val total = MutableLiveData(2000000)
    private val isAutoSpin = MutableLiveData(false)
    private val musicState = MutableLiveData(false)
    private var spinning = false

    private val temp = MutableLiveData(true)

    private lateinit var slots: List<ImageView>
    private var slotsValue = List(18) {Random.nextInt(1, 5)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        slots = listOf(
            findViewById(R.id.slot1),
            findViewById(R.id.slot2),
            findViewById(R.id.slot3),
            findViewById(R.id.slot4),
            findViewById(R.id.slot5),
            findViewById(R.id.slot6),
            findViewById(R.id.slot7),
            findViewById(R.id.slot8),
            findViewById(R.id.slot9),
            findViewById(R.id.slot10),
            findViewById(R.id.slot11),
            findViewById(R.id.slot12),
            findViewById(R.id.slot13),
            findViewById(R.id.slot14),
            findViewById(R.id.slot15),
            findViewById(R.id.slot16),
            findViewById(R.id.slot17),
            findViewById(R.id.slot18))

        findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            finish()
        }

        findViewById<ImageButton>(R.id.plus_bet_button).setOnClickListener {
            maxBet.value?.let { mb ->
                bet.value?.let {b ->
                    if(mb > b) {
                        bet.value = b + 1
                    }
                }
            }
        }
        findViewById<ImageButton>(R.id.plus_max_bet_button).setOnClickListener {
            maxBet.value?.let { mb ->
                    maxBet.value = mb + 1
            }
        }

        findViewById<ImageButton>(R.id.minus_bet_button).setOnClickListener {
            bet.value?.let { b ->
                bet.value = b + 1
            }
        }

        findViewById<ImageButton>(R.id.minus_max_bet_button).setOnClickListener {
            maxBet.value?.let { mb ->
                maxBet.value = mb - 1
            }
        }

        fun TextView.setGradient(): TextView {
            paint.shader = LinearGradient(
                width.toFloat() / 2f, 0f, width.toFloat() / 2f, textSize, intArrayOf(
                    0xFFDDDDE9.toInt(), 0xFF6D6DFF.toInt()
                ), null, Shader.TileMode.CLAMP)
            return this
        }

        val betText =  findViewById<TextView>(R.id.bet_text).setGradient()
        val maxBetText = findViewById<TextView>(R.id.max_bet_text).setGradient()
        val totalText = findViewById<TextView>(R.id.total_text).setGradient()

        bet.observe(this) {
            betText.text = it.toString()
        }
        maxBet.observe(this) {
            maxBetText.text = it.toString()
        }
        total.observe(this) {
            totalText.text = it.toString()
        }

        findViewById<ImageButton>(R.id.privacy_policy_button).setOnClickListener {
            val privacyPolicyIntent = Intent(this, PrivacyPolicyActivity::class.java)
            startActivity(privacyPolicyIntent)
        }

        val spinButton = findViewById<ImageButton>(R.id.spin_button)

        spinButton.setOnClickListener {
            if(isAutoSpin.value == false && !spinning) {
                doSpin(spinButton)
            }
        }

        findViewById<ImageButton>(R.id.auto_spin_button).setOnClickListener {

            isAutoSpin.value = !(isAutoSpin.value ?: true)
            if(isAutoSpin.value == true && !spinning) {
                doSpin(spinButton)
            }
        }
        temp.observe(this) {
            Log.i("Is enabled", it.toString())
            spinButton.isEnabled = it
        }
    }

    private fun doSpin(spinButton: ImageButton?) {
        temp.value = false
//        spinButton?.isEnabled = false
        total.value?.let { t ->
            bet.value?.let { b ->
                lifecycleScope.launch {
                    var isAutoSpin = isAutoSpin.value ?: false
                    do {
                        if (t >= b && !spinning) {
                            spinning = true
                            total.value = t - b
                            repeat(30) {
                                val newList = mutableListOf<Int>()
                                for (slot in slots) {
                                    val rnd = Random.nextInt(1, 5)
                                    slot.setImageResource(
                                        when (rnd) {
                                            1 -> R.drawable.diamond
                                            2 -> R.drawable.ball
                                            3 -> R.drawable.girl
                                            else -> R.drawable.wild
                                        }
                                    )
                                    newList.add(rnd)
                                }
                                slotsValue = newList
                                delay(150)
                            }
                            val slots1 = slotsValue.take(3)
                            val slots2 = slotsValue.drop(3).take(3)
                            val slots3 = slotsValue.drop(6).take(3)
                            val slots4 = slotsValue.drop(9).take(3)
                            val slots5 = slotsValue.drop(12).take(3)
                            val slots6 = slotsValue.drop(15).take(3)
                            var cash = 0
                            for (i in 1..3) {
                                if (!slots1.contains(i).or(slots1.contains(4))) continue
                                if (!slots2.contains(i).or(slots2.contains(4))) continue
                                if (!slots3.contains(i).or(slots3.contains(4))) continue
                                if (!slots4.contains(i).or(slots4.contains(4))) continue
                                if (!slots5.contains(i).or(slots5.contains(4))) continue
                                if (!slots6.contains(i).or(slots6.contains(4))) continue
                                cash += (i * 10) * b + (if (b >= 250) 250 else 0) + (if (b >= 1000) 1000 else 0) + (if (b >= 2500) 2500 else 0) + (if (b >= 5000) 5000 else 0)
                            }
                            total.value = (t - b) + cash
                            isAutoSpin = this@GameActivity.isAutoSpin.value ?: false
                            if(isAutoSpin) {
                                delay(100)
                            }
//                            spinButton?.isEnabled = true
                            temp.value = true
                            spinning = false
                        }
                        else {
                            break
                        }
                    } while(isAutoSpin)
                    temp.value = true
                    spinning = false
//                    spinButton?.isEnabled = true
                }
            }
        }

    }
}