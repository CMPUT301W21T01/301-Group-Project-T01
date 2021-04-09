package com.example.experimentify;

import androidx.annotation.Nullable;

public class LocalUserSingleton {
    private static User localUser = null;

    public static User getLocalUser() {
        return localUser;
    }

    public static void setLocalUser(User initialUser) {
        if (initialUser != null) {
            localUser = initialUser;
        }
    }

}
