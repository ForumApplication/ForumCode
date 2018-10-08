package com.example.abhishekrawat.questionstudy.Util;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.content.Context;
import android.net.Uri;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.s3.transferutility.*;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.example.abhishekrawat.questionstudy.ui.AddQuestionFragment;
import com.example.abhishekrawat.questionstudy.ui.AddQuestionView;

import java.io.File;
import java.net.URISyntaxException;


public class AWSUtil {
    String bucket = "forumapplication";
    Context mContext;
    Activity mActivity;
    Fragment mFragment;
    Object mView;
    public AWSUtil(Context context,Fragment fragment,Object view) {
        this.mContext = context;
        this.mView=view;
        this.mFragment=fragment;
    }

    public void uploadWithTransferUtility(Uri uri) {
        try {

            AWSMobileClient.getInstance().initialize(mContext).execute();
            AWSConfiguration configuration = AWSMobileClient.getInstance().getConfiguration();

            TransferUtility transferUtility =
                    TransferUtility.builder()
                            .context(mContext)
                            .awsConfiguration(configuration)
                            .s3Client(new AmazonS3Client(AWSMobileClient.getInstance().getCredentialsProvider()))

                            .build();
            File file = null;
            file = new File(PathUtil.getPath(mContext, uri));
            long length = file.length() / 1024; // Size in KB

            if(length>500){
                file=Util.reduceFileSize(file);
            }
           final String filename=Util.getCurrentTimestamp()+String.valueOf(Math.random()).substring(3) + "." + Util.getMimeType(mContext,uri);
           final String fileUrl="userfiles/" +filename ;
            TransferObserver uploadObserver =
                    transferUtility.upload(
                            fileUrl,
                            file);

            uploadObserver.setTransferListener(new TransferListener() {

                @Override
                public void onStateChanged(int id, TransferState state) {
                    if (TransferState.COMPLETED == state) {
                        if(mFragment instanceof AddQuestionFragment)
                        {
                            AddQuestionView adv=(AddQuestionView)mView;
                            adv.fileUploadSuccess(filename);
                        }

                    }
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                    int percentDone = (int) percentDonef;
                }

                @Override
                public void onError(int id, Exception ex) {
                }

            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    private String getAWSUrl(String filename)
    {
        return "https://s3.ap-south-1.amazonaws.com/forumapplication/"+filename;
    }
}
