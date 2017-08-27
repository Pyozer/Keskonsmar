package com.pyozer.keskonsmar;

public class JeuDeMot {

    private int id_jdm;
    private String text_jdm;
    private Auteur auteur_jdm;
    private String date_jdm;
    private int like_jdm;
    private int dislike_jdm;

    public JeuDeMot(int id_jdm, String text_jdm, Auteur auteur_jdm, String date_jdm, int like_jdm, int dislike_jdm) {
        this.id_jdm = id_jdm;
        this.auteur_jdm = auteur_jdm;
        this.date_jdm = date_jdm;
        this.text_jdm = text_jdm;
        this.like_jdm = like_jdm;
        this.dislike_jdm = dislike_jdm;
    }

    public int getIdJdm() {
        return id_jdm;
    }

    public void setIdJdm(int id_jdm) {
        this.id_jdm = id_jdm;
    }

    public String getTextJdm() {
        return text_jdm;
    }

    public void setTextJdm(String text_jdm) {
        this.text_jdm = text_jdm;
    }

    public Auteur getAuteurJdm() {
        return auteur_jdm;
    }

    public void setAuteurJdm(Auteur auteur_jdm) {
        this.auteur_jdm = auteur_jdm;
    }

    public String getDateJdm() {
        return date_jdm;
    }

    public void setDateJdm(String date_jdm) {
        this.date_jdm = date_jdm;
    }

    public int getLikeJdm() {
        return like_jdm;
    }

    public void setLikeJdm(int like_jdm) {
        this.like_jdm = like_jdm;
    }

    public int getDislikeJdm() {
        return dislike_jdm;
    }

    public void setDislikeJdm(int dislike_jdm) {
        this.dislike_jdm = dislike_jdm;
    }

    public int getDegreJdm() {
        return getLikeJdm() - getDislikeJdm();
    }
}
