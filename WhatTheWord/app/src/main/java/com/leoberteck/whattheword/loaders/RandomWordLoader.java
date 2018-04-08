package com.leoberteck.whattheword.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.leoberteck.backend.wtwapi.Wtwapi;
import com.leoberteck.backend.wtwapi.model.WordEntry;
import com.leoberteck.whattheword.BuildConfig;

import java.io.IOException;

public class RandomWordLoader extends AsyncTaskLoader<WordEntry> {
    private Wtwapi wtwapi = null;

    public RandomWordLoader(Context context) {
        super(context);
    }

    @Override
    public WordEntry loadInBackground() {
        if(wtwapi == null) {  // Only do this once
            Wtwapi.Builder builder = new Wtwapi.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null)
                .setRootUrl(BuildConfig.ENDPOINTS_SERVER_URL)
                .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                    @Override
                    public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                        abstractGoogleClientRequest.setDisableGZipContent(true);
                    }
                });
            wtwapi = builder.build();
        }
        WordEntry result = null;
        try {
            result = wtwapi.word().execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
