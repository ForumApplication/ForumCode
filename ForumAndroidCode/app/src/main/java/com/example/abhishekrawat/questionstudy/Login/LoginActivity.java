package com.example.abhishekrawat.questionstudy.Login;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.example.abhishekrawat.questionstudy.R;
import com.example.abhishekrawat.questionstudy.Util.CONSTANTS;
import com.example.abhishekrawat.questionstudy.Util.FragmentUtil;
import com.example.abhishekrawat.questionstudy.ui.MainFragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener,RegisterFragment.RegisterPageListener {
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FragmentUtil.addFragment(this, LoginFragment.getInstance(),R.id.fragment_container);
        mProgressBar=this.findViewById(R.id.progress_bar);
    }

    @Override
    public void loginSuccess() {
        setResult(CONSTANTS.LOGIN_FINISH_REQUEST_CODE);
        finish();
    }

    @Override
    public void openRegisterFragment() {
        FragmentUtil.replaceAndAddFragment(this,RegisterFragment.getInstance(),R.id.fragment_container);
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void backButtonFinish() {
        setResult(CONSTANTS.BACK_FINISH_REQUEST_CODE);
        finish();
    }
    @Override
    public void onBackPressed() {

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment != null && fragment instanceof LoginFragment) {
                showExitAppDialog();
            }
            super.onBackPressed();
    }
    private void showExitAppDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.app_name);

        builder.setMessage(R.string.do_want_exit_from_the_app);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(CONSTANTS.BACK_FINISH_REQUEST_CODE);
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.setCancelable(false);
        builder.create().show();
    }


    @Override
    public void finishActivity() {
        hideProgressBar();
        setResult(CONSTANTS.REGISTER_FINISH_REQUEST_CODE);
        finish();
    }

}
