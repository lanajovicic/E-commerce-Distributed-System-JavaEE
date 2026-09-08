package klijent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sesija {
    private static int idKor;
    private static String korisnickoIme;
    private static String ime;
    private static String prezime;
    private static int stanjeNovca;
    private static List<String> uloge = new ArrayList<>();
    private static String adresa;
    private static String grad;
    private static boolean ulogovan = false;

    public static void prijavi(int id, String ki, String i, String p, int stanje, String u, String adr, String gr) {
        idKor = id;
        korisnickoIme = ki;
        ime = i;
        prezime = p;
        stanjeNovca = stanje;
        uloge = new ArrayList<>(Arrays.asList(u.split(",")));
        adresa = adr;
        grad = gr;
        ulogovan = true;
    }

    public static void odjavi() {
        idKor = 0;
        korisnickoIme = null;
        ime = null;
        prezime = null;
        stanjeNovca = 0;
        uloge = new ArrayList<>();
        adresa = null;
        grad = null;
        ulogovan = false;
    }

    public static boolean isUlogovan() { return ulogovan; }
    public static boolean isAdmin() { return uloge.contains("Admin"); }
    public static boolean isProdavac() { return uloge.contains("Prodavac"); }
    public static boolean isKupac() { return uloge.contains("Kupac"); }

    public static int    getIdKor() { return idKor; }
    public static String getKorisnickoIme() { return korisnickoIme; }
    public static String getIme() { return ime; }
    public static String getPrezime() { return prezime; }
    public static int    getStanjeNovca() { return stanjeNovca; }
    public static List<String> getUloge() { return uloge; }
    public static String getAdresa() { return adresa; }
    public static String getGrad() { return grad; }
}