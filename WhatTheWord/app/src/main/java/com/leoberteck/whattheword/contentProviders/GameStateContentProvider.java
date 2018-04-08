package com.leoberteck.whattheword.contentProviders;

import android.content.UriMatcher;

import com.leoberteck.whattheword.contract.BaseContractEntry;
import com.leoberteck.whattheword.contract.GameStatusContract;

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
