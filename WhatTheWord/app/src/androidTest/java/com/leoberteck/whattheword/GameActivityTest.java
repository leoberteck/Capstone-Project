package com.leoberteck.whattheword;

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.leoberteck.whattheword.data.dao.GameStatusDao;
import com.leoberteck.whattheword.data.dao.ScoreDao;
import com.leoberteck.whattheword.data.entities.GameStatusEntity;
import com.leoberteck.whattheword.data.entities.ScoreEntity;
import com.leoberteck.whattheword.view.GameActivity;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashSet;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class GameActivityTest {

    @Rule
    public IntentsTestRule<GameActivity> testRule = new IntentsTestRule<>(GameActivity.class, false, false);

    private static final String WORD = "banana";
    private static final String DEFINITION = "a fruit";

    @Test
    public void gameWinDialogTest(){

        insertMockGameStatusData();
        launchActivity();
        registerIdlingResources();

        onView(withId(R.id.editTextGuess)).perform(typeText(testRule.getActivity().getPresenter().getGameStatus().getWord()));
        onView(withId(R.id.buttonGuess)).perform(click());
        onView(withText(R.string.you_got_it)).check(matches(isDisplayed()));

        clearMockData();
        resetPresenter();
        unregisterIdlingResources();
    }

    @Test
    public void gameOverDialogTest(){

        insertMockGameStatusData();
        launchActivity();
        registerIdlingResources();

        onView(withId(R.id.editTextGuess)).perform(typeText("fsdsfsd"));
        onView(withId(R.id.buttonGuess)).perform(click());
        onView(withText(R.string.final_score_is)).check(matches(isDisplayed()));

        clearMockData();
        resetPresenter();
        unregisterIdlingResources();
    }

    @Test
    public void bestScoreDialogTest(){
        insertMockGameStatusData();
        insertMockScoreData();
        launchActivity();
        registerIdlingResources();

        onView(withId(R.id.editTextGuess)).perform(typeText(testRule.getActivity().getPresenter().getGameStatus().getWord()));
        onView(withId(R.id.buttonGuess)).perform(click());
        onView(withText(R.string.new_high_score)).check(matches(isDisplayed()));

        clearMockData();
        resetPresenter();
        unregisterIdlingResources();
    }

    @Test
    public void continueGameTest() {
        //Setup
        insertMockGameStatusData();
        launchActivity();
        registerIdlingResources();

        //Test
        onView(withText(DEFINITION)).check(matches(isDisplayed()));
        Assert.assertNotNull(testRule.getActivity().getPresenter().getHiddenWord());
        Assert.assertNotNull(testRule.getActivity().getPresenter().getDefinition());
        Assert.assertEquals("1", testRule.getActivity().getPresenter().getLevel());

        //Cleanup
        clearMockData();
        resetPresenter();
        unregisterIdlingResources();
    }

    @Test
    public void startNewGameTest() {
        launchActivity();
        registerIdlingResources();
        Espresso.onIdle();
        Assert.assertNotNull(testRule.getActivity().getPresenter().getHiddenWord());
        Assert.assertNotNull(testRule.getActivity().getPresenter().getDefinition());
        Assert.assertEquals("1", testRule.getActivity().getPresenter().getLevel());
        Assert.assertEquals("0", testRule.getActivity().getPresenter().getScore());
        resetPresenter();
        unregisterIdlingResources();
    }

    private void launchActivity(){
        testRule.launchActivity(new Intent());
        intending(not(isInternal())).respondWith(new ActivityResult(Activity.RESULT_OK, null));
    }

    private void registerIdlingResources(){
        IdlingRegistry.getInstance().register(testRule.getActivity().getLoadWordIdlingResource());
        IdlingRegistry.getInstance().register(testRule.getActivity().getLoadGameStatusIdlingResource());
    }

    private void unregisterIdlingResources(){
        IdlingRegistry.getInstance().unregister(testRule.getActivity().getLoadWordIdlingResource());
        IdlingRegistry.getInstance().unregister(testRule.getActivity().getLoadGameStatusIdlingResource());
    }

    private void insertMockGameStatusData(){
        GameStatusEntity gameStatus = new GameStatusEntity();
        gameStatus.setScore(1000);
        gameStatus.setHearts(1);
        gameStatus.setRevealedLetters(new ArrayList<>());
        gameStatus.setBeatenWords(new HashSet<>());
        gameStatus.setWord(WORD);
        gameStatus.setDefinition(DEFINITION);
        new GameStatusDao(getInstrumentation().getContext().getContentResolver()).insertSinc(gameStatus);
    }

    private void insertMockScoreData(){
        ScoreEntity scoreEntity = new ScoreEntity();
        scoreEntity.setScore(500);
        new ScoreDao(getInstrumentation().getContext().getContentResolver()).insertSinc(scoreEntity);
    }

    private void clearMockData(){
        new GameStatusDao(getInstrumentation().getContext().getContentResolver()).clearSinc();
        new ScoreDao(getInstrumentation().getContext().getContentResolver()).clearSinc();
    }

    private void resetPresenter(){
        testRule.getActivity().getPresenter().setGameStatus(null);
    }
}