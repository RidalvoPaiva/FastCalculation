\\ Criando uma nova (ResultFragment) para mostrar o resultado do jogo, em seguida adicionar um botão para reiniciar o jogo no (GameFragment)
com as configurações definidas anteriormente na aula do Prof. Pedro.\\

01. \\Criando a nova ResultFragment\\
\\Esse fragmento tem a função de exibir o resultado e passar o valor como argumento\\. 
====================================================================================================================================================================
====================================================================================================================================================================

class ResultFragment : Fragment() {
    private lateinit var fragmentResultBinding: FragmentResultBinding
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

=========================================================================================================================================================================
=========================================================================================================================================================================
02. \\Modificando o GameFragment para navegar até o ResultFragment\\
\\Para isso, modifiquei o (GameFragment) para que, ao final do jogo, ele navegue até o (ResultFragment), passando os pontos como argumento.\\

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

==================================================================================================================================================
==================================================================================================================================================
03\\ Layout para o (ResultFragment)
\\Segue o exemplo de código para o fragmento de resultado (fragment_result.xml):

<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical">

    <TextView
        android:id="@+id/resultTv"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="24sp"
        android:text="Your Score: 0" />

    <Button
        android:id="@+id/restartBt"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Restart Game" />
</LinearLayout>

=======================================================================================================================================================================
\\RESUMO\\:
    I >>> Foi criado o (ResultFragment) para mostrar o resultado <<<.
   II >>> Adicionamos um botão para reiniciar o jogo com as configurações existentes <<<.
   III >>> Modificamos o (GameFragment) para navegar para o (ResultFragment) ao final do jogo, passando os pontos como argumento <<<.
   IV >>> Agora,o aplicativo irá mostrar o resultado em uma nova tela e reiniciar o jogo ao clicar no botão <<<.
