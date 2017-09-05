package com.pyozer.keskonsmar;

public class Util {

    public static String getDateFormated(long date) {

        long ecart = (System.currentTimeMillis() - date) / 1000;

        if(ecart < 0) ecart = 0;

        if (ecart < 60) return (int) ecart + "sec";
        else if (ecart < 3600) return (int) Math.floor(ecart / 60) + "min";
        else if (ecart < 86400) return (int) Math.floor(ecart / 3600) + "h";
        else return (int) Math.floor(ecart / 86400) + "j";
    }

    public static String getAuteurWithArobase(String auteur) {
        return "@" + auteur;
    }

    public static String getDegreWithSymbol(int degre) {
        return degre + "°";
    }
}
