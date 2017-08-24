package com.pyozer.keskonsmar;

public class JeuDeMot {

    private int id_jdm;
    private String text_jdm;
    private String auteur_jdm;
    private String date_jdm;

    public JeuDeMot(int id_jdm, String text_jdm, String auteur_jdm, String date_jdm) {
        this.id_jdm = id_jdm;
        this.auteur_jdm = "@" + auteur_jdm;
        this.date_jdm = date_jdm;
        this.text_jdm = text_jdm;
    }

    public int getId() {
        return id_jdm;
    }

    public void setId(int id) {
        this.id_jdm = id;
    }

    public String getAuteur() {
        return auteur_jdm;
    }

    public void setAuteur(String auteur) {
        this.auteur_jdm = auteur;
    }

    public String getDate() {
        return date_jdm;
    }

    public void setDate(String date) {
        this.date_jdm = date;
    }

    public String getJeuDeMot() {
        return text_jdm;
    }

    public void setJeuDeMot(String jeuDeMot) {
        this.text_jdm = jeuDeMot;
    }

    public String toString() {
        return text_jdm + " " + auteur_jdm;
    }
}
