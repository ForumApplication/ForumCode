package com.example.abhishekrawat.questionstudy.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.abhishekrawat.questionstudy.Model.QuestionDTO;
import com.example.abhishekrawat.questionstudy.R;
import com.example.abhishekrawat.questionstudy.ui.ActivityListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class QuestionRecyclerViewAdapter extends RecyclerView.Adapter<QuestionRecyclerViewAdapter.QuestionViewHolder> implements View.OnClickListener {
    private List<QuestionDTO> questionList;
    Context mContext;

    QuestionItemListener pageListener;

    public QuestionRecyclerViewAdapter(Context context, List<QuestionDTO> questions) {
        this.mContext = context;
        this.questionList = questions;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        QuestionViewHolder vh = new QuestionViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final QuestionViewHolder holder, int position) {
        QuestionDTO question = questionList.get(position);
        try {
            holder.questionTitle.setText(question.question);
            holder.questionTitle.setTag(position);
            holder.questionDescription.setText(question.description);
            holder.createTime.setText(question.date);
            holder.userName.setText(question.user.name);
            if (question.mediaUrl.size() > 0) {
                holder.imageQuestion.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(question.mediaUrl.get(0).url)
                        .into(holder.imageQuestion)
                ;
            }
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(this);
        } catch (Exception ex) {
        }
        pageListener = (QuestionItemListener) mContext;
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    @Override
    public void onClick(View v) {
        pageListener.openQuestionDetailFragment(questionList.get(Integer.parseInt(v.getTag().toString())));
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView questionTitle, questionDescription, userName, createTime;
        ImageView imageQuestion;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            questionTitle = itemView.findViewById(R.id.question_title);
            questionDescription = itemView.findViewById(R.id.question_description);
            userName = itemView.findViewById(R.id.user_name);
            createTime = itemView.findViewById(R.id.create_time);
            imageQuestion = itemView.findViewById(R.id.image_question);

        }
    }

    public interface QuestionItemListener extends ActivityListener {
        void openQuestionDetailFragment(QuestionDTO question);
    }
}
