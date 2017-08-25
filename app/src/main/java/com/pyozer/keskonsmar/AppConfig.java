package com.pyozer.keskonsmar;

public interface AppConfig {

    String ADDR_SERVER = "http://88.164.230.33/keskonsmar/";
    int MIN_PASS_LENGTH = 5;

    String PREF_KEY_ACCOUNT = "account";
    String PREF_KEY_ACCOUNT_ID = "id";
    String PREF_KEY_ACCOUNT_PSEUDO = "pseudo";
    String PREF_KEY_ACCOUNT_PASSWORD = "password";
    String PREF_KEY_IS_LOGGEDIN = "isLoggedIn";

    String INTENT_EXTRA_KEY = "SNACKBAR_MESSAGE";

    int TYPE_DATA_RECENT = 0;
    int TYPE_DATA_TREND = 1;
    int TYPE_DATA_USER = 2;
    int TYPE_DATA_SEARCH = 3;

}