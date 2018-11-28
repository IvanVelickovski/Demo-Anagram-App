package com.ivanvelickovski.webfactorydemoapp.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ivanvelickovski.webfactorydemoapp.Model.VolumeData;
import com.ivanvelickovski.webfactorydemoapp.R;

import java.io.IOException;
import java.io.InputStream;

public class ChooseOptionFragment extends Fragment implements View.OnClickListener {
    private ChooseOptionListener mListener;

    private ProgressBar progressBar;
    private TextView txtRemoteDataReady;
    private TextView txtLocalDataReady;
    private TextView txtRemoteDataError;
    private TextView txtLocalDataError;
    private Button btnShowData;
    private boolean localDataReady, remoteDataReady;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_choose_option, container, false);

        Button btnLocalSource = v.findViewById(R.id.btnLocalSource);
        Button btnRemoteSource = v.findViewById(R.id.btnRemoteSource);

        initProperties(v);

        String[] sourceOptions = getContext().getResources().getStringArray(R.array.source_options);
        String localSourceOption = sourceOptions[0];
        String remoteSourceOption = sourceOptions[1];

        btnLocalSource.setText(localSourceOption);
        btnRemoteSource.setText(remoteSourceOption);

        btnLocalSource.setOnClickListener(this);
        btnRemoteSource.setOnClickListener(this);
        btnShowData.setOnClickListener(this);

        downloadProgressUpdate(0);

        return v;
    }

    private void initProperties(View v) {
        progressBar = v.findViewById(R.id.pbChoose);
        txtLocalDataReady = v.findViewById(R.id.txtLocalDataReady);
        txtRemoteDataReady = v.findViewById(R.id.txtRemoteDataReady);
        txtLocalDataError = v.findViewById(R.id.txtLocalDataError);
        txtRemoteDataError = v.findViewById(R.id.txtRemoteDataError);
        btnShowData = v.findViewById(R.id.btnShowData);

        txtRemoteDataReady.setHeight(0);
        txtLocalDataReady.setHeight(0);
        txtRemoteDataError.setHeight(0);
        txtLocalDataError.setHeight(0);
        btnShowData.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChooseOptionListener) {
            mListener = (ChooseOptionListener) context;
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

    public void downloadProgressUpdate(float progress) {
        if (progress > 0 && progress < 1) {
            txtRemoteDataError.setHeight(0);
            txtLocalDataError.setHeight(0);

            progressBar.setVisibility(View.VISIBLE);
            txtRemoteDataReady.setHeight(0);

            if (!localDataReady) {
                btnShowData.setVisibility(View.GONE);
            }
        } else {
            progressBar.setVisibility(View.GONE);

            if (progress == 1) {
                remoteDataReady = true;
                txtRemoteDataReady.setVisibility(View.VISIBLE);
                txtRemoteDataReady.setHeight(50);
                btnShowData.setVisibility(View.VISIBLE);
            }
        }
        setViewsAlpha(progress);
    }

    public void remoteDataError() {
        txtRemoteDataReady.setHeight(50);
        txtRemoteDataReady.setVisibility(View.INVISIBLE);
        txtRemoteDataError.setHeight(50);
    }

    public void localDataError() {
        txtLocalDataReady.setHeight(0);
        txtLocalDataReady.setVisibility(View.INVISIBLE);
        txtLocalDataError.setHeight(50);
    }

    public void localDataProgressUpdate(Boolean downloaded) {
        if (downloaded) {
            txtLocalDataReady.setHeight(50);
            txtLocalDataReady.setVisibility(View.VISIBLE);
            btnShowData.setVisibility(View.VISIBLE);
            localDataReady = true;
        } else {
            txtLocalDataReady.setHeight(0);

            if (!remoteDataReady) {
                btnShowData.setVisibility(View.GONE);
            }
        }
    }

    private void setViewsAlpha(float progress) {
        if (((ViewGroup)getView()) == null) {
            return;
        }

        for (int i = 0; i < ((ViewGroup)getView()).getChildCount(); i++) {
            View viewToSetAlphaTo = ((ViewGroup)getView()).getChildAt(i);
            if (viewToSetAlphaTo.getId() != R.id.pbChoose) {
                if (progress > 0 && progress < 1) {
                    float inactiveAlpha = (float)getContext().getResources().getInteger(R.integer.alpha_inactive) / 100f;
                    viewToSetAlphaTo.setAlpha(inactiveAlpha);
                } else {
                    float activeAlpha = (float)getContext().getResources().getInteger(R.integer.alpha_active) / 100f;
                    viewToSetAlphaTo.setAlpha(activeAlpha);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLocalSource:
                if (!localDataReady) {
                    mListener.downloadLocalData();
                }
                break;
            case R.id.btnRemoteSource:
                if (!remoteDataReady) {
                    mListener.downloadRemoteData();
                }
                break;
            case R.id.btnShowData:
                mListener.showData();
                break;
        }
    }

    public interface ChooseOptionListener {
        void downloadRemoteData();
        void downloadLocalData();
        void showData();
    }
}
