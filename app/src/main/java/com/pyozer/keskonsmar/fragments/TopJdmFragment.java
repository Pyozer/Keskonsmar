package com.pyozer.keskonsmar.fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class TopJdmFragment extends JDMFragment {

    public TopJdmFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("posts").orderByChild("nbLikes");
    }
}