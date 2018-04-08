package com.leoberteck.whattheword.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.leoberteck.whattheword.contract.ScoreContract;
import com.leoberteck.whattheword.entities.Score;
import com.leoberteck.whattheword.utils.CursorWrapper;

public class ScoreLoader extends AsyncTaskLoader<Score>{

    public ScoreLoader(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    public Score loadInBackground() {
        Score score = null;
        Cursor cursor = getContext().getContentResolver().query(
            ScoreContract.contractEntry.getContentUri()
            , ScoreContract.contractEntry.getColumns()
            , null
            , null
            , null
        );

        if(cursor != null && cursor.moveToFirst()){
            CursorWrapper cursorWrapper = CursorWrapper.wrap(cursor);
            score = ScoreContract.contractEntry.deserialize(cursorWrapper, cursorWrapper.getPosition());
            cursorWrapper.close();
        }
        return score;
    }
}