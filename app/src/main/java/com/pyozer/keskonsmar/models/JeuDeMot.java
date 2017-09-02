package com.pyozer.keskonsmar.models;

import java.util.HashMap;
import java.util.Map;

public class JeuDeMot {

    public String uid;
    public String jdm;
    public String auteur;
    public String date;

    public Map<String, Boolean> likes = new HashMap<>();
    public Map<String, Boolean> dislikes = new HashMap<>();

    public int nbLikes = 0;
    public int nbDislikes = 0;
    public int degreJdm = 0;

    public JeuDeMot() {
    }

    public JeuDeMot(String uid, String auteur, String jdm) {
        this(uid, auteur, jdm, String.valueOf(System.currentTimeMillis()));
    }

    public JeuDeMot(String uid, String auteur, String jdm, String date) {
        this.uid = uid;
        this.jdm = jdm;
        this.auteur = auteur;
        this.date = date;
    }

    public int getNbLikes() {
        return nbLikes;
    }
    public int getNbDislikes() {
        return nbDislikes;
    }

    public void like(String userUID) {
        if(dislikes.containsKey(userUID)) {
            dislikes.remove(userUID);
            nbDislikes--;
        }
        if(likes.containsKey(userUID)) {
            likes.remove(userUID);
            nbLikes--;
        } else {
            likes.put(userUID, true);
            nbLikes++;
        }
        setDegreJdm();
    }

    public void dislike(String userUID) {
        if(likes.containsKey(userUID)) {
            likes.remove(userUID);
            nbLikes--;
        }
        if(dislikes.containsKey(userUID)) {
            dislikes.remove(userUID);
            nbDislikes--;
        } else {
            dislikes.put(userUID, true);
            nbDislikes++;
        }
        setDegreJdm();
    }

    public void setDegreJdm() {
        degreJdm = getNbLikes() - getNbDislikes();
    }

    public int getDegreJdm() {
        return degreJdm;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("auteur", auteur);
        result.put("jdm", jdm);
        result.put("likes", likes);
        result.put("dislikes", dislikes);
        result.put("nbLikes", nbLikes);
        result.put("nbDislikes", nbDislikes);
        result.put("degreJdm", getDegreJdm());
        result.put("date", date);

        return result;
    }
}
