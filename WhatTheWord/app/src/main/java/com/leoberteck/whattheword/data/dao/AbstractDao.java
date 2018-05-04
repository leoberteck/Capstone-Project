package com.leoberteck.whattheword.data.dao;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.leoberteck.whattheword.data.contract.BaseContractEntry;
import com.leoberteck.whattheword.data.entities.BaseEntity;
import com.leoberteck.whattheword.utils.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("StaticFieldLeak")
public abstract class AbstractDao<T extends BaseEntity, U extends BaseContractEntry<T>> implements AbstractDaoInterface<T, U> {

    @NonNull
    private final U contract;
    @NonNull
    private final ContentResolver contentResolver;

    AbstractDao(@NonNull U contract, @NonNull ContentResolver contentResolver) {
        this.contract = contract;
        this.contentResolver = contentResolver;
    }

    @Override
    public void query(OnTaskFinishListener<List<T>> listener) {
        new QueryInBackground<>(listener, contentResolver, contract).execute(null, null);
    }

    @Override
    public void query(String selection, String[] selectionArgs, OnTaskFinishListener<List<T>> listener) {
        new QueryInBackground<>(listener, contentResolver, contract).execute(selection, selectionArgs);
    }

    @Override
    public void findOneById(long id, OnTaskFinishListener<T> listener) {
        findOne(BaseColumns._ID + "=?", new String[]{String.valueOf(id)}, listener);
    }

    @Override
    public void findOne(String selection, String[] selectionArgs, OnTaskFinishListener<T> listener) {
        new FindOneInBackgroud<>(listener, contentResolver, contract).execute(selection, selectionArgs);
    }

    @Override
    public void insert(T entity, OnTaskFinishListener<T> listener) {
        new InsertInBackgroud<>(listener, contentResolver, contract).execute(entity);
    }

    @Override
    public void delete(long id, OnTaskFinishListener<Integer> listener) {
        new DeleteInBackground<>(listener, contentResolver, contract).execute(id);
    }

    @Override
    public void delete(T entity, OnTaskFinishListener<Integer> listener) {
        delete(entity.getId(), listener);
    }

    public void clear(OnTaskFinishListener<Integer> listener){
        new DeleteInBackground<>(listener, contentResolver, contract).execute();
    }

    @Override
    public void update(T entity, OnTaskFinishListener<Integer> listener) {
        new UpdateInBackground<>(listener, contentResolver, contract).execute(entity);
    }


    @Override
    public List<T> querySinc() {
        return querySinc(null, null);
    }

    @Override
    public List<T> querySinc(String selection, String[] selectionArgs) {
        return new QueryInBackground<>(null, contentResolver, contract).doWork(selection, selectionArgs);
    }

    @Override
    public T findOneByIdSinc(long id) {
        return findOneSinc(BaseColumns._ID + "=?", new String[]{String.valueOf(id)});
    }

    @Override
    public T findOneSinc(String selection, String[] selectionArgs) {
        return new FindOneInBackgroud<>(null, contentResolver, contract).doWork(selection, selectionArgs);
    }

    @Override
    public T insertSinc(T entity) {
        return new InsertInBackgroud<>(null, contentResolver, contract).doWork(entity);
    }

    @Override
    public Integer deleteSinc(long id) {
        return new DeleteInBackground<>(null, contentResolver, contract).doWork(id);
    }

    @Override
    public Integer deleteSinc(T entity) {
        return deleteSinc(entity.getId());
    }

    @Override
    public Integer updateSinc(T entity) {
        return new UpdateInBackground<>(null, contentResolver, contract).doWork(entity);
    }

    public void clearSinc(){
        new DeleteInBackground<>(null, contentResolver, contract).doWork();
    }

    private static class QueryInBackground<T_ENTITY extends BaseEntity, T_CONTRACT extends BaseContractEntry<T_ENTITY>> extends CallbackAsyncTask<Object, List<T_ENTITY>, T_ENTITY, T_CONTRACT> {

        QueryInBackground(
                @Nullable OnTaskFinishListener<List<T_ENTITY>> listener
                , @NonNull ContentResolver contentResolver
                , @NonNull T_CONTRACT t_contract) {
            super(listener, contentResolver, t_contract);
        }

        @Override
        List<T_ENTITY> doWork(Object... objects) {
            List<T_ENTITY> result = new ArrayList<>();
            String where = (String) objects[0];
            String[] whereArgs = (String[]) objects[1];
            CursorWrapper wrapper;
            Cursor cursor = contentResolver.query(
                    contract.getContentUri()
                    , contract.getColumns()
                    , where
                    , whereArgs
                    , null
            );
            if(cursor != null && cursor.moveToFirst()){
                wrapper = CursorWrapper.wrap(cursor);
                do{
                    result.add(contract.deserialize(wrapper, cursor.getPosition()));
                }while (cursor.moveToNext());
                wrapper.close();
            }
            return result;
        }
    }

    private static class FindOneInBackgroud<T_ENTITY extends BaseEntity, T_CONTRACT extends BaseContractEntry<T_ENTITY>> extends CallbackAsyncTask<Object, T_ENTITY, T_ENTITY, T_CONTRACT> {
        FindOneInBackgroud(
                @Nullable OnTaskFinishListener<T_ENTITY> listener
                , @NonNull ContentResolver contentResolver
                , @NonNull T_CONTRACT t_contract) {
            super(listener, contentResolver, t_contract);
        }

        @Override
        T_ENTITY doWork(Object... objects) {
            T_ENTITY result = null;
            String where = (String) objects[0];
            String[] whereArgs = (String[]) objects[1];
            CursorWrapper wrapper;
            Cursor cursor = contentResolver.query(
                contract.getContentUri()
                , contract.getColumns()
                , where
                , whereArgs
                , null
            );
            if(cursor != null && cursor.moveToFirst()){
                wrapper = CursorWrapper.wrap(cursor);
                result = contract.deserialize(wrapper, 0);
                wrapper.close();
            }
            return result;
        }
    }

    private static class InsertInBackgroud<T_ENTITY extends BaseEntity, T_CONTRACT extends BaseContractEntry<T_ENTITY>> extends CallbackAsyncTask<T_ENTITY, T_ENTITY, T_ENTITY, T_CONTRACT>{
        InsertInBackgroud(
                @Nullable OnTaskFinishListener<T_ENTITY> listener
                , @NonNull ContentResolver contentResolver
                , @NonNull T_CONTRACT t_contract) {
            super(listener, contentResolver, t_contract);
        }

        @Override
        T_ENTITY doWork(T_ENTITY... t_entities) {
            T_ENTITY toInsert = t_entities[0];
            ContentValues values = contract.serialize(toInsert);
            if(toInsert.getId() == null){
                values.remove(BaseContractEntry._ID);
            }
            Uri uri = contentResolver.insert(
                contract.getContentUri()
                , values
            );
            assert uri != null;
            String id  = uri.getPathSegments().get(1);
            try{
                toInsert.setId(Long.parseLong(id));
            }catch (Exception ignore){
                toInsert.setId(0);
            }
            return toInsert;
        }
    }

    private static class DeleteInBackground<T_ENTITY extends BaseEntity, T_CONTRACT extends BaseContractEntry<T_ENTITY>> extends CallbackAsyncTask<Long, Integer, T_ENTITY, T_CONTRACT>{

        DeleteInBackground(
                @Nullable OnTaskFinishListener<Integer> listener
                , @NonNull ContentResolver contentResolver
                , @NonNull T_CONTRACT t_contract) {
            super(listener, contentResolver, t_contract);
        }

        @Override
        Integer doWork(Long... longs) {
            Long id = longs != null && longs.length > 0 ? longs[0] : null;
            Uri uri = contract.getContentUri();
            if(id != null){
                uri = ContentUris.withAppendedId(contract.getContentUri(), id);
            }
            return contentResolver.delete(
                uri
                , null
                , null
            );
        }
    }

    private static class UpdateInBackground<T_ENTITY extends BaseEntity, T_CONTRACT extends BaseContractEntry<T_ENTITY>> extends CallbackAsyncTask<T_ENTITY, Integer, T_ENTITY, T_CONTRACT>{
        UpdateInBackground(
                @Nullable OnTaskFinishListener<Integer> listener
                , @NonNull ContentResolver contentResolver
                , @NonNull T_CONTRACT t_contract) {
            super(listener, contentResolver, t_contract);
        }

        @Override
        Integer doWork(T_ENTITY... t_entities) {
            T_ENTITY toUpdate = t_entities[0];
            return contentResolver.update(
                ContentUris.withAppendedId(contract.getContentUri(), toUpdate.getId())
                , contract.serialize(toUpdate)
                , null
                , null
            );
        }
    }

    private static abstract class CallbackAsyncTask<T_PARAM, T_RESULT, T_ENTITY extends BaseEntity, T_CONTRACT extends BaseContractEntry<T_ENTITY>> extends AsyncTask<T_PARAM, Void, T_RESULT>{
        @Nullable
        private final OnTaskFinishListener<T_RESULT> listener;
        @NonNull
        final ContentResolver contentResolver;
        @NonNull
        final T_CONTRACT contract;
        private Exception e;

        CallbackAsyncTask(
                @Nullable OnTaskFinishListener<T_RESULT> listener
                , @NonNull ContentResolver contentResolver
                , @NonNull T_CONTRACT contract) {
            this.listener = listener;
            this.contentResolver = contentResolver;
            this.contract = contract;
        }

        @Override
        protected T_RESULT doInBackground(T_PARAM[] t_params) {
            T_RESULT result = null;
            try {
                result = doWork(t_params);
            } catch (Exception e){
                this.e = e;
            }
            return result;
        }

        @Override
        protected void onPostExecute(T_RESULT t_result) {
            super.onPostExecute(t_result);
            if(listener != null){
                listener.onTaskFinish(e, t_result);
            }
        }

        abstract T_RESULT doWork(T_PARAM... params);
    }

}