package com.pyozer.keskonsmar;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class JeuDeMotAdapter extends RecyclerView.Adapter<JeuDeMotAdapter.MyViewHolder> {

    private List<JeuDeMot> jeuDeMotList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView auteur;
        TextView date;
        TextView jeuDeMot;
        TextView degre;

        MyViewHolder(View view) {
            super(view);
            auteur = (TextView) view.findViewById(R.id.jdm_auteur);
            date = (TextView) view.findViewById(R.id.jdm_date);
            jeuDeMot = (TextView) view.findViewById(R.id.jdm_jeuDeMot);
            degre = (TextView) view.findViewById(R.id.jdm_degre);
        }
    }

    JeuDeMotAdapter(List<JeuDeMot> moviesList) {
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
        holder.auteur.setText(jeuDeMot.getAuteurJdm().toString());
        holder.date.setText(jeuDeMot.getDateJdm());
        holder.jeuDeMot.setText(jeuDeMot.getTextJdm());
        holder.degre.setText(String.valueOf(jeuDeMot.getDegreJdm()) + "°");
    }

    @Override
    public int getItemCount() {
        return jeuDeMotList.size();
    }
}