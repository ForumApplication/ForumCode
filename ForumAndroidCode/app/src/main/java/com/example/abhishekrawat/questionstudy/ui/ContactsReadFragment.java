package com.example.abhishekrawat.questionstudy.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.abhishekrawat.questionstudy.Adapter.ContactsRecyclerViewAdapter;
import com.example.abhishekrawat.questionstudy.Model.ContactsChip;
import com.example.abhishekrawat.questionstudy.Model.ContactsDTO;
import com.example.abhishekrawat.questionstudy.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static android.Manifest.permission.READ_CONTACTS;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class ContactsReadFragment extends Fragment implements View.OnClickListener {

    private ContactsPageListener mListener;
    int REQUEST_READ_CONTACTS = 2;
    List<ContactsDTO> contactList,addedContacts;
    Cursor cursor;
    private ProgressDialog pDialog;
    Handler updateBarHandler;
    RecyclerView mRecyclerView;
    ContactsRecyclerViewAdapter adapter;
    EditText searchText;
   Button mAddMemberBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView=view.findViewById(R.id.contacts_recycler_view);
        searchText=view.findViewById(R.id.search_text);
        searchText.addTextChangedListener(searchWatcher);
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Reading contacts...");
        pDialog.setCancelable(false);
        pDialog.show();
        updateBarHandler = new Handler();
        mAddMemberBtn=view.findViewById(R.id.add_member_btn);
        mAddMemberBtn.setOnClickListener(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getContacts();
            }
        }).start();
        addedContacts=new ArrayList<>();

    }
    TextWatcher searchWatcher=new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (adapter != null) {
                adapter.getFilter().filter(s);
            }
            // TODO Auto-generated method stub
        }
    };

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
        String email = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        StringBuffer output;

        ContentResolver contentResolver = getContext().getContentResolver();
        cursor = contentResolver.query(CONTENT_URI, null, null, null, null);
        // Iterate every contact in the phone
        if (cursor.getCount() > 0) {
            final int[] counter = {0};
            while (cursor.moveToNext()) {
                ContactsDTO contact = new ContactsDTO();
                // Update the progress message
                updateBarHandler.post(new Runnable() {
                    public void run() {

                        pDialog.setTitle("Reading contacts : " + counter[0]++ + "/" + cursor.getCount());
                        pDialog.show();
                    }
                });
                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    contact.name = name;
                    //This is to read multiple phone numbers associated with the same contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        contact.mobile = phoneNumber;
                    }

                    phoneCursor.close();

                /*    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);
                    while (emailCursor.moveToNext()) {
                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                        output.append("\n Email:" + email);
                    }

                    emailCursor.close();

                    String columns[] = {
                            ContactsContract.CommonDataKinds.Event.START_DATE,
                            ContactsContract.CommonDataKinds.Event.TYPE,
                            ContactsContract.CommonDataKinds.Event.MIMETYPE,
                    };

                    String where = ContactsContract.CommonDataKinds.Event.TYPE + "=" + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY +
                            " and " + ContactsContract.CommonDataKinds.Event.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "' and " + ContactsContract.Data.CONTACT_ID + " = " + contact_id;

                    String[] selectionArgs = null;
                    String sortOrder = ContactsContract.Contacts.DISPLAY_NAME;
                    */
                }

                // Add the contact to the ArrayList
                if(isValidMobile(contact.mobile))
                     contactList.add(contact);
            }

            // ListView has to be updated using a ui thread
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    LinearLayoutManager manager = new LinearLayoutManager(getContext());
                    manager.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(manager);
                    adapter = new ContactsRecyclerViewAdapter(getContext(),contactList);
                    mRecyclerView.setAdapter(adapter);
                }
            });

            // Dismiss the progressbar after 500 millisecondds
            updateBarHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    pDialog.dismiss();
                }
            }, 500);
        }

    }
    private boolean isValidMobile(String phone) {
        boolean check=false;
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
        }
        catch (Exception ex)
        {
            return  false;
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (ContactsPageListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void addContact(ContactsDTO contact) {
        addedContacts.add(contact);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.add_member_btn:
                mListener.onMembersAdded(addedContacts);
                break;
        }
    }

    public interface ContactsPageListener {
       void onMembersAdded(List<ContactsDTO> contacts);
    }
}
