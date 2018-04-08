package com.leoberteck.whattheword.contentProviders;

import android.content.UriMatcher;

import com.leoberteck.whattheword.contract.BaseContractEntry;
import com.leoberteck.whattheword.contract.ScoreContract;

public class ScoreContentProvider extends AbstractContentProvider {

    private static final int SCORES = 100;
    private static final int SCORE_BY_ID = 101;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ScoreContract.AUTHORITY, ScoreContract.PATH, SCORES);
        uriMatcher.addURI(ScoreContract.AUTHORITY, ScoreContract.PATH + "#", SCORE_BY_ID);
    }

    @Override
    protected UriMatcher getUriMatcher() {
        return uriMatcher;
    }

    @Override
    protected int getUriContentMatchId() {
        return SCORES;
    }

    @Override
    protected int getUriContentByIdMatchId() {
        return SCORE_BY_ID;
    }

    @Override
    protected BaseContractEntry getContractEntry() {
        return ScoreContract.contractEntry;
    }
}
