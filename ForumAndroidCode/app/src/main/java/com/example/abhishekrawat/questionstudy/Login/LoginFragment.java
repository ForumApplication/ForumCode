package com.example.abhishekrawat.questionstudy.Login;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abhishekrawat.questionstudy.Login.Presenter.LoginPresenter;
import com.example.abhishekrawat.questionstudy.R;
import com.example.abhishekrawat.questionstudy.Util.FragmentUtil;


public class LoginFragment extends Fragment implements LoginView,View.OnClickListener {
    LoginFragmentListener pageListener;
    EditText mMobileNumber,mPassword;

    public static LoginFragment getInstance() {
        LoginFragment fragment = new LoginFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMobileNumber=view.findViewById(R.id.mobile_number);
        mPassword=view.findViewById(R.id.password);
        view.findViewById(R.id.sign_in).setOnClickListener(this);
        view.findViewById(R.id.sign_up).setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        pageListener=(LoginFragmentListener) context;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        pageListener.showProgressBar();
        LoginPresenter loginPresenter=new LoginPresenter(this);
        switch(view.getId())
        {
            case R.id.sign_up:
                pageListener.openRegisterFragment();
                break;
            case R.id.sign_in:
                loginPresenter.login(getContext(),mMobileNumber.getText().toString(),mPassword.getText().toString());

        }
    }

    @Override
    public void onLoginSuccess() {
        pageListener.loginSuccess();
    }

    @Override
    public void onError(String message) {
        pageListener.hideProgressBar();
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }

    public interface  LoginFragmentListener{
        void loginSuccess();
        void openRegisterFragment();
        void showProgressBar();
        void hideProgressBar();
        void backButtonFinish();
    }
}
