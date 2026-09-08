package klijent;
import java.util.Scanner;

public class Klijent {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Dobrodosli u sistem prodaje <33");
        while (true) {
            if (!Sesija.isUlogovan()) {
                prikaziMeniOdjave();
            } else {
                prikaziMeniPrijave();
            }
        }
    }

    static void prikaziMeniOdjave() {
        System.out.println("1. Prijava");
        System.out.println("0. Izlaz");
        System.out.print("Izbor: ");
        String izbor = sc.nextLine().trim();

        switch (izbor) {
            case "1": prijava(); break;
            case "0": System.exit(0); break;
            default: System.out.println("Unesite vazecu opciju!!");
        }
    }

    static void prikaziMeniPrijave() {
        System.out.println("--> Ulogovani ste kao: " + Sesija.getKorisnickoIme());
        System.out.println("1.  Odjava");
        System.out.println("--> GRADOVI I KORISNICI");
        System.out.println("2.  Kreiraj grad (admin)");
        System.out.println("3.  Kreiraj korisnika (admin)");
        System.out.println("4.  Dodaj novac korisniku (admin)");
        System.out.println("5.  Promeni adresu korisnika (admin)");
        System.out.println("6.  Dohvati sve gradove");
        System.out.println("7.  Dohvati sve korisnike");
        System.out.println("--> ARTIKLI I KATEGORIJE");
        System.out.println("8.  Kreiraj kategoriju");
        System.out.println("9.  Kreiraj artikal");
        System.out.println("10. Menjaj cenu artikla");
        System.out.println("11. Postavi popust");
        System.out.println("12. Dohvati sve kategorije");
        System.out.println("13. Dohvati moje artikle");
        System.out.println("--> KORPA");
        System.out.println("14. Dodaj artikal u korpu");
        System.out.println("15. Obrisi artikal iz korpe");
        System.out.println("16. Dohvati korpu");
        System.out.println("--> WISHLIST");
        System.out.println("17. Dodaj u wishlist");
        System.out.println("18. Obrisi iz wishlist");
        System.out.println("19. Dohvati wishlist");
        System.out.println("--> KUPOVINA");
        System.out.println("20. Placanje");
        System.out.println("21. Moje narudzbine");
        System.out.println("22. Sve narudzbine");
        System.out.println("23. Sve transakcije");
        System.out.print("Izbor: ");
        String izbor = sc.nextLine().trim();

        switch (izbor) {
            case "1":  odjava(); break;
            case "2":  kreirajGrad(); break;
            case "3":  kreirajKorisnika(); break;
            case "4":  dodajNovac(); break;
            case "5":  promeniAdresu(); break;
            case "6":  dohvatiGradove(); break;
            case "7":  dohvatiKorisnike(); break;
            case "8":  kreirajKategoriju(); break;
            case "9":  kreirajArtikal(); break;
            case "10": menjajCenu(); break;
            case "11": postaviPopust(); break;
            case "12": dohvatiKategorije(); break;
            case "13": dohvatiMojeArtikle(); break;
            case "14": dodajUKorpu(); break;
            case "15": obrisiIzKorpe(); break;
            case "16": dohvatiKorpu(); break;
            case "17": dodajUWishlist(); break;
            case "18": obrisiIzWishlist(); break;
            case "19": dohvatiWishlist(); break;
            case "20": placanje(); break;
            case "21": mojeNarudzbine(); break;
            case "22": sveNarudzbine(); break;
            case "23": sveTransakcije(); break;
            default: System.out.println("Nepoznata opcija :(");
        }
    }

    static void prijava() {
        System.out.print("Korisnicko ime: ");
        String ki = sc.nextLine().trim();
        System.out.print("Sifra: ");
        String sifra = sc.nextLine().trim();
        String odgovor = HttpKlijent.post("/login", ki + ";" + sifra);
        System.out.println("Odgovor: " + odgovor);

        // odgovor format: OK;idKor;korisnickoIme;ime;prezime;stanjeNovca;uloge;adresa;grad
        if (odgovor.startsWith("OK")) {
            String[] d = odgovor.split(";");
            Sesija.prijavi(Integer.parseInt(d[1]), d[2], d[3], d[4], 
                           Integer.parseInt(d[5]), d[6], d[7], d[8]);
            System.out.println("Dobrodosliii u nasu prodavnicuuu  " + Sesija.getIme() + " :)");
            System.out.println("Uloge: " + Sesija.getUloge());
        } else {
            System.out.println("Prijava neuspesna :(");
        }
    }

    static void odjava() {
        Sesija.odjavi();
        System.out.println("Uspesno ste se odjavili.");
    }

    static void kreirajGrad() {
    if (!Sesija.isAdmin()) {
        System.out.println("Nemate dozvolu za ovu akciju!");
        return;
    }
    System.out.print("Naziv grada: ");
    String naziv = sc.nextLine().trim();
    System.out.println(HttpKlijent.post("/grad", naziv));
}

    static void kreirajKorisnika() {
        if (!Sesija.isAdmin()) {
            System.out.println("Nemate dozvolu za ovu akciju!");
            return;
        }
        System.out.print("Korisnicko ime: ");
        String ki = sc.nextLine().trim();
        System.out.print("Sifra: ");   
        String sifra = sc.nextLine().trim();
        System.out.print("Ime: ");
        String ime = sc.nextLine().trim();
        System.out.print("Prezime: ");
        String prez = sc.nextLine().trim();
        System.out.print("Adresa: ");
        String adr = sc.nextLine().trim();
        System.out.print("ID grada: ");
        String idG = sc.nextLine().trim();
        String body = ki+";"+sifra+";"+ime+";"+prez+";"+adr+";"+idG;
        System.out.println(HttpKlijent.post("/korisnik", body));
    }

    static void dodajNovac() {
        if (!Sesija.isAdmin()) {
            System.out.println("Nemate dozvolu za ovu akciju!");
            return;
        }
        System.out.print("ID korisnika: ");
        String idK = sc.nextLine().trim();
        System.out.print("Iznos: ");
        String iznos = sc.nextLine().trim();
        System.out.println(HttpKlijent.put("/korisnik/" + idK + "/novac", iznos));
    }

    static void promeniAdresu() {
        if (!Sesija.isAdmin()) {
            System.out.println("Nemate dozvolu za ovu akciju!");
            return;
        }
        System.out.print("ID korisnika: ");
        String idK = sc.nextLine().trim();
        System.out.print("Nova adresa: ");
        String adr = sc.nextLine().trim();
        System.out.print("ID novog grada: ");
        String idG = sc.nextLine().trim();
        System.out.println(HttpKlijent.put("/korisnik/" + idK + "/adresa", adr + ";" + idG));
    }

    static void dohvatiGradove() {
        String odg = HttpKlijent.get("/grad");
        ispisiListu(odg);
    }

    static void dohvatiKorisnike() {
        String odg = HttpKlijent.get("/korisnik");
        ispisiListu(odg);
    }

    static void kreirajKategoriju() {
        if (!Sesija.isProdavac()) {
            System.out.println("Nemate dozvolu za ovu akciju!");
            return;
        }
        System.out.print("Naziv kategorije: ");
        String naziv = sc.nextLine().trim();
        System.out.print("ID nadkategorije (Enter za prazno): ");
        String nadkat = sc.nextLine().trim();
        System.out.println(HttpKlijent.post("/kategorija", naziv + ";" + nadkat));
    }

    static void kreirajArtikal() {
        if (!Sesija.isProdavac()) {
            System.out.println("Nemate dozvolu za ovu akciju!");
            return;
        }
        System.out.print("Naziv: ");
        String naziv = sc.nextLine().trim();
        System.out.print("Opis: ");
        String opis = sc.nextLine().trim();
        System.out.print("Cena: ");
        String cena = sc.nextLine().trim();
        System.out.print("Popust (%): ");
        String pop = sc.nextLine().trim();
        System.out.print("ID kategorije: ");
        String idKat = sc.nextLine().trim();
        String body = naziv+";"+opis+";"+cena+";"+pop+";"+idKat+";"+Sesija.getIdKor();
        System.out.println(HttpKlijent.post("/artikal", body));
    }

    static void menjajCenu() {
        System.out.print("ID artikla: ");
        String idA = sc.nextLine().trim();
        System.out.print("Nova cena: ");
        String cena = sc.nextLine().trim();
        System.out.println(HttpKlijent.put("/artikal/" + idA + "/cena", cena + ";" + Sesija.getIdKor()));
    }

    static void postaviPopust() {
        System.out.print("ID artikla: ");
        String idA = sc.nextLine().trim();
        System.out.print("Popust (%): ");
        String pop = sc.nextLine().trim();
        System.out.println(HttpKlijent.put("/artikal/" + idA + "/popust", pop + ";" + Sesija.getIdKor()));
    }

    static void dohvatiKategorije() {
        ispisiListu(HttpKlijent.get("/kategorija"));
    }

    static void dohvatiMojeArtikle() {
        ispisiListu(HttpKlijent.get("/artikal/" + Sesija.getIdKor()));
    }

    static void dodajUKorpu() {
        System.out.print("ID artikla: ");
        String idA = sc.nextLine().trim();
        System.out.print("Količina: ");
        String kol = sc.nextLine().trim();
        System.out.println(HttpKlijent.post("/korpa", Sesija.getIdKor() + ";" + idA + ";" + kol));
    }

    static void obrisiIzKorpe() {
    System.out.print("ID artikla: ");
    String idA = sc.nextLine().trim();
    System.out.print("Količina: ");
    String kol = sc.nextLine().trim();
    System.out.println(HttpKlijent.put("/korpa/obrisi", Sesija.getIdKor() + ";" + idA + ";" + kol));
}

    static void dohvatiKorpu() {
        ispisiListu(HttpKlijent.get("/korpa/" + Sesija.getIdKor()));
    }

    static void dodajUWishlist() {
        System.out.print("ID artikla: ");
        String idA = sc.nextLine().trim();
        System.out.println(HttpKlijent.post("/wishlist", Sesija.getIdKor() + ";" + idA));
    }

    static void obrisiIzWishlist() {
    System.out.print("ID artikla: ");
    String idA = sc.nextLine().trim();
    System.out.println(HttpKlijent.put("/wishlist/obrisi", Sesija.getIdKor() + ";" + idA));
}

    static void dohvatiWishlist() {
        ispisiListu(HttpKlijent.get("/wishlist/" + Sesija.getIdKor()));
    }

    static void placanje() {
        if (!Sesija.isKupac()) {
            System.out.println("Ovu akciju mogu samo kupci!");
            return;
        }

        String korpa = HttpKlijent.get("/korpa/" + Sesija.getIdKor());
        System.out.println("Trenutna korpa:\n");
        ispisiListu(korpa);

        if (korpa.startsWith("GRESKA") || korpa.contains("prazna")) {
            System.out.println("Korpa je prazna, ne možete platiti!");
            return;
        }

        // stavkee iz korpice
        StringBuilder stavke = new StringBuilder();
        String[] delovi = korpa.substring(3).split("\\|");
        for (String deo : delovi) {
            if (deo.startsWith("ukupno:") || deo.trim().isEmpty()) continue;
            String[] d = deo.split(":");
            String idArt = d[0];
            String[] kolCena = d[2].split("x");
            String kolicina = kolCena[0];
            String cenaPuna = kolCena[1]; // cena bez popusta
            String idPro = d[3];

            if (stavke.length() > 0) stavke.append(",");
            stavke.append(idArt).append(":").append(kolicina).append(":").append(cenaPuna).append(":").append(idPro);
        }

    String body = Sesija.getIdKor() + ";" + Sesija.getAdresa() + ";" + Sesija.getGrad() + ";" + stavke;
    System.out.println(HttpKlijent.post("/placanje", body));
}

    static void mojeNarudzbine() {
        ispisiListu(HttpKlijent.get("/narudzbina/" + Sesija.getIdKor()));
    }

    static void sveNarudzbine() {
        ispisiListu(HttpKlijent.get("/narudzbina"));
    }

    static void sveTransakcije() {
        ispisiListu(HttpKlijent.get("/transakcija"));
    }

    static void ispisiListu(String odgovor) { // pom fja za ispis samo
        if (odgovor == null || odgovor.startsWith("GRESKA")) {
            System.out.println(odgovor);
            return;
        }
        String sadrzaj = odgovor.startsWith("OK;") ? odgovor.substring(3) : odgovor;
        String[] stavke = sadrzaj.split("\\|");
        for (String s : stavke) {
            if (!s.trim().isEmpty()) System.out.println("  > " + s);
        }
    }
}