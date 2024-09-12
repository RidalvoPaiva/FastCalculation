package br.edu.ifsp.scl.fastcalculation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import br.edu.ifsp.scl.fastcalculation.Extras.EXTRA_SETTINGS
import br.edu.ifsp.scl.fastcalculation.databinding.FragmentGameBinding
import br.edu.ifsp.scl.fastcalculation.databinding.FragmentWelcomeBinding
import br.edu.scl.ifsp.sdm.fastcalculation.CalculationGame


class GameFragment : Fragment() {
    private lateinit var fragmentGameBinding: FragmentGameBinding

    private lateinit var settings: Settings
    private lateinit var calculationGame: CalculationGame
    private var currentRound: CalculationGame.Round? = null
    private var startRoundTimer = 0L
    private var totalGame = 0L
    private var hits = 0
    private val roundDeadLineHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            totalGameTime += settings.roundInterval
            play()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            settings = it.getParcelable(EXTRA_SETTINGS) ?: Settings()
        }
        calculationGame = CalculationGame(settings.rounds)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentGameBinding = FragmentGameBinding.inflate(inflater, container, false)
        val onClickListener = View.OnClickListener {
            val value = (it as Button).text.toString().toInt()
            if (value == currentRound?.answer) {
                var totalGameTime = System.currentTimeMillis() - startRoundTimer
                hits++
            } else {
                var totalGameTime = +settings.roundInterval
                hits--
            }
            play()

        }
        fragmentGameBinding.apply {
            alternativeOneBt.setOnClickListener(onClickListener)
            alternativeTwoBt.setOnClickListener(onClickListener)
            alternativeThreeBt.setOnClickListener(onClickListener)
        }
        roundDeadLineHandler.removeMessages(MSG_ROUND_DEADLINE)
        play()

        return fragmentGameBinding.root
    }

    private fun play() {
        currentRound = calculationGame.nextRound()
        if (currentRound != null) {
            fragmentGameBinding.apply {
                "Round: ${currentRound!!.round}/${settings.rounds}".also {
                    roundTv.text = it
                }
                questionTv.text = currentRound!!.question
                alternativeOneBt.text = currentRound!!.alt1.toString()
                alternativeTwoBt.text = currentRound!!.alt2.toString()
                alternativeThreeBt.text = currentRound!!.alt3.toString()
            }
            startRoundTimer = System.currentTimeMillis()
            roundDeadLineHandler.sendEmptyMessageDelayed(MSG_ROUND_DEADLINE, settings.roundInterval)
        } else {
            val totalTimeInSeconds = totalGameTime / 1000L
            val points = hits * 10f / totalTimeInSeconds

            // Navega para o fragmento de resultado
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, ResultFragment.newInstance(points, settings))
                .commit()
        }
    }
}
 
    companion object {
        private const val MSG_ROUND_DEADLINE = 0
        @JvmStatic
        fun newInstance(settings: Settings) =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_SETTINGS, settings)

                }
            }
    }

}
