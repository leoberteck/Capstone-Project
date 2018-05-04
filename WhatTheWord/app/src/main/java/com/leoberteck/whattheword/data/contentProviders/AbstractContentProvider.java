package com.leoberteck.whattheword.data.contentProviders;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.leoberteck.whattheword.data.contract.BaseContractEntry;
import com.leoberteck.whattheword.utils.DbHelper;

public abstract class AbstractContentProvider extends ContentProvider {

    protected SQLiteOpenHelper sqLiteOpenHelper;

    @Override
    public boolean onCreate() {
        sqLiteOpenHelper = DbHelper.getInstance(getContext());
        return true;
    }

    @NonNull
    @Override
    public Cursor query(
            @NonNull Uri uri
            , @Nullable String[] projection
            , @Nullable String selection
            , @Nullable String[] selectionArgs
            , @Nullable String sortOrder) {
        final SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        int match = getUriMatcher().match(uri);
        Cursor cursor;
        if(match != -1){
            if (match == getUriContentByIdMatchId()){
                String id = uri.getPathSegments().get(1);
                selection = BaseColumns._ID + "= ? ";
                selectionArgs = new String[]{id};
            }
            cursor =  db.query(getContractEntry().getTableName(),
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        assert cursor != null;
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        int match = getUriMatcher().match(uri);
        Uri returnUri;
        if(match == getUriContentMatchId()){
            long id = db.insert(getContractEntry().getTableName(), null, contentValues);
            if ( id > 0 ) {
                returnUri = ContentUris.withAppendedId(getContractEntry().getContentUri(), id);
            } else {
                throw new android.database.SQLException("Failed to insert row into " + uri);
            }
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        int match = getUriMatcher().match(uri);
        int tasksDeleted;
        if(match != -1){
            if (match == getUriContentByIdMatchId()){
                String id = uri.getPathSegments().get(1);
                selection = BaseColumns._ID + "= ? ";
                selectionArgs = new String[]{id};
            }
            tasksDeleted =  db.delete(getContractEntry().getTableName(), selection, selectionArgs);
            getContext().getContentResolver().notifyChange(getContractEntry().getContentUri(), null);
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (tasksDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        int match = getUriMatcher().match(uri);
        int tasksUpdated;
        if(match != -1){
            if (match == getUriContentByIdMatchId()){
                String id = uri.getPathSegments().get(1);
                selection = BaseColumns._ID + "= ? ";
                selectionArgs = new String[]{id};
            }
            tasksUpdated =  db.update(getContractEntry().getTableName(), contentValues, selection, selectionArgs);
            getContext().getContentResolver().notifyChange(getContractEntry().getContentUri(), null);
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (tasksUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return tasksUpdated;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    protected abstract UriMatcher getUriMatcher();
    protected abstract int getUriContentMatchId();
    protected abstract int getUriContentByIdMatchId();
    protected abstract BaseContractEntry getContractEntry();
}