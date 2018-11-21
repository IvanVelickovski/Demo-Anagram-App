package com.ivanvelickovski.webfactorydemoapp.UI;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ivanvelickovski.webfactorydemoapp.Model.VolumeData;
import com.ivanvelickovski.webfactorydemoapp.R;
import com.ivanvelickovski.webfactorydemoapp.Retrofit.ApiService;
import com.ivanvelickovski.webfactorydemoapp.Retrofit.RetroClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadFragment extends Fragment {
    private DownloadData downloadData;
    private static DownloadListener mListener;
    private static float progress;
    private static boolean downloadActive;

    public DownloadFragment() {
        progress = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return new View(getActivity());
    }

    public void startDownload() {
        if (downloadData == null || progress == 0) {
            downloadActive = true;
            downloadData = new DownloadData();
            downloadData.execute();
        }
    }

    public boolean downloadIsActive() {
        return downloadActive;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DownloadListener) {
            mListener = (DownloadListener) context;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        downloadData = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("restart_download", true);
    }

    public interface DownloadListener {
        void dataDownloaded(VolumeData data);
        void downloadProgress(float progress);
        void dataDownloadError();
    }

    private static class DownloadData extends AsyncTask<String, Void, VolumeData> {
        private VolumeData data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = 0.1f;
            mListener.downloadProgress(progress);
        }

        @Override
        protected VolumeData doInBackground(String... strings) {
            ApiService api = RetroClient.getApiService();
            Call<VolumeData> call = api.getVolumeData();

            try {
                data = call.execute().body();
            } catch (IOException e) {
                data = null;
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(VolumeData volumeData) {
            if (volumeData != null) {
                progress = 1;
                mListener.dataDownloaded(volumeData);
            } else {
                progress = -1;
                mListener.dataDownloadError();
            }

            mListener.downloadProgress(progress);
            downloadActive = false;
        }
    }
}
