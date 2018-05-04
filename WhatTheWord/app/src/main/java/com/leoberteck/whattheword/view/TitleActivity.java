package com.leoberteck.whattheword.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;

import com.google.android.gms.games.Games;
import com.leoberteck.whattheword.BR;
import com.leoberteck.whattheword.R;
import com.leoberteck.whattheword.data.entities.ScoreEntity;
import com.leoberteck.whattheword.loaders.ScoreLoader;
import com.leoberteck.whattheword.mvp.impl.TitlePresenterImpl;
import com.leoberteck.whattheword.mvp.interfaces.Title;
import com.leoberteck.whattheword.utils.DialogUtils;
import com.leoberteck.whattheword.utils.NetworkUtilsImpl;
import com.leoberteck.whattheword.utils.SimpleIdlingResource;
import com.leoberteck.whattheword.utils.WidgetUtils;
import com.leoberteck.wtw.entities.GameStatus;

public class TitleActivity extends GoogleSignInActivity
    implements LoaderManager.LoaderCallbacks<ScoreEntity>
    , Title.TitleActivity {

    private static final int LOAD_BEST_SCORE = 1001;
    private static final int RC_LEADERBOARD_UI = 1002;
    private Title.TitlePresenter presenter;

    @Nullable
    private SimpleIdlingResource loadScoreIdlingResource;

    @NonNull
    @VisibleForTesting
    public IdlingResource getLoadScoreIdlingResource() {
        if(loadScoreIdlingResource == null){
            loadScoreIdlingResource = new SimpleIdlingResource();
        }
        return loadScoreIdlingResource;
    }

    public Title.TitlePresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_title_screen);
        presenter = ViewModelProviders.of(this).get(TitlePresenterImpl.class);
        presenter.setActivity(this);
        binding.setVariable(BR.vm, presenter);
        aquireSignIn();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadScore();
    }

    @Override
    public void loadScore() {
        if(loadScoreIdlingResource != null){ loadScoreIdlingResource.setIdleState(false); }
        LoaderManager loaderManager = getSupportLoaderManager();
        if(loaderManager.getLoader(LOAD_BEST_SCORE) != null){
            getSupportLoaderManager().restartLoader(LOAD_BEST_SCORE, null, this).forceLoad();
        } else {
            getSupportLoaderManager().initLoader(LOAD_BEST_SCORE, null, this).forceLoad();
        }
    }

    @Override
    public void sendUpdateScoreBroadCast(long newScore) {
        WidgetUtils.updatePlayWidgetBestScore(this, newScore);
    }

    @NonNull
    @Override
    public Loader<ScoreEntity> onCreateLoader(int id, Bundle args) {
        return new ScoreLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ScoreEntity> loader, ScoreEntity data) {
        presenter.setBestScore(data);
        if(loadScoreIdlingResource != null){ loadScoreIdlingResource.setIdleState(true); }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ScoreEntity> loader) {
        presenter.setBestScore(null);
    }

    @Override
    public void navigateToGameActivity(GameStatus gameStatus) {
        if(isOnline()){
            View view = findViewById(R.id.fabPlay);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this
                , view
                , "transition"
            );
            int revealX = (int) (view.getX() + view.getWidth() / 2);
            int revealY = (int) (view.getY() + view.getHeight() / 2);
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra(GameActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
            intent.putExtra(GameActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);
            ActivityCompat.startActivity(this, intent, options.toBundle());
        } else {
            showNoInternetDialog();
        }
    }

    @Override
    public void showConfirmationDialog(DialogInterface.OnClickListener onConfirmListener) {
        DialogUtils.showConfirmationDialog(this
            , R.string.warning
            , R.string.clear_score_warning
            , onConfirmListener
            , null);
    }

    @Override
    public void showLeaderBoards(){
        if(googleSignInAccount != null){
            Games.getLeaderboardsClient(this, googleSignInAccount)
                .getLeaderboardIntent(getString(R.string.leaderboard_id))
                .addOnSuccessListener(intent -> startActivityForResult(intent, RC_LEADERBOARD_UI));
        }
    }

    private void showNoInternetDialog(){
        DialogUtils.showNoServerConnectionDialog(this, null);
    }

    private boolean isOnline(){
        return NetworkUtilsImpl.getInstance().isOnline(this);
    }
}