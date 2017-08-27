package com.pyozer.keskonsmar;

public class Auteur {

    private int id_user;
    private String pseudo_user;

    public Auteur(int id_user, String pseudo_user) {

        this.id_user = id_user;
        this.pseudo_user = pseudo_user;
    }

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
}
