package com.pyozer.keskonsmar.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.pyozer.keskonsmar.R;
import com.pyozer.keskonsmar.models.JeuDeMot;
import com.pyozer.keskonsmar.viewholder.JdmViewHolder;

public abstract class JDMFragment extends Fragment {

    private static final String TAG = "JDMFragment";

    private DatabaseReference mDatabase;
    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter mAdapter;

    private ProgressBar mSpinner;
    private CardView mJdmEmpty;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_jdm, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        mRecyclerView = mView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        mJdmEmpty = mView.findViewById(R.id.jdm_empty);

        mSpinner = mView.findViewById(R.id.loader);
        showElement(mSpinner);
        hideElement(mRecyclerView);
        hideElement(mJdmEmpty);

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);
        mAdapter = new FirebaseRecyclerAdapter<JeuDeMot, JdmViewHolder>(JeuDeMot.class, R.layout.jeu_de_mot_row, JdmViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final JdmViewHolder viewHolder, final JeuDeMot model, final int position) {
                final DatabaseReference postRef = getRef(position);

                // On regarde si l'utilisateur à déjà like ou dislike
                viewHolder.like.setColorFilter(getResources().getColor(R.color.iconColor));
                viewHolder.dislike.setColorFilter(getResources().getColor(R.color.iconColor));

                if (model.likes.containsKey(getUid())) {
                    viewHolder.like.setColorFilter(getResources().getColor(R.color.likeColor));
                } else if (model.dislikes.containsKey(getUid())) {
                    viewHolder.dislike.setColorFilter(getResources().getColor(R.color.dislikeColor));
                }

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "onItemClickGeneral: position " + position);

                        showDeleteAlert(postRef.getKey(), model);
                    }
                });

                // Bind Post to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToPost(model, new View.OnClickListener() {

                    @Override
                    public void onClick(View likeView) {
                        // Run transactions
                        mDatabase.child("posts").child(postRef.getKey()).runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData mutableData) {
                                JeuDeMot jdm = mutableData.getValue(JeuDeMot.class);
                                if (jdm == null) {
                                    return Transaction.success(mutableData);
                                }

                                jdm.like(getUid());

                                // Set value and report transaction success
                                mutableData.setValue(jdm);
                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                // Transaction completed
                                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                            }
                        });
                    }
                }, new View.OnClickListener() {

                    @Override
                    public void onClick(View dislikeView) {
                        mDatabase.child("posts").child(postRef.getKey()).runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData mutableData) {
                                JeuDeMot jdm = mutableData.getValue(JeuDeMot.class);
                                if (jdm == null) {
                                    return Transaction.success(mutableData);
                                }

                                jdm.dislike(getUid());

                                // Set value and report transaction success
                                mutableData.setValue(jdm);
                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                // Transaction completed
                                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                            }
                        });
                    }
                });
            }
        };
        postsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideElement(mSpinner);
                if(!dataSnapshot.exists()) {
                    showElement(mJdmEmpty);
                    hideElement(mRecyclerView);
                } else {
                    showElement(mRecyclerView);
                    hideElement(mJdmEmpty);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showDeleteAlert(final String postKey, final JeuDeMot jeuDeMot) {
        if (jeuDeMot.uid.equals(getUid())) {

            AlertDialog.Builder alertDelete = new AlertDialog.Builder(getContext());
            alertDelete.setTitle("Supprimer le JDM");
            alertDelete.setMessage("Voulez-vous vraiment supprimer ce JDM ?");
            alertDelete.setCancelable(true);

            alertDelete.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mDatabase.child("posts").child(postKey).removeValue();
                    dialog.cancel();
                }
            });

            alertDelete.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog alert = alertDelete.create();
            alert.show();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);

    private void showElement(View elem) {
        elem.setVisibility(View.VISIBLE);
    }
    private void hideElement(View elem) {
        elem.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }
}