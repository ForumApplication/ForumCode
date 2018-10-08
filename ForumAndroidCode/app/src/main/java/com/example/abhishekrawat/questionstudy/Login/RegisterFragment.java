package com.example.abhishekrawat.questionstudy.Login;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.abhishekrawat.questionstudy.Login.Presenter.RegisterPresenter;
import com.example.abhishekrawat.questionstudy.Model.UserDTO;
import com.example.abhishekrawat.questionstudy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.Executor;


public class RegisterFragment extends Fragment implements View.OnClickListener, RegisterView {
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    EditText mMobileNumber, mOtp,mName,mPassword;
    FirebaseUser user;
    String mVerificationId;
    private FirebaseAuth mAuth;
    RegisterPageListener pageListener;
    public static RegisterFragment getInstance() {
        RegisterFragment fragment = new RegisterFragment();
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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.send_otp_btn).setOnClickListener(this);
        view.findViewById(R.id.verify_otp_btn).setOnClickListener(this);
        view.findViewById(R.id.register_btn).setOnClickListener(this);
        mMobileNumber = view.findViewById(R.id.mobile_number);
        mOtp = view.findViewById(R.id.otp);
        mAuth = FirebaseAuth.getInstance();
        mName=view.findViewById(R.id.name);
        mPassword=view.findViewById(R.id.password);
        pageListener.hideProgressBar();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                mOtp.setText(credential.getSmsCode());
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                } else if (e instanceof FirebaseTooManyRequestsException) {
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                onOtpSend();
                mVerificationId = verificationId;

                Toast.makeText(getContext(), "OTP send", Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        pageListener=(RegisterPageListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        RegisterPresenter registerPresenter = new RegisterPresenter(this);
        switch (v.getId()) {
            case R.id.send_otp_btn:
                pageListener.showProgressBar();
                registerPresenter.startPhoneNumberVerification(getActivity(), "+91" + mMobileNumber.getText().toString(), mCallback);
                break;
            case R.id.verify_otp_btn:
                pageListener.showProgressBar();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, mOtp.getText().toString());
                signInWithPhoneAuthCredential(credential);
                break;
            case R.id.register_btn:
                pageListener.showProgressBar();
                 registerPresenter.registerUser(getContext(),mName.getText().toString(),mMobileNumber.getText().toString(),mPassword.getText().toString());
                break;


        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = task.getResult().getUser();
                            onOtpVerify();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            }
                        }
                    }
                });
    }

    @Override
    public void onOtpSend() {
        pageListener.hideProgressBar();
        getView().findViewById(R.id.layout_mobile).setVisibility(View.GONE);
        getView().findViewById(R.id.layout_otp).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.layout_register).setVisibility(View.GONE);

    }

    @Override
    public void onOtpVerify() {
        pageListener.hideProgressBar();
        getView().findViewById(R.id.layout_mobile).setVisibility(View.GONE);
        getView().findViewById(R.id.layout_otp).setVisibility(View.GONE);
        getView().findViewById(R.id.layout_register).setVisibility(View.VISIBLE);

    }

    @Override
    public void onRegisterSuccess() {
        pageListener.finishActivity();
    }

    @Override
    public void onError(String message) {
        pageListener.hideProgressBar();
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }
    public interface RegisterPageListener{
        void finishActivity();
        void showProgressBar();
        void hideProgressBar();
    }
}
