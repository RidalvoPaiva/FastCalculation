package br.edu.ifsp.scl.fastcalculation

import android.content.Intent
import android.os.Bundle
import android.view.TextureView
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.ifsp.scl.fastcalculation.Extras.EXTRA_SETTINGS
import br.edu.ifsp.scl.fastcalculation.R.string.settings
import br.edu.ifsp.scl.fastcalculation.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private val activitySettingsBinding: ActivitySettingsBinding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activitySettingsBinding.root)

        setSupportActionBar(activitySettingsBinding.gameTbIn.gameTb)
        supportActionBar?.subtitle = getString(R.string.app_name)
        supportActionBar?.subtitle = getString(settings)
        activitySettingsBinding.apply {
            letsGoBt.setOnClickListener {
                val settings = Settings(
                    playerNameEt.text.toString(),
                    (roundsSP.selectedView as TextureView).toString().toInt(),
                    roundIntervalRg.run {
                        findViewById<RadioButton>(checkedRadioButtonId).text.toString().toLong() * 1000L

                    }
                )
                val gameActivityIntent = Intent(this@SettingsActivity,GameActivity::class.java)
                gameActivityIntent.putExtra(EXTRA_SETTINGS, settings)
                startActivity(gameActivityIntent)
                finish()

            }

        }

        }
    }
