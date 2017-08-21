package com.pyozer.keskonsmar;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class JeuDeMotAdapter extends RecyclerView.Adapter<JeuDeMotAdapter.MyViewHolder> {

    private List<JeuDeMot> jeuDeMotList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView auteur;
        public TextView date;
        public TextView jeuDeMot;
        public Button like;
        public Button dislike;

        public MyViewHolder(View view) {
            super(view);
            auteur = (TextView) view.findViewById(R.id.auteur);
            date = (TextView) view.findViewById(R.id.date);
            jeuDeMot = (TextView) view.findViewById(R.id.jeuDeMot);
            like = (Button) view.findViewById(R.id.like);
            dislike = (Button) view.findViewById(R.id.dislike);
        }
    }

    public JeuDeMotAdapter(List<JeuDeMot> moviesList) {
        this.jeuDeMotList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.jeu_de_mot_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        JeuDeMot jeuDeMot = jeuDeMotList.get(position);
        holder.auteur.setText(jeuDeMot.getAuteur());
        holder.date.setText(jeuDeMot.getDate().toString());
        holder.jeuDeMot.setText(jeuDeMot.getJeuDeMot());
    }

    @Override
    public int getItemCount() {
        return jeuDeMotList.size();
    }
}