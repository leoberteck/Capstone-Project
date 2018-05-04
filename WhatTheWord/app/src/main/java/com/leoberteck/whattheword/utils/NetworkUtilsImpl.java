package com.leoberteck.whattheword.utils;

public class NetworkUtilsImpl implements NetworkUtilsIterface {
    private static NetworkUtilsIterface instance = new NetworkUtilsImpl();

    public static NetworkUtilsIterface getInstance() {
        return instance;
    }

    public static void setInstance(NetworkUtilsIterface instance) {
        NetworkUtilsImpl.instance = instance;
    }
}
