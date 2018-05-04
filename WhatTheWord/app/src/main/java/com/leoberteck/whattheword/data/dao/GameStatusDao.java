package com.leoberteck.whattheword.data.dao;

import android.content.ContentResolver;
import android.support.annotation.NonNull;

import com.leoberteck.whattheword.data.contract.GameStatusContract;
import com.leoberteck.whattheword.data.entities.GameStatusEntity;

public class GameStatusDao extends AbstractDao<GameStatusEntity, GameStatusContract.GameStatusContractEntry> {
    public GameStatusDao(@NonNull ContentResolver contentResolver) {
        super(GameStatusContract.contractEntry, contentResolver);
    }
}
