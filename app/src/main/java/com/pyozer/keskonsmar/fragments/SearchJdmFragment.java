package com.pyozer.keskonsmar.fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class SearchJdmFragment extends JDMFragment {

    private static String mQuery;

    public SearchJdmFragment() {
    }

    public static SearchJdmFragment newInstance(String query) {
        mQuery = query;

        return new SearchJdmFragment();
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("posts");
    }
}