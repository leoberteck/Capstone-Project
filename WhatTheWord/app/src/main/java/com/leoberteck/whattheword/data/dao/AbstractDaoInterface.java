package com.leoberteck.whattheword.data.dao;

import com.leoberteck.whattheword.data.contract.BaseContractEntry;
import com.leoberteck.whattheword.data.entities.BaseEntity;

import java.util.List;

/**
 * Created by leonardo on 13/01/18.
 */

public interface AbstractDaoInterface<T extends BaseEntity, U extends BaseContractEntry<T>> {

    List<T> querySinc();

    List<T> querySinc(String selection, String[] selectionArgs);

    T findOneByIdSinc(long id);

    T findOneSinc(String selection, String[] selectionArgs);

    T insertSinc(T entity);

    Integer deleteSinc(long id);

    Integer deleteSinc(T entity);

    Integer updateSinc(T entity);


    void query(OnTaskFinishListener<List<T>> listener);

    void query(String selection, String[] selectionArgs, OnTaskFinishListener<List<T>> listener);

    void findOneById(long id, OnTaskFinishListener<T> listener);

    void findOne(String selection, String[] selectionArgs, OnTaskFinishListener<T> listener);

    void insert(T entity, OnTaskFinishListener<T> listener);

    void delete(long id, OnTaskFinishListener<Integer> listener);

    void delete(T entity, OnTaskFinishListener<Integer> listener);

    void update(T entity, OnTaskFinishListener<Integer> listener);

    interface OnTaskFinishListener<T>{
        void onTaskFinish(Exception ex, T result);
    }
}
