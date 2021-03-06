package com.leoberteck.whattheword.data.contract;

import android.content.ContentValues;
import android.net.Uri;

import com.leoberteck.whattheword.data.entities.ScoreEntity;
import com.leoberteck.whattheword.utils.CursorWrapper;

public class ScoreContract {

    public static final String AUTHORITY = "com.leoberteck.whattheword.score";
    public static final String PATH = "score";
    public static final Uri baseUri = Uri.parse("content://" + AUTHORITY);
    public static final ScoreContractEntry contractEntry = new ScoreContractEntry();

    public static class ScoreContractEntry implements BaseContractEntry<ScoreEntity> {

        public static final Uri contentUri = baseUri.buildUpon().appendPath(PATH).build();
        public static final String TABLE_NAME = "SCORE";
        public static final String SCORE = "SCORE";

        private ScoreContractEntry() { }

        public static final String DDL =
            "CREATE TABLE " + TABLE_NAME + "("
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SCORE + " INTEGER "
            + ");";

        @Override
        public ScoreEntity deserialize(CursorWrapper cursor, int position) {
            ScoreEntity score = new ScoreEntity();
            score.setId(cursor.getLong(_ID, 0L));
            score.setScore(cursor.getLong(SCORE, 0L));
            return score;
        }

        @Override
        public ContentValues serialize(ScoreEntity entity) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(_ID, entity.getId());
            contentValues.put(SCORE, entity.getScore());
            return contentValues;
        }

        @Override
        public String[] getColumns() {
            return new String[]{ _ID, SCORE };
        }

        @Override
        public String getTableName() {
            return TABLE_NAME;
        }

        @Override
        public Uri getContentUri() {
            return contentUri;
        }
    }
}
