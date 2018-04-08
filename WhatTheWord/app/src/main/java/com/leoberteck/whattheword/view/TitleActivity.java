package com.leoberteck.whattheword.view;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.leoberteck.whattheword.BR;
import com.leoberteck.whattheword.R;
import com.leoberteck.whattheword.entities.Score;
import com.leoberteck.whattheword.loaders.ScoreLoader;
import com.leoberteck.whattheword.mvp.impl.TitlePresenterImpl;
import com.leoberteck.whattheword.mvp.interfaces.Title;

public class TitleActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Score> {

    private static final int LOAD_BEST_SCORE = 1001;

    private Title.TitlePresenter presenter;
    private ViewDataBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_title_screen);
        presenter = ViewModelProviders.of(this).get(TitlePresenterImpl.class);
        binding.setVariable(BR.vm, presenter);
        getSupportLoaderManager().initLoader(LOAD_BEST_SCORE, null, this).forceLoad();
    }

    @NonNull
    @Override
    public Loader<Score> onCreateLoader(int id, Bundle args) {
        return new ScoreLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Score> loader, Score data) {
        presenter.setBestScore(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Score> loader) {
        presenter.setBestScore(null);
    }
}
