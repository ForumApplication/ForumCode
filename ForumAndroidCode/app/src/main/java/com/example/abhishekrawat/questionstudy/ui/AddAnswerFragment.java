package com.example.abhishekrawat.questionstudy.ui;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhishekrawat.questionstudy.Model.AnswerDTO;
import com.example.abhishekrawat.questionstudy.R;
import com.example.abhishekrawat.questionstudy.presenter.AddAnswerPresenter;


public class AddAnswerFragment extends BottomSheetDialogFragment implements View.OnClickListener,AddAnswerView {

    ProgressBar mProgressBar;
    EditText mAnswer;
    private AddAnswerFragmentListener mListener;
    private BottomSheetBehavior mBehavior;
    private AppBarLayout app_bar_layout;
    String questionId;
    private static String QUESTION_ID="question_id";



    public static AddAnswerFragment getInstance(String questionId) {
        AddAnswerFragment fragment = new AddAnswerFragment();
        Bundle bundle=new Bundle();
        bundle.putString(QUESTION_ID,questionId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View view = View.inflate(getContext(), R.layout.fragment_add_answer, null);
        dialog.setContentView(view);
        view.findViewById(R.id.save_answer_btn).setOnClickListener(this);
        mAnswer=view.findViewById(R.id.answer_text);
        mProgressBar=view.findViewById(R.id.answer_progress_bar);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                }
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) { }

                if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                    dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        return dialog;
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (AddAnswerFragmentListener) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.save_answer_btn:
                mProgressBar.setVisibility(View.VISIBLE);
                AddAnswerPresenter presenter=new AddAnswerPresenter(this);
                AnswerDTO answer=new AnswerDTO();
                questionId=this.getArguments().getString(QUESTION_ID);
                answer.answer=mAnswer.getText().toString();
                answer.questionId=questionId;
                presenter.SaveAnswer(getContext(),answer);
                break;
        }
    }

    @Override
    public void onError(String message) {
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess() {
        this.dismiss();
        Fragment fragment =getFragmentManager().getFragments().get(0);
        if (fragment != null && fragment instanceof QuestionThreadFragment) {
            QuestionThreadFragment qtf = (QuestionThreadFragment) fragment;
            qtf.refreshAnswers();
        }
        Toast.makeText(getContext(),getString(R.string.answer_saved),Toast.LENGTH_LONG).show();
    }

    public interface AddAnswerFragmentListener {
        void showProgressBar();
        void hideProgressBar();
     }
}
