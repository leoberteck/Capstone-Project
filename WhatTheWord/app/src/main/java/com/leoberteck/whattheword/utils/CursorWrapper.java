package com.leoberteck.whattheword.utils;


import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class CursorWrapper implements Cursor {

    @NonNull
    public static CursorWrapper wrap(Cursor cursor){
        return new CursorWrapper(cursor);
    }

    private CursorWrapper(@NonNull Cursor cursor) {
        this.cursor = cursor;
    }

    @NonNull
    private final Cursor cursor;

    private final Map<String, Integer> indexCache = new HashMap<>();

    //region Cursor implementation

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public int getPosition() {
        return cursor.getPosition();
    }

    @Override
    public boolean move(int i) {
        return cursor.move(i);
    }

    @Override
    public boolean moveToPosition(int i) {
        return cursor.moveToPosition(i);
    }

    @Override
    public boolean moveToFirst() {
        return cursor.moveToFirst();
    }

    @Override
    public boolean moveToLast() {
        return cursor.moveToLast();
    }

    @Override
    public boolean moveToNext() {
        return cursor.moveToNext();
    }

    @Override
    public boolean moveToPrevious() {
        return cursor.moveToPrevious();
    }

    @Override
    public boolean isFirst() {
        return cursor.isFirst();
    }

    @Override
    public boolean isLast() {
        return cursor.isLast();
    }

    @Override
    public boolean isBeforeFirst() {
        return cursor.isBeforeFirst();
    }

    @Override
    public boolean isAfterLast() {
        return cursor.isAfterLast();
    }

    @Override
    public int getColumnIndex(String s) {
        return cursor.getColumnIndex(s);
    }

    @Override
    public int getColumnIndexOrThrow(String s) throws IllegalArgumentException {
        return cursor.getColumnIndexOrThrow(s);
    }

    @Override
    public String getColumnName(int i) {
        return cursor.getColumnName(i);
    }

    @Override
    public String[] getColumnNames() {
        return cursor.getColumnNames();
    }

    @Override
    public int getColumnCount() {
        return cursor.getColumnCount();
    }

    @Override
    public byte[] getBlob(int i) {
        return cursor.getBlob(i);
    }

    @Override
    public String getString(int i) {
        return cursor.getString(i);
    }

    @Override
    public void copyStringToBuffer(int i, CharArrayBuffer charArrayBuffer) {
        cursor.copyStringToBuffer(i, charArrayBuffer);
    }

    @Override
    public short getShort(int i) {
        return cursor.getShort(i);
    }

    @Override
    public int getInt(int i) {
        return cursor.getInt(i);
    }

    @Override
    public long getLong(int i) {
        return cursor.getLong(i);
    }

    @Override
    public float getFloat(int i) {
        return cursor.getFloat(i);
    }

    @Override
    public double getDouble(int i) {
        return cursor.getDouble(i);
    }

    @Override
    public int getType(int i) {
        return cursor.getType(i);
    }

    @Override
    public boolean isNull(int i) {
        return cursor.isNull(i);
    }

    @Deprecated
    @Override
    public void deactivate() {
        cursor.deactivate();
    }

    @Deprecated
    @Override
    public boolean requery() {
        return cursor.requery();
    }

    @Override
    public void close() {
        cursor.close();
    }

    @Override
    public boolean isClosed() {
        return cursor.isClosed();
    }

    @Override
    public void registerContentObserver(ContentObserver contentObserver) {
        cursor.registerContentObserver(contentObserver);
    }

    @Override
    public void unregisterContentObserver(ContentObserver contentObserver) {
        cursor.unregisterContentObserver(contentObserver);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        cursor.registerDataSetObserver(dataSetObserver);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        cursor.unregisterDataSetObserver(dataSetObserver);
    }

    @Override
    public void setNotificationUri(ContentResolver contentResolver, Uri uri) {
        cursor.setNotificationUri(contentResolver, uri);
    }

    @Override
    public Uri getNotificationUri() {
        return cursor.getNotificationUri();
    }

    @Override
    public boolean getWantsAllOnMoveCalls() {
        return cursor.getWantsAllOnMoveCalls();
    }

    @Override
    public void setExtras(Bundle bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cursor.setExtras(bundle);
        }
    }

    @Override
    public Bundle getExtras() {
        return cursor.getExtras();
    }

    @Override
    public Bundle respond(Bundle bundle) {
        return cursor.respond(bundle);
    }

    //endregion

    public String getString(String s, String def){
        String result = def;
        Integer index = getIndex(s);
        if(index >= 0){
            result = getString(index);
        }
        return result;
    }

    public Long getLong(String s, Long def){
        Long result = def;
        Integer index = getIndex(s);
        if(index >= 0){
            result = getLong(index);
        }
        return result;
    }

    public Integer getInt(String s, Integer def){
        Integer result = def;
        Integer index = getIndex(s);
        if(index >= 0){
            result = getInt(index);
        }
        return result;
    }

    public Double getDouble(String s, Double def){
        Double result = def;
        Integer index = getIndex(s);
        if(index >= 0){
            result = getDouble(index);
        }
        return result;
    }

    private Integer getIndex(String s){
        if(!indexCache.containsKey(s)){
            indexCache.put(s, getColumnIndex(s));
        }
        return indexCache.get(s);
    }
}