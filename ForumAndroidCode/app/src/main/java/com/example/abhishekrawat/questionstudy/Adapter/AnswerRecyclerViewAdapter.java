package com.example.abhishekrawat.questionstudy.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhishekrawat.questionstudy.Model.AnswerDTO;
import com.example.abhishekrawat.questionstudy.Model.QuestionDTO;
import com.example.abhishekrawat.questionstudy.R;
import com.example.abhishekrawat.questionstudy.ui.ActivityListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class AnswerRecyclerViewAdapter extends RecyclerView.Adapter<AnswerRecyclerViewAdapter.AnswerViewHolder>
        implements View.OnClickListener{
    private List<AnswerDTO> answerList;
    Context mContext;
    AnswerItemListener pageListener;

    public AnswerRecyclerViewAdapter(Context context, List<AnswerDTO> questions) {
        this.mContext=context;
        this.answerList=questions;
    }
    @NonNull
    @Override
    public AnswerRecyclerViewAdapter.AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer, parent, false);
        AnswerRecyclerViewAdapter.AnswerViewHolder vh = new AnswerRecyclerViewAdapter.AnswerViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final AnswerRecyclerViewAdapter.AnswerViewHolder holder, int position) {
        AnswerDTO answer=answerList.get(position);
        try {
            holder.answerText.setText(answer.answer);
            holder.answerText.setTag(position);
            holder.createTime.setText(answer.date);
            holder.userName.setText(answer.user.name);
        }
        catch (Exception ex)
        {}
        pageListener=(AnswerItemListener) mContext;
    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

        }
    }

    public class AnswerViewHolder extends RecyclerView.ViewHolder {
        TextView answerText,userName,createTime;
        public AnswerViewHolder(View itemView) {
            super(itemView);
            answerText=itemView.findViewById(R.id.answer_text);
            userName=itemView.findViewById(R.id.user_name);
            createTime=itemView.findViewById(R.id.create_time);
        }
    }
    public interface AnswerItemListener extends ActivityListener
    {
      //  void openQuestionDetailFragment(QuestionDTO question);
    }
}
