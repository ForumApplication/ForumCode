package com.example.abhishekrawat.questionstudy.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.abhishekrawat.questionstudy.Adapter.AnswerRecyclerViewAdapter;
import com.example.abhishekrawat.questionstudy.Adapter.ContactsRecyclerViewAdapter;
import com.example.abhishekrawat.questionstudy.Adapter.GroupRecyclerViewAdapter;
import com.example.abhishekrawat.questionstudy.Adapter.QuestionRecyclerViewAdapter;
import com.example.abhishekrawat.questionstudy.Login.LoginActivity;
import com.example.abhishekrawat.questionstudy.Model.ContactsDTO;
import com.example.abhishekrawat.questionstudy.Model.GroupDTO;
import com.example.abhishekrawat.questionstudy.Model.QuestionDTO;
import com.example.abhishekrawat.questionstudy.R;
import com.example.abhishekrawat.questionstudy.Util.CONSTANTS;
import com.example.abhishekrawat.questionstudy.Util.FragmentUtil;
import com.example.abhishekrawat.questionstudy.Util.PreferenceManager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements QuestionRecyclerViewAdapter.QuestionItemListener
        , NavigationView.OnNavigationItemSelectedListener, MainFragment.MainFragmentListener,
        AddQuestionFragment.AddQuestionPageListener, ActivityListener, AddAnswerFragment.AddAnswerFragmentListener,
        AnswerRecyclerViewAdapter.AnswerItemListener, HomeFragment.HomePageListener, GroupFragment.GroupPageListener
        , QuestionThreadFragment.QuestionThreadListener, AddGroupFragment.AddGroupPageListener,
        ContactsReadFragment.ContactsPageListener,ContactsRecyclerViewAdapter.ContactAdapterListener,
        GroupRecyclerViewAdapter.GroupItemListener,GroupDetailFragment.GroupDetailFragmentListener {
    DrawerLayout mDrawerLayout;
    ProgressBar mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mProgressbar = (ProgressBar) findViewById(R.id.progress_bar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (!PreferenceManager.getInstance(this).getLoggedInStatus()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 1);
        } else {
            FragmentUtil.addFragment(this, MainFragment.getInstance(), R.id.fragment_container);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CONSTANTS.LOGIN_FINISH_REQUEST_CODE || resultCode == CONSTANTS.REGISTER_FINISH_REQUEST_CODE)
            FragmentUtil.addFragment(this, MainFragment.getInstance(), R.id.fragment_container);
        else if (resultCode == CONSTANTS.BACK_FINISH_REQUEST_CODE) {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void openQuestionDetailFragment(QuestionDTO question) {
        QuestionThreadFragment questionThreadFragment = QuestionThreadFragment.getInstance(question);
        FragmentUtil.replaceAndAddFragment(this, questionThreadFragment, R.id.fragment_container);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment != null && (fragment instanceof MainFragment)) {
                showExitAppDialog();
            } else
                super.onBackPressed();
        }
    }

    private void showExitAppDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.app_name);

        builder.setMessage(R.string.do_want_exit_from_the_app);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.setCancelable(false);
        builder.create().show();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.logout) {
            PreferenceManager.getInstance(this).setLoggedInStatus(false);
            PreferenceManager.getInstance(this).setUserInfo("");
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void openAddQuestion() {
        AddQuestionFragment addQuestionFragment = new AddQuestionFragment();
        FragmentUtil.replaceAndAddFragment(this, addQuestionFragment, R.id.fragment_container);
    }

    @Override
    public void onQuesitonAddSuccess() {
        hideProgressBar();
        FragmentUtil.popBackStackAndAdd(this, MainFragment.getInstance(), R.id.fragment_container);
    }

    @Override
    public void showProgressBar() {
        mProgressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressbar.setVisibility(View.GONE);
    }

    @Override
    public void openAddGroup() {
        AddGroupFragment addGroupFragment = new AddGroupFragment();
        FragmentUtil.replaceAndAddFragment(this, addGroupFragment, R.id.fragment_container);
    }

    @Override
    public void onMembersAdded(List<ContactsDTO> contacts) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null && fragment instanceof ContactsReadFragment) {
             FragmentUtil.popBackStackImmediate(this);
             fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment != null && fragment instanceof AddGroupFragment) {
                AddGroupFragment agf=(AddGroupFragment)fragment;
                 agf.getContactChipList(contacts);
            }
        }
    }

    @Override
    public void addContact(ContactsDTO contact) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null && fragment instanceof ContactsReadFragment) {
            ((ContactsReadFragment) fragment).addContact(contact);
        }
    }

    @Override
    public void onGroupAddSuccess() {
        hideProgressBar();
        Toast.makeText(this,"Group Added",Toast.LENGTH_LONG).show();
        FragmentUtil.popBackStackAndAdd(this, MainFragment.getInstance(), R.id.fragment_container);
    }

    @Override
    public void openGroupDetailFragment(GroupDTO group) {
        GroupDetailFragment groupDetailFragment = GroupDetailFragment.getInstance(group);
        FragmentUtil.replaceAndAddFragment(this, groupDetailFragment, R.id.fragment_container);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0) {
                    Fragment fragment=getSupportFragmentManager().getFragments().get(0);
                    boolean readAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (readAccepted)
                    {
                        if(fragment instanceof AddQuestionFragment)
                            ((AddQuestionFragment) fragment).pickImage();
                    }
                    else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                ((AddQuestionFragment) fragment).showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String [] {Manifest.permission.READ_EXTERNAL_STORAGE},
                                                            0);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }
}
