package com.pyozer.keskonsmar;

import java.util.Date;

public class JeuDeMot {

    private String auteur;
    private String date;
    private String jeuDeMot;

    public JeuDeMot(String jeuDeMot, String auteur, String date) {
        this.auteur = "par " + auteur;
        this.date = date;
        this.jeuDeMot = jeuDeMot;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getJeuDeMot() {
        return jeuDeMot;
    }

    public void setJeuDeMot(String jeuDeMot) {
        this.jeuDeMot = jeuDeMot;
    }
}
