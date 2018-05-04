package com.leoberteck.whattheword.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.leoberteck.whattheword.data.contract.GameStatusContract;
import com.leoberteck.whattheword.data.entities.GameStatusEntity;
import com.leoberteck.whattheword.utils.CursorWrapper;

public class GameStateLoader extends AsyncTaskLoader<GameStatusEntity> {

    public GameStateLoader(Context context) {
        super(context);
    }

    @Override
    public GameStatusEntity loadInBackground() {
        GameStatusEntity gameStatusEntity = null;
        Cursor cursor = getContext().getContentResolver().query(
            GameStatusContract.contractEntry.getContentUri()
            , GameStatusContract.contractEntry.getColumns()
            , null
            , null
            , null
        );

        if(cursor != null && cursor.moveToFirst()){
            CursorWrapper cursorWrapper = CursorWrapper.wrap(cursor);
            gameStatusEntity = GameStatusContract.contractEntry.deserialize(cursorWrapper, cursorWrapper.getPosition());
            cursorWrapper.close();
        }
        return gameStatusEntity;
    }
}
