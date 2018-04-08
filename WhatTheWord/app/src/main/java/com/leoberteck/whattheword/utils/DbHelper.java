package com.leoberteck.whattheword.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.leoberteck.whattheword.contract.GameStatusContract;
import com.leoberteck.whattheword.contract.ScoreContract;

public class DbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "wtwdb";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ScoreContract.ScoreContractEntry.DDL);
        db.execSQL(GameStatusContract.GameStatusContractEntry.DDL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
