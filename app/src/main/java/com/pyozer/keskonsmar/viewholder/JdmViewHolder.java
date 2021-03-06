package com.pyozer.keskonsmar.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pyozer.keskonsmar.R;
import com.pyozer.keskonsmar.Util;
import com.pyozer.keskonsmar.models.JeuDeMot;

public class JdmViewHolder extends RecyclerView.ViewHolder {

    public TextView auteur;
    public TextView date;
    public TextView jeuDeMot;
    public TextView degre;

    public ImageView like;
    public ImageView dislike;
    public TextView nbLike;
    public TextView nbDislike;

    public JdmViewHolder(View itemView) {
        super(itemView);

        auteur = itemView.findViewById(R.id.jdm_auteur);
        date = itemView.findViewById(R.id.jdm_date);
        jeuDeMot = itemView.findViewById(R.id.jdm_jeuDeMot);
        degre = itemView.findViewById(R.id.jdm_degre);

        like = itemView.findViewById(R.id.jdm_like_img);
        dislike = itemView.findViewById(R.id.jdm_dislike_img);

        nbLike = itemView.findViewById(R.id.jdm_like);
        nbDislike = itemView.findViewById(R.id.jdm_dislike);
    }

    public void bindToPost(JeuDeMot jdm, View.OnClickListener likeClickListener, View.OnClickListener dislikeClickListener) {
        auteur.setText(Util.getAuteurWithArobase(jdm.auteur));
        date.setText(Util.getDateFormated(jdm.timestamp));
        jeuDeMot.setText(jdm.jdm);

        degre.setText(Util.getDegreWithSymbol(jdm.degreJdm));

        nbLike.setText(String.valueOf(jdm.nbLikes));
        nbDislike.setText(String.valueOf(jdm.nbDislikes));

        like.setOnClickListener(likeClickListener);
        dislike.setOnClickListener(dislikeClickListener);
    }
}