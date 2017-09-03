package com.pyozer.keskonsmar.fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class WorstJdmFragment extends JDMFragment {

    public WorstJdmFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("posts").orderByChild("nbDislikes");
    }
}