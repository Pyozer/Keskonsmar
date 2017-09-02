package com.pyozer.keskonsmar.fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class LastJdmFragment extends JDMFragment {

    public LastJdmFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("posts");
    }
}