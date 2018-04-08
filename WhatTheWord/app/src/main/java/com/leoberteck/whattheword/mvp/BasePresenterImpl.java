package com.leoberteck.whattheword.mvp;

import android.arch.lifecycle.ViewModel;
import android.databinding.BaseObservable;

public abstract class BasePresenterImpl extends ViewModel implements BasePresenter {

    private BaseObservable observable = new BaseObservable();

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
       observable.addOnPropertyChangedCallback(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        observable.removeOnPropertyChangedCallback(callback);
    }

    @Override
    public void notifyPropertyChanged(int fieldId) {
        observable.notifyPropertyChanged(fieldId);
    }

    @Override
    public void notifyChange() {
        observable.notifyChange();
    }
}
