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
import android.widget.TextView;

import com.example.abhishekrawat.questionstudy.Adapter.QuestionRecyclerViewAdapter;
import com.example.abhishekrawat.questionstudy.Model.QuestionDTO;
import com.example.abhishekrawat.questionstudy.R;
import com.example.abhishekrawat.questionstudy.Util.Util;
import com.example.abhishekrawat.questionstudy.presenter.HomePresenter;

import java.util.List;


public class HomeFragment extends Fragment implements HomeView {

    HomePageListener mListener;
    RecyclerView questionRecyclerView;
    List<QuestionDTO> questionList;
    QuestionRecyclerViewAdapter qrvAdapter;
    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        questionRecyclerView=(RecyclerView) view.findViewById(R.id.question_recycler_view);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.openAddQuestion();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener.showProgressBar();
        getQuestions();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener=(HomePageListener)context;
    }
    public void getQuestions()
    {
        HomePresenter homePresenter=new HomePresenter(this);
        homePresenter.getQuestions(getContext());
    }
    @Override
    public void onQuestionListSuccess(List<QuestionDTO> questionList) {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        questionRecyclerView.setLayoutManager(manager);
         QuestionRecyclerViewAdapter qrvAdapter=new QuestionRecyclerViewAdapter(getContext(),questionList);
        questionRecyclerView.setAdapter(qrvAdapter);
        mListener.hideProgressBar();
    }
     public interface HomePageListener{
        void showProgressBar();
        void hideProgressBar();
         void openAddQuestion();

     }

}
