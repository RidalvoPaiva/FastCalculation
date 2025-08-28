package br.edu.ifsp.scl.fastcalculation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.edu.ifsp.scl.fastcalculation.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private lateinit var fragmentResultBinding: FragmentResultBinding
    private var points: Float = 0f
    private lateinit var settings: Settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            points = it.getFloat(ARG_POINTS)
            settings = it.getParcelable(EXTRA_SETTINGS) ?: Settings()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentResultBinding = FragmentResultBinding.inflate(inflater, container, false)

        fragmentResultBinding.apply {
            resultTv.text = getString(R.string.points, points)
            restartBt.setOnClickListener {
                restartGame()
            }
        }

        return fragmentResultBinding.root
    }

    private fun restartGame() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.gameFl, GameFragment.newInstance(settings))
            .commit()
    }

    companion object {
        private const val ARG_POINTS = "points"
        private const val EXTRA_SETTINGS = "settings"

        @JvmStatic
        fun newInstance(points: Float, settings: Settings) =
            ResultFragment().apply {
                arguments = Bundle().apply {
                    putFloat(ARG_POINTS, points)
                    putParcelable(EXTRA_SETTINGS, settings)
                }
            }
    }
}
