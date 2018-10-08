package com.example.abhishekrawat.questionstudy.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.abhishekrawat.questionstudy.Adapter.AnswerRecyclerViewAdapter;
import com.example.abhishekrawat.questionstudy.Adapter.FilesRecyclerViewAdapter;
import com.example.abhishekrawat.questionstudy.Model.AnswerDTO;
import com.example.abhishekrawat.questionstudy.Model.MediaFilesDTO;
import com.example.abhishekrawat.questionstudy.Model.QuestionDTO;
import com.example.abhishekrawat.questionstudy.R;
import com.example.abhishekrawat.questionstudy.presenter.QuestionThreadPresenter;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class QuestionThreadFragment extends Fragment implements QuestionThreadView {
    TextView questionTitle,questionDescription,userName,createTime;
    QuestionDTO question;
    AnswerRecyclerViewAdapter arvAdapter;
    RecyclerView answerRecyclerView,questionImageRecyclerView;
    QuestionThreadListener mListener;
    RelativeLayout imageContainer;
    ImageView moreImage;
    private static String QUESTION="question";
    public static QuestionThreadFragment getInstance(QuestionDTO question) {
        QuestionThreadFragment fragment = new QuestionThreadFragment();
        Bundle args = new Bundle();
        args.putSerializable(QUESTION,question);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question_thread, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        question=(QuestionDTO) this.getArguments().getSerializable(QUESTION);
        questionTitle=view.findViewById(R.id.question_title);
        questionDescription=view.findViewById(R.id.question_description);
        userName=view.findViewById(R.id.user_name);
        createTime=view.findViewById(R.id.create_time);
        imageContainer=view.findViewById(R.id.image_list_container);
        moreImage=view.findViewById(R.id.more_images);



        questionTitle.setText(question.question);
        questionDescription.setText(question.description);
        userName.setText(question.user.name);
        createTime.setText(question.date);
        answerRecyclerView=(RecyclerView) view.findViewById(R.id.answer_recycler_view);
        questionImageRecyclerView=(RecyclerView) view.findViewById(R.id.question_image_recycler_view);

        if(question.mediaUrl.size()>0)
        {
            imageContainer.setVisibility(View.VISIBLE);
            if(question.mediaUrl.size()>1)
            {moreImage.setVisibility(View.VISIBLE);

            }
            /*Glide.with(getContext())
                    .load(question.mediaUrl.get(0).url)
                    .into(questionImage)
            ;*/
            setImages(question.mediaUrl);
        }

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheetAddAnswer();
            }
        });



        getAnswers(question.id);



    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener=(QuestionThreadListener)context;

    }
    public void setImages(List<MediaFilesDTO> files)
    {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        questionImageRecyclerView.setLayoutManager(manager);
        FilesRecyclerViewAdapter qrvAdapter=new FilesRecyclerViewAdapter(getContext(),files);
        questionImageRecyclerView.setAdapter(qrvAdapter);
    }
    public void getAnswers(String id)
    {
        mListener.showProgressBar();
        AnswerDTO searchRequest=new AnswerDTO();
        searchRequest.questionId=id;
        QuestionThreadPresenter presenter=new QuestionThreadPresenter(this);
        presenter.getAnswers(getContext(),searchRequest);
    }
    public void refreshAnswers()
    {
        getAnswers(question.id);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void openBottomSheetAddAnswer()
    {  AddAnswerFragment fragment =  AddAnswerFragment.getInstance(question.id);
        fragment.show(getFragmentManager(), "dialog");

    }

    @Override
    public void OnGetAnswers(List<AnswerDTO> answerList) {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        answerRecyclerView.setLayoutManager(manager);
        AnswerRecyclerViewAdapter qrvAdapter=new AnswerRecyclerViewAdapter(getContext(),answerList);
        answerRecyclerView.setAdapter(qrvAdapter);
        mListener.hideProgressBar();
    }

    @Override
    public void OnError(String message) {
        mListener.hideProgressBar();
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }

    public interface QuestionThreadListener{
        void showProgressBar();
        void hideProgressBar();
    }
}
