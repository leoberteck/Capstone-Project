package com.leoberteck.whattheword.contract;

import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

import com.leoberteck.whattheword.entities.BaseEntity;
import com.leoberteck.whattheword.utils.CursorWrapper;

public interface BaseContractEntry<T extends BaseEntity> extends BaseColumns {

    T deserialize(CursorWrapper cursor, int position);
    ContentValues serialize(T entity);
    String[] getColumns();
    String getTableName();
    Uri getContentUri();
}
