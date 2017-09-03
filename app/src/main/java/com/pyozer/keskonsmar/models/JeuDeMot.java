package com.pyozer.keskonsmar.models;

import java.util.HashMap;
import java.util.Map;

public class JeuDeMot {

    public String uid;
    public String jdm;
    public String auteur;
    public long timestamp;

    public Map<String, Boolean> likes = new HashMap<>();
    public Map<String, Boolean> dislikes = new HashMap<>();

    public int nbLikes = 0;
    public int nbDislikes = 0;
    public int degreJdm = 0;

    public JeuDeMot() {
    }

    public JeuDeMot(String uid, String auteur, String jdm) {
        this.uid = uid;
        this.jdm = jdm;
        this.auteur = auteur;
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
        degreJdm = nbLikes - nbDislikes;
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
        result.put("degreJdm", degreJdm);

        return result;
    }
}
