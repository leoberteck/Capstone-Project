package com.leoberteck.whattheword.data.dao;

import android.content.ContentResolver;
import android.support.annotation.NonNull;

import com.leoberteck.whattheword.data.contract.ScoreContract;
import com.leoberteck.whattheword.data.entities.ScoreEntity;

public class ScoreDao extends AbstractDao<ScoreEntity, ScoreContract.ScoreContractEntry> {
    public ScoreDao(@NonNull ContentResolver contentResolver) {
        super(ScoreContract.contractEntry, contentResolver);
    }
}
