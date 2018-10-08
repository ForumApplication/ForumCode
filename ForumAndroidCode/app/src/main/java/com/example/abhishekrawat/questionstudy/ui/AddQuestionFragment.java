package com.example.abhishekrawat.questionstudy.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v17.leanback.widget.HorizontalGridView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhishekrawat.questionstudy.Adapter.ImageAdapterGridView;
import com.example.abhishekrawat.questionstudy.Model.GroupDTO;
import com.example.abhishekrawat.questionstudy.Model.QuestionDTO;
import com.example.abhishekrawat.questionstudy.Model.UserDTO;
import com.example.abhishekrawat.questionstudy.R;
import com.example.abhishekrawat.questionstudy.Util.AWSUtil;
import com.example.abhishekrawat.questionstudy.Util.FragmentUtil;
import com.example.abhishekrawat.questionstudy.Util.PreferenceManager;
import com.example.abhishekrawat.questionstudy.Util.Util;
import com.example.abhishekrawat.questionstudy.presenter.AddQuestionPresenter;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddQuestionFragment extends Fragment implements View.OnClickListener, AddQuestionView,
        AdapterView.OnItemSelectedListener, View.OnTouchListener {

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 2;
    private AddQuestionPageListener mListener;
    private EditText mQuestionTitle, mQuestionDescription;
    private Button mSaveBtn;
    TextView mGroupHint;
    private Spinner mGroupSpinner;
    List<GroupDTO> mGroups;
    GroupDTO selectedGroup;
    View button;
    HorizontalGridView imageGrid;
    List<String> uploadedImages=new ArrayList<>();
    List<Uri> imageUris=new ArrayList<>();
    Dialog fileUploadDialog;
    TextView uploadStatus;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_question, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mQuestionTitle = view.findViewById(R.id.question_title);
        mQuestionDescription = view.findViewById(R.id.question_description);
        mSaveBtn = view.findViewById(R.id.save_btn);
        mGroupHint = view.findViewById(R.id.group_hint);
        imageGrid=view.findViewById(R.id.gridview_images);

        imageGrid.setWindowAlignment(HorizontalGridView.WINDOW_ALIGN_BOTH_EDGE);
        imageGrid.setWindowAlignmentOffsetPercent(35);

        view.findViewById(R.id.image_camera).setOnClickListener(this);
        view.findViewById(R.id.file_pick).setOnClickListener(this);

        mSaveBtn.setOnClickListener(this);
        mGroupSpinner = view.findViewById(R.id.group_spinner);
        mGroupSpinner.setOnItemSelectedListener(this);
        mGroupSpinner.setOnTouchListener(this);
        getGroups();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImage = data.getData();

        if(Util.isValidFile(Util.getMimeType(getContext(),selectedImage)))
        {
            imageUris.add(selectedImage);
            refreshGrid();
        }
        else
            Toast.makeText(getContext(),"File type not supported",Toast.LENGTH_LONG).show();
    }
    public void refreshGrid()
    {
        imageGrid.setAdapter(new ImageAdapterGridView(getContext(),imageUris));

    }
    public void getGroups() {
        Gson gson = new Gson();
        String userInfo = PreferenceManager.getInstance(getContext()).getUserInfo();
        UserDTO user = gson.fromJson(userInfo, UserDTO.class);
        AddQuestionPresenter presenter = new AddQuestionPresenter(this);
        presenter.getGroups(getContext(), user.mobile);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddQuestionPageListener) {
            mListener = (AddQuestionPageListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.save_btn:
                mListener.showProgressBar();
                if(imageUris.size()>0)
                     uploadImages();
                else
                    saveQuestion();

                break;
            case R.id.file_pick:
                button = v;
                if (!checkPermission()) {
                    requestPermission();
                } else {
                    openGalleryPickImage();
                }

                break;

            case R.id.image_camera:
                button = v;
                if (!checkPermission()) {
                    requestPermission();
                } else {
                    openCameraPickImage();
                }
                break;


        }
    }

    @Override
    public void onAddQuestionSuccess() {
        Toast.makeText(getContext(), getString(R.string.question_added), Toast.LENGTH_LONG).show();
        mListener.onQuesitonAddSuccess();
    }

    @Override
    public void onError(String message) {
        mListener.hideProgressBar();
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGroupListSuccess(List<GroupDTO> groupList) {
        mGroups = groupList;

        if (groupList != null) {
            setDataToSpinner(mGroupSpinner, groupList,
                    getString(R.string.group_list_hint),
                    getString(R.string.group_list_hint), mGroupHint);
        }

    }

    @Override
    public void fileUploadSuccess(String Url) {
        uploadedImages.add(Url);
        TextView status = (TextView) fileUploadDialog.findViewById(R.id.textView);
        status.setText(uploadedImages.size() + "/" + imageUris.size());

        if (uploadedImages.size() == imageUris.size()) {
            fileUploadDialog.dismiss();
            saveQuestion();
        }
    }
    private void saveQuestion()
    {
        if(validateData()) {
                QuestionDTO questionDTO = new QuestionDTO();
                questionDTO.question = mQuestionTitle.getText().toString();
                questionDTO.description = mQuestionDescription.getText().toString();
                questionDTO.groupId = selectedGroup.id;
                questionDTO.fileUrls=uploadedImages;
                AddQuestionPresenter presenter = new AddQuestionPresenter(this);
                presenter.SaveQuestion(getContext(), questionDTO);
        }
        else
            mListener.hideProgressBar();
    }
    private boolean validateData() {
        if (TextUtils.isEmpty(mQuestionTitle.getText().toString())) {
            Toast.makeText(getContext(), "Please enter question title", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(mQuestionDescription.getText().toString())) {
            Toast.makeText(getContext(), "Please enter question description", Toast.LENGTH_LONG).show();
            return false;
        }
        if (selectedGroup == null) {
            Toast.makeText(getContext(), "Please select group", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }



    private void setDataToSpinner(Spinner spinner, final List<GroupDTO> groupList, String hintText,
                                  String selectedItem, TextView hintTextView) {
        List<String> strGroupList = new ArrayList<>();
        for (GroupDTO group : groupList) {
            strGroupList.add(group.title);
        }
        List<String> itemList = new ArrayList<>();
        itemList.add(hintText);
        itemList.addAll(strGroupList);
        hintTextView.setVisibility(View.GONE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.item_spinner_layout, itemList) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == 0) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(0));
                    return v;
                }
                v.setTag(groupList.get(position - 1));
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount(); // you dont display last item. It is used as hint.
            }

        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        if (!TextUtils.isEmpty(selectedItem)) {
            int count = 1;
            for (String item : strGroupList) {
                if (item.equalsIgnoreCase(selectedItem)) {
                    spinner.setSelection(count);
                    break;
                }
                count++;
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Util.hideKeyboard(getContext());
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedGroup = (GroupDTO) view.getTag();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface AddQuestionPageListener extends ActivityListener {
        // TODO: Update argument type and name
        void onQuesitonAddSuccess();
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
    }
    private void uploadImages()
    {

        fileUploadDialog = new Dialog(getContext());
        uploadStatus=new TextView(getContext());
        uploadStatus.setId(R.id.textView);
        uploadStatus.setText("0/"+imageUris.size());
        fileUploadDialog.setContentView(uploadStatus);
        fileUploadDialog.setTitle("File upload status");
        fileUploadDialog.setCancelable(false);
        fileUploadDialog.show();
        for(Uri uri:imageUris) {
            AWSUtil aws = new AWSUtil(getContext(), this, this);
            aws.uploadWithTransferUtility(uri);
        }
    }

    public void openCameraPickImage() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PICK_FROM_CAMERA);

    }

    public void openGalleryPickImage() {
        Intent intent = new Intent();
        String[] mimeTypes = {"image/*", "application/pdf"};

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";

            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }

            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Document"), PICK_FROM_GALLERY);


    }

    public void pickImage() {
        switch (button.getId()) {
            case R.id.image_camera:
                openCameraPickImage();
                break;
            case R.id.file_pick:
                openCameraPickImage();
                break;
        }
    }

    public void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}
