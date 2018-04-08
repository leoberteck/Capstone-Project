package com.leoberteck.whattheword.mvp;

import android.databinding.Observable;

public interface BasePresenter extends Observable {
    void notifyPropertyChanged(int fieldId);

    void notifyChange();
}
