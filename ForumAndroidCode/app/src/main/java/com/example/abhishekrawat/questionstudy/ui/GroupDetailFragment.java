package com.example.abhishekrawat.questionstudy.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhishekrawat.questionstudy.Adapter.QuestionRecyclerViewAdapter;
import com.example.abhishekrawat.questionstudy.Model.GroupDTO;
import com.example.abhishekrawat.questionstudy.Model.QuestionDTO;
import com.example.abhishekrawat.questionstudy.Model.UserDTO;
import com.example.abhishekrawat.questionstudy.R;
import com.example.abhishekrawat.questionstudy.presenter.GroupDetailPresenter;
import com.pchmn.materialchips.ChipView;
import com.pchmn.materialchips.ChipsInput;

import java.util.List;

public class GroupDetailFragment extends Fragment implements GroupDetailView {

    private GroupDetailFragmentListener mListener;
    TextView groupTitle, groupDescription, adminName, createTime;
    GroupDetailPresenter mPresenter;
    RecyclerView questionRecyclerView;
    LinearLayout membersChipContainer;
    private GroupDTO group;
    private static String GROUP = "group";
    // TODO: Rename and change types and number of parameters
    public static GroupDetailFragment getInstance(GroupDTO group) {
        GroupDetailFragment fragment = new GroupDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(GROUP, group);
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
        return inflater.inflate(R.layout.fragment_group_detail, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        group = (GroupDTO) this.getArguments().getSerializable(GROUP);
        groupTitle = view.findViewById(R.id.group_title);
        groupDescription = view.findViewById(R.id.group_description);
        adminName = view.findViewById(R.id.admin_name);
        createTime = view.findViewById(R.id.create_time);
        questionRecyclerView=(RecyclerView) view.findViewById(R.id.question_recycler_view);
        membersChipContainer=view.findViewById(R.id.members_chip_container);
        groupTitle.setText(group.title);
        groupDescription.setText(group.description);
        adminName.setText(group.admin.name);
        createTime.setText(group.createdDate);
        mPresenter=new GroupDetailPresenter(this);
        getQuestions();
        getMembers();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GroupDetailFragmentListener) {
            mListener = (GroupDetailFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    void getQuestions()
    {
        mListener.showProgressBar();
        mPresenter.getQuestions(this.getContext(),group.id);
    }
    void getMembers()
    {
        mListener.showProgressBar();
        mPresenter.getMembers(this.getContext(),group.id);
    }


    @Override
    public void onQuestionLoaded(List<QuestionDTO> questions) {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        questionRecyclerView.setLayoutManager(manager);
        QuestionRecyclerViewAdapter qrvAdapter=new QuestionRecyclerViewAdapter(getContext(),questions);
        questionRecyclerView.setAdapter(qrvAdapter);
        mListener.hideProgressBar();
    }

    @Override
    public void onMembersLoaded(List<UserDTO> members) {
        for (UserDTO member:members) {

            ChipView membersChip=new ChipView(getContext());
            membersChip.setLabel(member.name);
            membersChip.setTag(member);
            membersChip.setPadding(5,0,0,0);
            membersChipContainer.addView(membersChip);
        }
    }

    @Override
    public void onError(String message) {
        mListener.hideProgressBar();
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }


    public interface GroupDetailFragmentListener extends ActivityListener {
    }
}
