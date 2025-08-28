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

class GameFragment : Fragment() {
    private lateinit var fragmentGameBinding: FragmentGameBinding

    private lateinit var settings: Settings
    private lateinit var calculationGame: CalculationGame
    private var currentRound: CalculationGame.Round? = null
    private var startRoundTimer = 0L
    private var totalGameTime = 0L
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
                totalGameTime += System.currentTimeMillis() - startRoundTimer
                hits++
            } else {
                totalGameTime += settings.roundInterval
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
                roundTv.text = "Round: ${currentRound!!.round}/${settings.rounds}"
                questionTv.text = currentRound!!.question
                alternativeOneBt.text = currentRound!!.alt1.toString()
                alternativeTwoBt.text = currentRound!!.alt2.toString()
                alternativeThreeBt.text = currentRound!!.alt3.toString()
            }
            // (Re)inicia o cronômetro do round
            startRoundTimer = System.currentTimeMillis()
            // Evita múltiplos agendamentos
            roundDeadLineHandler.removeMessages(MSG_ROUND_DEADLINE)
            roundDeadLineHandler.sendEmptyMessageDelayed(MSG_ROUND_DEADLINE, settings.roundInterval)
        } else {
            // Acabaram os rounds
            roundDeadLineHandler.removeMessages(MSG_ROUND_DEADLINE)

            val totalTimeInSeconds = totalGameTime / 1000L

            val points: Float = if (totalTimeInSeconds > 0L) {
                (hits.toFloat() * 10f) / totalTimeInSeconds.toFloat()
            } else {
                0f
            }

            // Usa o container do próprio fragment para fazer o replace (evita erro de R.id.* inexistente)
            parentFragmentManager.beginTransaction()
                .replace(this@GameFragment.id, ResultFragment.newInstance(points, settings))
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        roundDeadLineHandler.removeMessages(MSG_ROUND_DEADLINE)
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
