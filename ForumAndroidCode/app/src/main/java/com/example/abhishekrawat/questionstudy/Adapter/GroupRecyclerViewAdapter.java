package com.example.abhishekrawat.questionstudy.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhishekrawat.questionstudy.Model.GroupDTO;
import com.example.abhishekrawat.questionstudy.R;
import com.example.abhishekrawat.questionstudy.ui.ActivityListener;

import java.util.List;

public class GroupRecyclerViewAdapter extends RecyclerView.Adapter<GroupRecyclerViewAdapter.GroupViewHolder> implements View.OnClickListener {
    private List<GroupDTO> groupList;
    Context mContext;

    GroupItemListener pageListener;

    public GroupRecyclerViewAdapter(Context context, List<GroupDTO> groupList) {
        this.mContext = context;
        this.groupList = groupList;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        GroupViewHolder vh = new GroupViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final GroupViewHolder holder, int position) {
        GroupDTO group = groupList.get(position);
        try {
            holder.groupTitle.setText(group.title);
            holder.groupTitle.setTag(position);
            holder.groupDescription.setText(group.description);
            holder.openGroupButton.setTag(position);
            holder.openGroupButton.setOnClickListener(this);
            holder.createTime.setText(group.createdDate);
            holder.userName.setText(group.admin.name);
        } catch (Exception ex) {
        }
        pageListener = (GroupItemListener) mContext;
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_group_btn:
                pageListener.openGroupDetailFragment(groupList.get(Integer.parseInt(v.getTag().toString())));
                break;
        }
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView groupTitle, groupDescription, userName, createTime;
        ImageView openGroupButton;

        public GroupViewHolder(View itemView) {
            super(itemView);
            groupTitle = itemView.findViewById(R.id.group_title);
            groupDescription = itemView.findViewById(R.id.group_description);
            openGroupButton = itemView.findViewById(R.id.open_group_btn);
            userName = itemView.findViewById(R.id.user_name);
            createTime = itemView.findViewById(R.id.create_time);
        }
    }

    public interface GroupItemListener extends ActivityListener {
        void openGroupDetailFragment(GroupDTO group);
    }
}