package br.com.igti.android.futebolquiz;

import android.animation.Animator;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import static android.content.ContentValues.TAG;


public class FutebolQuizFragment extends Fragment {

    private static final String KEY_INDICE = "indice";
    public static final String ARG_ID_FUTEBOL = "futebol_id";

    private Button mBotaoVerdade;
    private Button mBotaoFalso;
    private TextView mConteudoCard;
    private CardView mCardView;
    private int mIndiceAtual = 0;

    private Pergunta[] mPerguntas = new Pergunta[]{
            new Pergunta(R.string.cardview_conteudo_joinville, true),
            new Pergunta(R.string.cardview_conteudo_cruzeiro, false),
            new Pergunta(R.string.cardview_conteudo_gremio, false)
    };

    private View.OnClickListener mBotaoVerdadeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checaResposta(true, v.getContext());
            mIndiceAtual = (mIndiceAtual + 1) % mPerguntas.length;
            atualizaQuestao();
            revelaCard();
        }
    };

    private View.OnClickListener mBotaoFalsoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checaResposta(false, v.getContext());
            mIndiceAtual = (mIndiceAtual + 1) % mPerguntas.length;
            atualizaQuestao();
            revelaCard();
        }
    };

    public static FutebolQuizFragment newInstance(UUID eventoId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ID_FUTEBOL, eventoId);

        FutebolQuizFragment fragment = new FutebolQuizFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private void atualizaQuestao() {
        int questao = mPerguntas[mIndiceAtual].getQuestao();
        mConteudoCard.setText(questao);
    }

    private void revelaCard() {
        //criando um reveal circular
        Animator animator = ViewAnimationUtils.createCircularReveal(
                mCardView,
                0,
                0,
                0,
                (float) Math.hypot(mCardView.getWidth(), mCardView.getHeight()));

        // interpolador ease-in/ease-out.
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

    private void checaResposta(boolean botaoPressionado, Context context) {
        boolean resposta = mPerguntas[mIndiceAtual].isQuestaoVerdadeira();

        int recursoRespostaId = 0;

        if (botaoPressionado == resposta) {
            new AudioPlayer().play(context, R.raw.cashregister);
            recursoRespostaId = R.string.toast_acertou;
        } else {
            new AudioPlayer().play(context, R.raw.buzzer);
            recursoRespostaId = R.string.toast_errou;
        }
        Toast.makeText(this.getActivity().getBaseContext(), recursoRespostaId, Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_futebol_fragment, container, false);

        if (savedInstanceState != null) {
            mIndiceAtual = savedInstanceState.getInt(KEY_INDICE, 0);
        }

        mBotaoVerdade = (Button) rootView.findViewById(R.id.botaoVerdade);
        mBotaoVerdade.setOnClickListener(mBotaoVerdadeListener);
        mBotaoFalso = (Button) rootView.findViewById(R.id.botaoFalso);
        mBotaoFalso.setOnClickListener(mBotaoFalsoListener);

        mConteudoCard = (TextView) rootView.findViewById(R.id.cardviewConteudo);
        atualizaQuestao();

        mCardView = (CardView) rootView.findViewById(R.id.cardview);

        Log.d(TAG, "onCreate()");
        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSavedInstanceState");
        outState.putInt(KEY_INDICE, mIndiceAtual);
    }

}