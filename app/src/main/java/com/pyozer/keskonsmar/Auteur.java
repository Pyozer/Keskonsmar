package com.pyozer.keskonsmar;

public class Auteur {

    private int id_user;
    private String pseudo_user;
    private int  nb_tendance = 0;
    private int nb_nul = 0;

    public Auteur(int id_user, String pseudo_user) {

        this.id_user = id_user;
        this.pseudo_user = pseudo_user;
    }

    public void addTendance() { this.nb_tendance++; }

    public int getNb_tendance() { return nb_tendance;}

    public void addNul() { this.nb_nul++ ;}

    public int getNb_nul() { return nb_nul;}

    public int getIdUser() {
        return id_user;
    }

    public void setIdUser(int id_user) {
        this.id_user = id_user;
    }

    public String getPseudoUser() {
        return pseudo_user;
    }

    public void setPseudoUser(String pseudo_user) {
        this.pseudo_user = pseudo_user;
    }

    public String toString() {
        return "@" + getPseudoUser();
    }

    public int getDegreAuteur() {
        return getNb_tendance() - getNb_nul();
    }
}
