package com.ivanvelickovski.webfactorydemoapp.UI;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.ivanvelickovski.webfactorydemoapp.Model.VolumeData;
import com.ivanvelickovski.webfactorydemoapp.Model.VolumeItem;
import com.ivanvelickovski.webfactorydemoapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ChooseOptionFragment.ChooseOptionListener,
    DownloadFragment.DownloadListener, BooksFragment.BooksListener, BooksAdapter.BooksAdapterListener {
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
        } else {
            initChooseOptionFragment(savedInstanceState);
        }
    }

    private void initChooseOptionFragment(final Bundle savedInstanceState) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.flTop, chooseOptionFragment)
                .runOnCommit(new Runnable() {
                    @Override
                    public void run() {
                        if (savedInstanceState != null
                                && savedInstanceState.getParcelableArrayList("books") != null
                                && !savedInstanceState.getBoolean("booksShown")) {
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
                .runOnCommit(new Runnable() {
                    @Override
                    public void run() {
                        ValueAnimator animator = ValueAnimator.ofInt(chooseOptionFragment.getView().getHeight(), 0);

                        animator.setTarget(chooseOptionFragment.getView());
                        animator.setDuration(500);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) chooseOptionFragment.getView().getLayoutParams();
                                layoutParams.height = (int)animation.getAnimatedValue();
                                chooseOptionFragment.getView().setLayoutParams(layoutParams);
                            }
                        });
                        animator.start();
                    }
                })
                .commit();
    }

    private String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            return new String(bytes);
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

    @Override
    public void setFragmentBackgroundColor(int color) {
        booksFragment.getView().setBackgroundColor(color);
    }
}
