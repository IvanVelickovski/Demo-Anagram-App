package com.ivanvelickovski.webfactorydemoapp.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.ivanvelickovski.webfactorydemoapp.Model.VolumeData;
import com.ivanvelickovski.webfactorydemoapp.Model.VolumeItem;
import com.ivanvelickovski.webfactorydemoapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ChooseOptionFragment.ChooseOptionListener,
    DownloadFragment.DownloadListener, BooksFragment.BooksListener {
    private DownloadFragment downloadFragment;
    private ChooseOptionFragment chooseOptionFragment;
    private BooksFragment booksFragment;
    private ArrayList<VolumeItem> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chooseOptionFragment = new ChooseOptionFragment();

        downloadFragment = new DownloadFragment();
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, downloadFragment).commit();

        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("books") != null) {
            books = savedInstanceState.getParcelableArrayList("books");

            if (savedInstanceState.getBoolean("booksShown")) {
                showData();
            }
        } else if (savedInstanceState != null && savedInstanceState.getBoolean("downloadActive")) {
            downloadFragment.startDownload();
        }
    }

    private void initChooseOptionFragment(final Bundle savedInstanceState) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.flTop, chooseOptionFragment)
                .runOnCommit(new Runnable() {
                    @Override
                    public void run() {
                        if (savedInstanceState.getParcelableArrayList("books") != null && !savedInstanceState.getBoolean("booksShown")) {
                            chooseOptionFragment.downloadProgressUpdate(1);
                        }
                    }
                })
                .commit();
    }

    @Override
    public void downloadRemoteData() {
        downloadFragment.startDownload();
    }

    @Override
    public void downloadLocalData() {
        String myLocalJson = inputStreamToString(getResources().openRawResource(R.raw.local_data));
        VolumeData data = new Gson().fromJson(myLocalJson, VolumeData.class);
        dataDownloaded(data);
        chooseOptionFragment.localDataProgressUpdate(true);
    }

    @Override
    public void showData() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("books", books);
        booksFragment = new BooksFragment();
        booksFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flList, booksFragment)
                .commit();
    }

    private String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void dataDownloaded(VolumeData data) {
        books = new ArrayList<>(data.getItems());
    }

    @Override
    public void downloadProgress(float progress) {
        chooseOptionFragment.downloadProgressUpdate(progress);
    }

    @Override
    public void dataDownloadError() {
        chooseOptionFragment.remoteDataError();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (books != null && !books.isEmpty()) {
            outState.putParcelableArrayList("books", books);

            if (booksFragment != null) {
                outState.putBoolean("booksShown", true);
            }
        } else if (downloadFragment.downloadIsActive()) {
            outState.putBoolean("downloadActive", true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        downloadFragment = null;
        chooseOptionFragment = null;
        booksFragment = null;
        books = null;
    }
}
