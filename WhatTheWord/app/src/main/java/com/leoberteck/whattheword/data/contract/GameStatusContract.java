package com.leoberteck.whattheword.data.contract;

import android.content.ContentValues;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.leoberteck.whattheword.data.entities.GameStatusEntity;
import com.leoberteck.whattheword.utils.CursorWrapper;

import java.util.List;
import java.util.Set;

public class GameStatusContract {

    public static final String AUTHORITY = "com.leoberteck.whattheword.game_state";
    public static final String PATH = "game_status";
    public static final Uri baseUri = Uri.parse("content://" + AUTHORITY);
    public static final GameStatusContractEntry contractEntry = new GameStatusContractEntry();

    private static final Gson gson = new GsonBuilder().create();

    private GameStatusContract() { }

    public static class GameStatusContractEntry implements BaseContractEntry<GameStatusEntity>{

        public static final Uri contentUri = baseUri.buildUpon().appendPath(PATH).build();
        public static final String TABLE_NAME = "GAME_STATUS";
        public static final String SCORE = "SCORE";
        public static final String DEFINITION = "DEFINITION";
        public static final String WORD = "WORD";
        public static final String REVEALED_LETTERS = "REVEALED_LETTERS";
        public static final String HEARTS = "HEARTS";
        public static final String BEATEN_WORDS = "BEATEN_WORDS";

        public static final String DDL =
            "CREATE TABLE " + TABLE_NAME + "("
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SCORE + " INTEGER, "
            + DEFINITION + " VARCHAR(500), "
            + WORD + " VARCHAR(255), "
            + REVEALED_LETTERS + " VARCHAR(255), "
            + BEATEN_WORDS + " VARCHAR(2000), "
            + HEARTS + " INTEGER "
            + ");";

        GameStatusContractEntry() { }

        @Override
        public GameStatusEntity deserialize(CursorWrapper cursor, int position) {
            GameStatusEntity gameStatusEntity = new GameStatusEntity();
            gameStatusEntity.setId(cursor.getLong(_ID, 0L));
            gameStatusEntity.setBeatenWords(
                    gson.fromJson(cursor.getString(BEATEN_WORDS, "[]")
                    , new TypeToken<Set<Long>>(){}.getType())
            );
            gameStatusEntity.setDefinition(cursor.getString(DEFINITION, null));
            gameStatusEntity.setHearts(cursor.getInt(HEARTS, 0));
            gameStatusEntity.setRevealedLetters(
                    gson.fromJson(cursor.getString(REVEALED_LETTERS, "[]")
                    , new TypeToken<List<Integer>>(){}.getType())
            );
            gameStatusEntity.setScore(cursor.getLong(SCORE, 0L));
            gameStatusEntity.setWord(cursor.getString(WORD, null));
            return gameStatusEntity;
        }

        @Override
        public ContentValues serialize(GameStatusEntity entity) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(_ID, entity.getId());
            contentValues.put(SCORE, entity.getScore());
            contentValues.put(DEFINITION, entity.getDefinition());
            contentValues.put(WORD, entity.getWord());
            contentValues.put(REVEALED_LETTERS, gson.toJson(entity.getRevealedLetters()));
            contentValues.put(HEARTS, entity.getHearts());
            contentValues.put(BEATEN_WORDS, gson.toJson(entity.getBeatenWords()));
            return contentValues;
        }

        @Override
        public String[] getColumns() {
            return new String[]{
                _ID
                , SCORE
                , DEFINITION
                , WORD
                , REVEALED_LETTERS
                , HEARTS
                , BEATEN_WORDS
            };
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
