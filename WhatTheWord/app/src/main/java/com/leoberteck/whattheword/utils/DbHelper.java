package com.leoberteck.whattheword.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.leoberteck.whattheword.BuildConfig;
import com.leoberteck.whattheword.data.contract.GameStatusContract;
import com.leoberteck.whattheword.data.contract.ScoreContract;

public class DbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    private static SQLiteOpenHelper instance;

    private DbHelper(Context context, String name) {
        super(context, name, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ScoreContract.ScoreContractEntry.DDL);
        db.execSQL(GameStatusContract.GameStatusContractEntry.DDL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static SQLiteOpenHelper getInstance(Context context) {
        if(instance == null){
            instance = new DbHelper(context, BuildConfig.DB_NAME);
        }
        return instance;
    }
}
