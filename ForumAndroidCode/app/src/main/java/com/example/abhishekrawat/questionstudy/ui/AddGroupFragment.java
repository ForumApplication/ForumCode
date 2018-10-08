package com.example.abhishekrawat.questionstudy.ui;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.abhishekrawat.questionstudy.Adapter.ContactsRecyclerViewAdapter;
import com.example.abhishekrawat.questionstudy.Model.ContactsChip;
import com.example.abhishekrawat.questionstudy.Model.ContactsDTO;
import com.example.abhishekrawat.questionstudy.Model.GroupDTO;
import com.example.abhishekrawat.questionstudy.Model.UserDTO;
import com.example.abhishekrawat.questionstudy.R;
import com.example.abhishekrawat.questionstudy.Util.FragmentUtil;
import com.example.abhishekrawat.questionstudy.Util.PreferenceManager;
import com.example.abhishekrawat.questionstudy.presenter.AddGroupPresenter;
import com.google.gson.Gson;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static android.Manifest.permission.READ_CONTACTS;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;


public class AddGroupFragment extends Fragment implements View.OnClickListener, AddGroupView {
    private AddGroupPageListener mListener;
    Button addGroupBtn;
    ChipsInput mChipsInput;
    List<ContactsDTO> contactList;
    EditText mTitle, mDescription;
    private List<ChipInterface> contactsAdded = new ArrayList<>();
    private List<ContactsChip> contactsChip = new ArrayList<>();
    int REQUEST_READ_CONTACTS = 2;
    Cursor cursor;

    public static AddGroupFragment getInstance() {
        AddGroupFragment fragment = new AddGroupFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
/*
        addGroupBtn=view.findViewById(R.id.add_member_btn);
        addGroupBtn.setOnClickListener(this);*/
        addGroupBtn = view.findViewById(R.id.add_group_btn);
        addGroupBtn.setOnClickListener(this);
        mTitle = view.findViewById(R.id.group_title);
        mDescription = view.findViewById(R.id.group_description);
        mChipsInput = view.findViewById(R.id.chips_input);
        mChipsInput.addChipsListener(chipsListener);
        getContacts();
    }

    ChipsInput.ChipsListener chipsListener = new ChipsInput.ChipsListener() {
        @Override
        public void onChipAdded(ChipInterface chip, int newSize) {
            contactsAdded.add(chip);
        }

        @Override
        public void onChipRemoved(ChipInterface chip, int newSize) {
            contactsAdded.remove(chip);
        }

        @Override
        public void onTextChanged(CharSequence text) {
            //Log.e(TAG, "text changed: " + text.toString());
        }
    };

    public void getContactChipList(List<ContactsDTO> contactsList) {
        Integer id = 0;
        for (ContactsDTO contact : contactsList) {
            ContactsChip contactChip = new ContactsChip(id, contact.name, contact.mobile);
            // add contact to the list
            contactsChip.add(contactChip);
            id++;
        }
        // pass contact list to chips input
        mChipsInput.setFilterableList(contactsChip);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_group, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (AddGroupPageListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_group_btn:
                mListener.showProgressBar();
                /*ContactsReadFragment fragment=new ContactsReadFragment();
                FragmentUtil.replaceAndAddFragment(getActivity(),fragment,R.id.fragment_container);*/
                GroupDTO group = new GroupDTO();
                AddGroupPresenter presenter = new AddGroupPresenter(this);
                Gson gson = new Gson();
                String userInfo = PreferenceManager.getInstance(getContext()).getUserInfo();
                UserDTO user = gson.fromJson(userInfo, UserDTO.class);
                group.userId = user.id;
                group.title = mTitle.getText().toString();
                group.description = mDescription.getText().toString();
                group.members= new ArrayList();
                for (ChipInterface chip:contactsAdded) {
                    group.members.add(chip.getInfo());
                }
                presenter.addGroup(getContext(), group);
                break;
        }
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(getContext(), READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContacts();
            }
        }
    }

    public void getContacts() {
        if (!mayRequestContacts()) {
            return;
        }
        contactList = new ArrayList<ContactsDTO>();
        String phoneNumber = null;
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        ContentResolver contentResolver = getContext().getContentResolver();
        cursor = contentResolver.query(CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            final int[] counter = {0};
            while (cursor.moveToNext()) {
                ContactsDTO contact = new ContactsDTO();
                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    contact.name = name;
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        contact.mobile = phoneNumber;
                    }
                    phoneCursor.close();
                }
                if (isValidMobile(contact.mobile))
                    contactList.add(contact);
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getContactChipList(contactList);
                }
            });
        }
    }

    private boolean isValidMobile(String phone) {
        boolean check = false;
        try {
            if (!Pattern.matches("[a-zA-Z]+", phone) && !TextUtils.isEmpty(phone)) {
                if (phone.length() < 10 || phone.length() > 13) {
                    check = false;
                } else {
                    check = true;
                }
            } else {
                check = false;
            }
            return check;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public void onAddGroupSuccess() {
        mListener.onGroupAddSuccess();
    }

    @Override
    public void onError(String message) {
        mListener.hideProgressBar();
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    public interface AddGroupPageListener {
        void onGroupAddSuccess();
        void showProgressBar();
        void hideProgressBar();
    }
}
