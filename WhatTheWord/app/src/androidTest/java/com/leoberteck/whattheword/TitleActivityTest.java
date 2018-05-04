package com.leoberteck.whattheword;

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.content.Context;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.leoberteck.whattheword.data.dao.ScoreDao;
import com.leoberteck.whattheword.data.entities.ScoreEntity;
import com.leoberteck.whattheword.utils.NetworkUtilsImpl;
import com.leoberteck.whattheword.utils.NetworkUtilsIterface;
import com.leoberteck.whattheword.view.GameActivity;
import com.leoberteck.whattheword.view.TitleActivity;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class TitleActivityTest {

    @Rule
    public IntentsTestRule<TitleActivity> testRule = new IntentsTestRule<>(TitleActivity.class, false, false);

    @Test
    public void loadScoreTest() {
        insertMockData();
        launchActivity();
        registerIdlingResource();

        Espresso.onIdle(() -> {
            Assert.assertEquals("10000", testRule.getActivity().getPresenter().getBestScore());
            return null;
        });

        unregisterIdlingResource();
        cleanUp();
    }

    @Test
    public void noScoreTest() {
        cleanUp();
        launchActivity();
        registerIdlingResource();

        Espresso.onIdle(() -> {
            Assert.assertNull(testRule.getActivity().getPresenter().getBestScore());
            return null;
        });

        unregisterIdlingResource();
    }

    @Test
    public void startGameTest() {
        cleanUp();
        launchActivity();
        registerIdlingResource();

        onView(withId(R.id.fabPlay)).perform(click());
        intended(allOf(
                isInternal()
                , hasComponent(GameActivity.class.getName())
                , hasExtraWithKey(GameActivity.EXTRA_CIRCULAR_REVEAL_X)
                , hasExtraWithKey(GameActivity.EXTRA_CIRCULAR_REVEAL_Y)
        ));

        unregisterIdlingResource();
    }

    @Test
    public void noConnectionTest() {
        cleanUp();
        launchActivity();
        mockOfflineConnection();
        registerIdlingResource();

        onView(withId(R.id.fabPlay)).perform(click());
        onView(withText(R.string.no_connection_to_server)).check(matches(isDisplayed()));

        unregisterIdlingResource();
    }

    private void mockOfflineConnection(){
        NetworkUtilsImpl.setInstance(new NetworkUtilsIterface() {
            @Override
            public boolean isOnline(Context context) {
                return false;
            }
        });
    }

    private void insertMockData(){
        ScoreEntity entity = new ScoreEntity();
        entity.setScore(10000);
        //Insert Initial Score Data
        new ScoreDao(getInstrumentation().getContext().getContentResolver()).insertSinc(entity);
    }

    private void cleanUp(){
        new ScoreDao(getInstrumentation().getContext().getContentResolver()).clearSinc();
    }

    private void registerIdlingResource() {
        IdlingRegistry.getInstance().register(testRule.getActivity().getLoadScoreIdlingResource());
    }

    private void unregisterIdlingResource(){
        IdlingRegistry.getInstance().unregister(testRule.getActivity().getLoadScoreIdlingResource());
    }

    private void launchActivity(){
        testRule.launchActivity(new Intent());
        intending(not(isInternal())).respondWith(new ActivityResult(Activity.RESULT_OK, null));
    }
}
