package com.leoberteck.whattheword.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.Surface;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.games.Games;
import com.leoberteck.backend.wtwapi.model.WordEntry;
import com.leoberteck.whattheword.BR;
import com.leoberteck.whattheword.R;
import com.leoberteck.whattheword.data.dao.GameStatusDao;
import com.leoberteck.whattheword.databinding.DialogGameOverBinding;
import com.leoberteck.whattheword.databinding.DialogSuccessBinding;
import com.leoberteck.whattheword.loaders.RandomWordLoader;
import com.leoberteck.whattheword.mvp.impl.GamePresenterImpl;
import com.leoberteck.whattheword.mvp.interfaces.Game;
import com.leoberteck.whattheword.utils.AnimationUtils;
import com.leoberteck.whattheword.utils.DialogUtils;
import com.leoberteck.whattheword.utils.SimpleIdlingResource;
import com.leoberteck.whattheword.utils.WidgetUtils;

public class GameActivity extends GoogleSignInActivity
    implements LoaderManager.LoaderCallbacks<WordEntry>
    , Game.GameActivity{

    public static final String EXTRA_CIRCULAR_REVEAL_X = "com.leoberteck.whattheword.view.GameActivity.reveal_x_extra";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "com.leoberteck.whattheword.view.GameActivity.reveal_y_extra";
    private static final int DURATION = 400;
    @ColorRes
    private static final int COLOR_START = R.color.colorAccent;
    @ColorRes
    private static final int COLOR_END = R.color.colorBackground;

    private Game.GamePresenter presenter;
    private int defaultOrientation;
    private int revealX;
    private int revealY;

    private static final int LOAD_WORD_ENTRY = 2001;
    private AlertDialog currentDialog;
    private View rootLayout;

    @Nullable
    private SimpleIdlingResource loadWordIdlingResource;
    @Nullable
    private SimpleIdlingResource loadGameStatusIdlingResource;

    @NonNull
    public SimpleIdlingResource getLoadWordIdlingResource() {
        if(loadWordIdlingResource == null){
            loadWordIdlingResource = new SimpleIdlingResource();
        }
        return loadWordIdlingResource;
    }

    @NonNull
    public SimpleIdlingResource getLoadGameStatusIdlingResource() {
        if(loadGameStatusIdlingResource == null){
            loadGameStatusIdlingResource = new SimpleIdlingResource();
        }
        return loadGameStatusIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = ViewModelProviders.of(this).get(GamePresenterImpl.class);
        presenter.setGameActivity(this);
        ViewDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_game);
        binding.setVariable(BR.vm, presenter);
        if(!getResources().getBoolean(R.bool.isTablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        defaultOrientation = getRequestedOrientation();

        if(presenter.getGameStatus() == null){
            if(loadGameStatusIdlingResource != null){
                loadGameStatusIdlingResource.setIdleState(false);
            }
            GameStatusDao gameStatusDao = new GameStatusDao(getContentResolver());
            gameStatusDao.findOne(null, null, (ex, result) -> {
                if(result != null && ex == null){
                    presenter.setGameStatus(result);
                } else {
                    fireLoader();
                }
                if(loadGameStatusIdlingResource != null){
                    loadGameStatusIdlingResource.setIdleState(true);
                }
            });
        }

        EditText editTextGuess = findViewById(R.id.editTextGuess);
        editTextGuess.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                presenter.onGuessClick();
                return true;
            }
            return false;
        });

        rootLayout = findViewById(R.id.rootLayout);
        final Intent intent = getIntent();
        if (savedInstanceState == null
            && intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X)
            && intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)
        ){
            rootLayout.setVisibility(View.INVISIBLE);

            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);

            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        revealActivity(revealX, revealY);
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else {
            rootLayout.setVisibility(View.VISIBLE);
        }

        aquireSignIn();
    }

    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);

            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x, y, 0, finalRadius);
            circularReveal.setDuration(DURATION);
            circularReveal.setInterpolator(new AccelerateInterpolator());
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    int colorStart = ContextCompat.getColor(GameActivity.this, COLOR_START);
                    int colorEnd = ContextCompat.getColor(GameActivity.this, COLOR_END);
                    ValueAnimator backgroundAnimator = ValueAnimator.ofArgb(colorStart, colorEnd);
                    backgroundAnimator.setDuration(DURATION * 2);
                    backgroundAnimator.addUpdateListener(animator -> rootLayout.setBackgroundColor((Integer) animator.getAnimatedValue()));
                    backgroundAnimator.start();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }
            });

            // make the view visible and start the animation
            rootLayout.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else {
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.saveState(getContentResolver(), null);
    }

    @Override
    protected void onDestroy() {
        hideDialog();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DialogUtils.showConfirmationDialog(this
            , R.string.confirmation
            , R.string.leave_game_confirmation
            , (dialog, which) -> finish()
            , null);
    }

    @Override
    public void fireLoader(){
        if(loadWordIdlingResource != null){
            loadWordIdlingResource.setIdleState(false);
        }
        LoaderManager loaderManager = getSupportLoaderManager();
        if(loaderManager.getLoader(LOAD_WORD_ENTRY) == null){
            getSupportLoaderManager().initLoader(LOAD_WORD_ENTRY, null, this).forceLoad();
        } else {
            getSupportLoaderManager().restartLoader(LOAD_WORD_ENTRY, null, this).forceLoad();
        }
        showLoading();
    }

    private void showLoading(){
        lockScreen();

        final View rootView = getLayoutInflater().inflate(R.layout.dialog_loading, null);
        final String alphabet = "abcdefghijklmnopqrstuvxwyz";

        currentDialog = new AlertDialog.Builder(this)
            .setTitle(R.string.please_wait)
            .setView(rootView)
            .setCancelable(false)
            .setOnDismissListener(dialog -> unlockScreen())
            .create();
        currentDialog.setOnShowListener(dialog -> {
            TextView textViewAnimated = rootView.findViewById(R.id.textViewAnimatedText);
            AnimationUtils.animateRange(textViewAnimated, (Object[])alphabet.split(""));
        });
        currentDialog.show();
    }

    public void hideDialog(){
        if(currentDialog != null && currentDialog.isShowing()) currentDialog.dismiss();
    }

    @Override
    public void showSuccessDialog(final boolean newHighScore, final long score, final long add, String word, final DialogInterface.OnClickListener onDialogClose) {
        hideKeyboard();

        DialogSuccessBinding dialogSuccessBinding
            = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_success, null, false);
        dialogSuccessBinding.setScore(String.valueOf(score));
        dialogSuccessBinding.setWord(word);
        dialogSuccessBinding.setHighScore(newHighScore);

        lockScreen();

        final View rootView = dialogSuccessBinding.getRoot();

        currentDialog = new AlertDialog.Builder(this)
            .setTitle(R.string.you_got_it)
            .setView(dialogSuccessBinding.getRoot())
            .setCancelable(false)
            .setPositiveButton(R.string.next_word, onDialogClose)
            .setOnDismissListener(dialog -> unlockScreen())
            .create();

        currentDialog.setOnShowListener(dialog -> {
            TextView finalScore = rootView.findViewById(R.id.textViewFinalScore);
            AnimationUtils.animateRange((int) score, (int) add, finalScore);
        });

        currentDialog.show();
    }

    @Override
    public void showGameOverDialog(long score, String word, final DialogInterface.OnClickListener onDialogClose) {
        hideKeyboard();

        DialogGameOverBinding dialogGameOverBinding
            = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_game_over, null, false);
        dialogGameOverBinding.setScore(String.valueOf(score));
        dialogGameOverBinding.setWord(word);

        lockScreen();

        currentDialog = new AlertDialog.Builder(this)
            .setTitle(R.string.game_over)
            .setView(dialogGameOverBinding.getRoot())
            .setCancelable(false)
            .setPositiveButton(R.string.play_again, onDialogClose)
            .setOnDismissListener(dialog -> unlockScreen()).create();

        currentDialog.show();
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showNoInternetDialog(){
        DialogUtils.showNoServerConnectionDialog(this, (dialog, which) -> finish());
    }

    @Override
    public void sendUpdateScoreBroadCast(long newScore) {
        WidgetUtils.updatePlayWidgetBestScore(this, newScore);
    }

    @Override
    public void publishScore(long score) {
        if(googleSignInAccount != null){
            try {
                Games.getLeaderboardsClient(this, googleSignInAccount)
                    .submitScore(getString(R.string.leaderboard_id), score);
            } catch (Exception ignore) {}
        }
    }

    @Override
    public void animateImageView(int index, Animation.AnimationListener listener) {
        int id;
        if(index == 4){
            id = R.id.imgHeart4;
        } else if(index == 3) {
            id = R.id.imgHeart3;
        } else if (index == 2) {
            id = R.id.imgHeart2;
        } else if (index == 1) {
            id = R.id.imgHeart1;
        } else {
            id = R.id.imgHeart0;
        }
        animateImageView(findViewById(id), listener);
    }

    private void animateImageView(ImageView imageView, Animation.AnimationListener listener){
        Animation animation = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_out);
        animation.setAnimationListener(listener);
        imageView.startAnimation(animation);
    }

    private void lockScreen(){
        setRequestedOrientation(getCurrentOrientation());
    }

    private void unlockScreen(){
        setRequestedOrientation(defaultOrientation);
    }

    private int getCurrentOrientation(){
        int rotation =  getWindowManager().getDefaultDisplay().getRotation();
        int orientation;
        switch (rotation) {
            case Surface.ROTATION_90:
                orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                break;
            case Surface.ROTATION_180:
                orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                break;
            case Surface.ROTATION_270:
                orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                break;
            case Surface.ROTATION_0:
            default:
                orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                break;
        }
        return orientation;
    }

    @NonNull
    @Override
    public Loader<WordEntry> onCreateLoader(int id, Bundle args) {
        return new RandomWordLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<WordEntry> loader, WordEntry data) {
        hideDialog();
        if(data != null){
            presenter.initGame(data);
        } else {
            showNoInternetDialog();
        }
        if(loadWordIdlingResource != null){
            loadWordIdlingResource.setIdleState(true);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<WordEntry> loader) {
        hideDialog();
    }

    public Game.GamePresenter getPresenter() {
        return presenter;
    }
}
