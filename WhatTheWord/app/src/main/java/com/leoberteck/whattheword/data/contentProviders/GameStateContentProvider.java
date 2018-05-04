package com.leoberteck.whattheword.data.contentProviders;

import android.content.UriMatcher;

import com.leoberteck.whattheword.data.contract.BaseContractEntry;
import com.leoberteck.whattheword.data.contract.GameStatusContract;

public class GameStateContentProvider extends AbstractContentProvider {

    private static final int GAMESTATES = 200;
    private static final int GAMESTATE_BY_ID = 201;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(GameStatusContract.AUTHORITY, GameStatusContract.PATH, GAMESTATES);
        uriMatcher.addURI(GameStatusContract.AUTHORITY, GameStatusContract.PATH + "/#", GAMESTATE_BY_ID);
    }

    @Override
    protected UriMatcher getUriMatcher() {
        return uriMatcher;
    }

    @Override
    protected int getUriContentMatchId() {
        return GAMESTATES;
    }

    @Override
    protected int getUriContentByIdMatchId() {
        return GAMESTATE_BY_ID;
    }

    @Override
    protected BaseContractEntry getContractEntry() {
        return GameStatusContract.contractEntry;
    }
}
