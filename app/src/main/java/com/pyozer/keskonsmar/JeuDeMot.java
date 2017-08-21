package com.pyozer.keskonsmar;

import java.util.Date;

public class JeuDeMot {

    private String auteur;
    private Date date;
    private String jeuDeMot;
    private int like;
    private int dislike;

    public JeuDeMot(String jeuDeMot, String auteur, Date date, int like, int dislike) {
        this.auteur = auteur;
        this.date = date;
        this.jeuDeMot = jeuDeMot;
        this.like = like;
        this.dislike = dislike;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getJeuDeMot() {
        return jeuDeMot;
    }

    public void setJeuDeMot(String jeuDeMot) {
        this.jeuDeMot = jeuDeMot;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }
}
