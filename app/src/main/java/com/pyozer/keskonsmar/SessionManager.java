package com.pyozer.keskonsmar;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {

    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    private SharedPreferences pref;

    private Editor editor;
    private Context _context;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(Constants.PREF_KEY_ACCOUNT, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void login(int id_user, String pseudo_user, String password_user) {

        editor.putInt(Constants.PREF_KEY_ACCOUNT_ID, id_user);
        editor.putString(Constants.PREF_KEY_ACCOUNT_PSEUDO, pseudo_user);
        editor.putString(Constants.PREF_KEY_ACCOUNT_PASSWORD, password_user);

        editor.putBoolean(Constants.PREF_KEY_IS_LOGGEDIN, true);

        // commit changes
        editor.apply();

        Log.d(TAG, "Login utilisateur modifiés !");
    }

    public void logout() {

        editor.remove(Constants.PREF_KEY_ACCOUNT_ID);
        editor.remove(Constants.PREF_KEY_ACCOUNT_PSEUDO);
        editor.remove(Constants.PREF_KEY_ACCOUNT_PASSWORD);

        editor.putBoolean(Constants.PREF_KEY_IS_LOGGEDIN, false);

        // commit changes
        editor.apply();

        Log.d(TAG, "Login utilisateur supprimés !");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(Constants.PREF_KEY_IS_LOGGEDIN, false);
    }
}