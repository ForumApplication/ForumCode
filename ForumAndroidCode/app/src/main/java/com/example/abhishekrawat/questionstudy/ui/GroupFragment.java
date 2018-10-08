package com.example.abhishekrawat.questionstudy.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abhishekrawat.questionstudy.Adapter.GroupRecyclerViewAdapter;
import com.example.abhishekrawat.questionstudy.Model.GroupDTO;
import com.example.abhishekrawat.questionstudy.Model.UserDTO;
import com.example.abhishekrawat.questionstudy.R;
import com.example.abhishekrawat.questionstudy.Util.PreferenceManager;
import com.example.abhishekrawat.questionstudy.presenter.GroupPresenter;
import com.google.gson.Gson;

import java.util.List;

public class GroupFragment extends Fragment implements GroupView{
    GroupPageListener mListener;
    RecyclerView groupRecyclerView;

    public static GroupFragment newInstance() {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
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
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupRecyclerView=(RecyclerView) view.findViewById(R.id.group_recycler_view);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_add_group);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.openAddGroup();
            }
        });
        getGroups();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener=(GroupPageListener)context;
        mListener.showProgressBar();

    }
    public void getGroups()
    {
        Gson gson=new Gson();
        String userInfo= PreferenceManager.getInstance(getContext()).getUserInfo();
        UserDTO user=gson.fromJson(userInfo,UserDTO.class);
        GroupPresenter homePresenter=new GroupPresenter(this);
        homePresenter.getGroups(getContext(),user.mobile);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onGroupListSuccess(List<GroupDTO> groupList) {

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        groupRecyclerView.setLayoutManager(manager);
        GroupRecyclerViewAdapter qrvAdapter = new GroupRecyclerViewAdapter(getContext(), groupList);
        groupRecyclerView.setAdapter(qrvAdapter);
        mListener.hideProgressBar();
    }

    public interface GroupPageListener{
        void openAddGroup();
        void showProgressBar();
        void hideProgressBar();
    }
}
