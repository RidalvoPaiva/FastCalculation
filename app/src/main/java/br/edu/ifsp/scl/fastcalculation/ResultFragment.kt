package br.edu.ifsp.scl.fastcalculation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ResultFragment : Fragment() {private lateinit var fragmentResultBinding: FragmentResultBinding
    private var points: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            points = it.getFloat(ARG_POINTS)
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
            .replace(R.id.container, GameFragment.newInstance(settings))
            .commit()
    }

    companion object {
        private const val ARG_POINTS = "points"
        private lateinit var settings: Settings

        @JvmStatic
        fun newInstance(points: Float, settings: Settings) =
            ResultFragment().apply {
                arguments = Bundle().apply {
                    putFloat(ARG_POINTS, points)
                    this@Companion.settings = settings
                }
            }
    }
}



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}