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

    public String getDateFormated() {

        long ecart = (System.currentTimeMillis() - Long.parseLong(date)) / 1000;

        if (ecart < 60) return (int) ecart + "sec";
        else if (ecart < 3600) return (int) Math.floor(ecart / 60) + "min";
        else if (ecart < 86400) return (int) Math.floor(ecart / 3600) + "h";
        else return (int) Math.floor(ecart / 86400) + "j";
    }

    public int getNbLikes() {
        return likes.size();
    }

    public void addLike(String userUID) {
        dislikes.remove(userUID);
        likes.put(userUID, true);
    }

    public void unLike(String userUID) {
        likes.remove(userUID);
    }

    public int getNbDislikes() {
        return dislikes.size();
    }

    public void addDislike(String userUID) {
        likes.remove(userUID);
        dislikes.put(userUID, true);
    }

    public void unDislike(String userUID) {
        dislikes.remove(userUID);
    }

    public int getDegreJdm() {
        return getNbLikes() - getNbDislikes();
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("auteur", auteur);
        result.put("jdm", jdm);
        result.put("likes", likes);
        result.put("dislikes", dislikes);
        result.put("nbLikes", getNbLikes());
        result.put("nbDislikes", getNbDislikes());
        result.put("degreJdm", getDegreJdm());
        result.put("date", date);

        return result;
    }
}
