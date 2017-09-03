package com.pyozer.keskonsmar.fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class UserJdmFragment extends JDMFragment {

    private static String mUser;

    public UserJdmFragment() {
    }

    public static UserJdmFragment newInstance(String user) {
        mUser = user;

        return new UserJdmFragment();
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("posts").orderByChild("uid").equalTo(mUser);
    }
}