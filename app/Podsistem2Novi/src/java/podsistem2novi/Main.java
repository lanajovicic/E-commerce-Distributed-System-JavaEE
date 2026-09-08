package podsistem2novi;

import entiteti.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem2NoviPU");
    private static EntityManager em = emf.createEntityManager();

    public static void main(String[] args) {
        try {
            InitialContext ictx = new InitialContext();
            ConnectionFactory cf = (ConnectionFactory) ictx.lookup("jms/__defaultConnectionFactory");
            Queue queue = (Queue) ictx.lookup("jms/Podsistem2Queue");

            try (JMSContext context = cf.createContext()) {
                JMSConsumer consumer = context.createConsumer(queue);
                System.out.println("Podsistem 2 pokrenut i čeka poruke...");

                while (true) {
                    Message msg = consumer.receive();
                    if (msg instanceof TextMessage) {
                        String text = ((TextMessage) msg).getText();
                        System.out.println("Primljen zahtev: " + text);

                        String[] delovi = text.split(";", -1);
                        String komanda = delovi[0];
                        String odgovor = obradiKomandu(komanda, delovi);
                        System.out.println("Odgovor: " + odgovor);

                        if (msg.getJMSReplyTo() != null) {
                            context.createProducer().send(msg.getJMSReplyTo(), odgovor);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String obradiKomandu(String komanda, String[] p) {
        try {
            switch (komanda) {
                case "KREIRAJ_KATEGORIJU": return kreirajKategoriju(p);
                case "KREIRAJ_ARTIKAL": return kreirajArtikal(p);
                case "MENJAJ_CENU": return menjajCenu(p);
                case "POSTAVI_POPUST": return postaviPopust(p);
                case "DODAJ_U_KORPU": return dodajUKorpu(p);
                case "OBRISI_IZ_KORPE": return obrisiIzKorpe(p);
                case "DODAJ_U_WISHLIST": return dodajUWishlist(p);
                case "OBRISI_IZ_WISHLIST": return obrisiIzWishlist(p);
                case "DOHVATI_KATEGORIJE": return dohvatiKategorije();
                case "DOHVATI_ARTIKLE_KORISNIKA": return dohvatiArtikleKorisnika(p);
                case "DOHVATI_KORPU": return dohvatiKorpu(p);
                case "DOHVATI_WISHLIST": return dohvatiWishlist(p);
                case "OBRISI_KORPU": return obrisiKorpu(p);
                default: return "GRESKA;Nepoznata komanda: " + komanda;
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "GRESKA;" + e.getMessage();
        }
    }

    // 6 – KREIRAJ_KATEGORIJU;naziv;idNadkat
    private static String kreirajKategoriju(String[] p) {
        em.getTransaction().begin();
        Kategorija k = new Kategorija();
        k.setNaziv(p[1]);
        if (!p[2].isEmpty()) {
            k.setIdNadkat(Integer.parseInt(p[2]));
        }
        em.persist(k);
        em.getTransaction().commit();
        return "OK;Kategorija kreirana: " + k.getNaziv();
    }

    // 7 – KREIRAJ_ARTIKAL;naziv;opis;cena;procenatPopusta;idKat;idPro
    private static String kreirajArtikal(String[] p) {
        Kategorija kat = em.find(Kategorija.class, Integer.parseInt(p[5]));
        if (kat == null) return "GRESKA;Kategorija nije pronađena";

        em.getTransaction().begin();
        Artikal a = new Artikal();
        a.setNaziv(p[1]);
        a.setOpis(p[2]);
        a.setCena(new BigDecimal(p[3]));
        a.setProcenatPopusta(new BigDecimal(p[4]));
        a.setIdKat(kat);
        a.setIdPro(Integer.parseInt(p[6]));
        em.persist(a);
        em.getTransaction().commit();
        return "OK;Artikal kreiran: " + a.getNaziv();
    }

    // 8 – MENJAJ_CENU;idArt;novaCena;idKor
    private static String menjajCenu(String[] p) {
        Artikal a = em.find(Artikal.class, Integer.parseInt(p[1]));
        if (a == null) return "GRESKA;Artikal nije pronađen";
        if (a.getIdPro() != Integer.parseInt(p[3])) return "GRESKA;Nemate pravo da menjate cenu ovog artikla";

        em.getTransaction().begin();
        a.setCena(new BigDecimal(p[2]));
        em.merge(a);
        em.getTransaction().commit();
        return "OK;Cena izmenjena na: " + a.getCena();
    }

    // 9 – POSTAVI_POPUST;idArt;popust;idKor
    private static String postaviPopust(String[] p) {
        Artikal a = em.find(Artikal.class, Integer.parseInt(p[1]));
        if (a == null) return "GRESKA;Artikal nije pronađen";
        if (a.getIdPro() != Integer.parseInt(p[3])) return "GRESKA;Nemate pravo da postavljate popust na ovaj artikal";

        em.getTransaction().begin();
        a.setProcenatPopusta(new BigDecimal(p[2]));
        em.merge(a);
        em.getTransaction().commit();
        return "OK;Popust postavljen: " + a.getProcenatPopusta() + "%";
    }

    // 10 – DODAJ_U_KORPU;idKor;idArt;kolicina
    private static String dodajUKorpu(String[] p) {
        int idKor = Integer.parseInt(p[1]);
        int idArt = Integer.parseInt(p[2]);
        int kolicina = Integer.parseInt(p[3]);

        Artikal art = em.find(Artikal.class, idArt);
        if (art == null) return "GRESKA;Artikal nije pronađen";

        List<Korpa> korpe = em.createQuery("SELECT k FROM Korpa k WHERE k.idKor = :id", Korpa.class).setParameter("id", idKor).getResultList();

        em.getTransaction().begin();
        Korpa korpa;
        if (korpe.isEmpty()) {
            korpa = new Korpa();
            korpa.setIdKor(idKor);
            korpa.setUkupnaCena(BigDecimal.ZERO);
            em.persist(korpa);
            em.flush();
        } else {
            korpa = korpe.get(0);
        }

        List<StavkaKorpe> postojece = em.createQuery( "SELECT s FROM StavkaKorpe s WHERE s.stavkaKorpePK.idKorpa = :ik AND s.stavkaKorpePK.idArt = :ia",StavkaKorpe.class)
                                                    .setParameter("ik", korpa.getIdKorpa()).setParameter("ia", idArt).getResultList();

        if (!postojece.isEmpty()) {
            StavkaKorpe stavka = postojece.get(0);
            stavka.setKolicina(stavka.getKolicina() + kolicina);
            em.merge(stavka);
        } else {
            StavkaKorpe stavka = new StavkaKorpe(korpa.getIdKorpa(), idArt);
            stavka.setKolicina(kolicina);
            em.persist(stavka);
        }

        BigDecimal cenaSaPopustom = art.getCena();
        if (art.getProcenatPopusta() != null &&
            art.getProcenatPopusta().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal faktor = BigDecimal.ONE.subtract(art.getProcenatPopusta().divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP));
            cenaSaPopustom = art.getCena().multiply(faktor).setScale(2, RoundingMode.HALF_UP);
        }
        korpa.setUkupnaCena(korpa.getUkupnaCena().add(cenaSaPopustom.multiply(new BigDecimal(kolicina))));
        em.merge(korpa);
        em.getTransaction().commit();
        return "OK;Artikal dodat u korpu. Nova ukupna cena: " + korpa.getUkupnaCena();
    }

    // 11 – OBRISI_IZ_KORPE;idKor;idArt;kolicina
    private static String obrisiIzKorpe(String[] p) {
        int idKor = Integer.parseInt(p[1]);
        int idArt = Integer.parseInt(p[2]);
        int kolicina = Integer.parseInt(p[3]);

        Artikal art = em.find(Artikal.class, idArt);
        if (art == null) return "GRESKA;Artikal nije pronađen";

        List<Korpa> korpe = em.createQuery("SELECT k FROM Korpa k WHERE k.idKor = :id", Korpa.class).setParameter("id", idKor).getResultList();
        if (korpe.isEmpty()) return "GRESKA;Korpa nije pronađena";
        Korpa korpa = korpe.get(0);

        List<StavkaKorpe> postojece = em.createQuery("SELECT s FROM StavkaKorpe s WHERE s.stavkaKorpePK.idKorpa = :ik AND s.stavkaKorpePK.idArt = :ia", StavkaKorpe.class)
                                                    .setParameter("ik", korpa.getIdKorpa()).setParameter("ia", idArt).getResultList();
        if (postojece.isEmpty()) return "GRESKA;Artikal nije u korpi";

        em.getTransaction().begin();
        StavkaKorpe stavka = postojece.get(0);

        BigDecimal cenaSaPopustom = art.getCena();
        if (art.getProcenatPopusta() != null &&
            art.getProcenatPopusta().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal faktor = BigDecimal.ONE.subtract(art.getProcenatPopusta().divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP));
            cenaSaPopustom = art.getCena().multiply(faktor).setScale(2, RoundingMode.HALF_UP);
        }

        if (stavka.getKolicina() <= kolicina) {
            korpa.setUkupnaCena(korpa.getUkupnaCena().subtract(cenaSaPopustom.multiply(new BigDecimal(stavka.getKolicina()))));
            em.remove(em.merge(stavka));
        } else {
            korpa.setUkupnaCena(korpa.getUkupnaCena().subtract(cenaSaPopustom.multiply(new BigDecimal(kolicina))));
            stavka.setKolicina(stavka.getKolicina() - kolicina);
            em.merge(stavka);
        }
        em.merge(korpa);
        em.getTransaction().commit();
        return "OK;Artikal uklonjen iz korpe";
    }

    // 12 – DODAJ_U_WISHLIST;idKor;idArt
    private static String dodajUWishlist(String[] p) {
        int idKor = Integer.parseInt(p[1]);
        int idArt = Integer.parseInt(p[2]);

        Artikal art = em.find(Artikal.class, idArt);
        if (art == null) return "GRESKA;Artikal nije pronađen";

        List<ListaZelja> liste = em.createQuery("SELECT l FROM ListaZelja l WHERE l.idKor = :id", ListaZelja.class).setParameter("id", idKor).getResultList();

        em.getTransaction().begin();
        ListaZelja lista;
        if (liste.isEmpty()) {
            lista = new ListaZelja();
            lista.setIdKor(idKor);
            lista.setVremeKreiranjaListe(new Date());
            em.persist(lista);
            em.flush();
        } else {
            lista = liste.get(0);
        }

        StavkaListeZelja stavka = new StavkaListeZelja(lista.getIdListe(), idArt);
        stavka.setVremeDodavanja(new Date());
        em.persist(stavka);
        em.getTransaction().commit();
        return "OK;Artikal dodat u listu želja";
    }

    // 13 – OBRISI_IZ_WISHLIST;idKor;idArt
    private static String obrisiIzWishlist(String[] p) {
        int idKor = Integer.parseInt(p[1]);
        int idArt = Integer.parseInt(p[2]);

        List<ListaZelja> liste = em.createQuery("SELECT l FROM ListaZelja l WHERE l.idKor = :id", ListaZelja.class).setParameter("id", idKor).getResultList();
        if (liste.isEmpty()) return "GRESKA;Lista želja nije pronađena";

        ListaZelja lista = liste.get(0);
        List<StavkaListeZelja> stavke = em.createQuery("SELECT s FROM StavkaListeZelja s WHERE s.stavkaListeZeljaPK.idListe = :il AND s.stavkaListeZeljaPK.idArt = :ia",StavkaListeZelja.class)
                                                    .setParameter("il", lista.getIdListe()).setParameter("ia", idArt).getResultList();
        if (stavke.isEmpty()) return "GRESKA;Artikal nije na listi želja";

        em.getTransaction().begin();
        em.remove(em.merge(stavke.get(0)));
        em.getTransaction().commit();
        return "OK;Artikal uklonjen sa liste želja";
    }

    // 17 – DOHVATI_KATEGORIJE
    private static String dohvatiKategorije() {
        List<Kategorija> kategorije = em.createNamedQuery("Kategorija.findAll", Kategorija.class).getResultList();
        if (kategorije.isEmpty()) return "OK;(nema kategorija)";

        StringBuilder sb = new StringBuilder("OK;");
        for (Kategorija k : kategorije) {
            sb.append(k.getIdKat()).append(":").append(k.getNaziv());
            if (k.getIdNadkat() != null) sb.append("(nadkat:").append(k.getIdNadkat()).append(")");
            sb.append("|");
        }
        return sb.toString();
    }

    // 18 – DOHVATI_ARTIKLE_KORISNIKA;idKor
    private static String dohvatiArtikleKorisnika(String[] p) {
        int idKor = Integer.parseInt(p[1]);
        List<Artikal> artikli = em.createQuery("SELECT a FROM Artikal a WHERE a.idPro = :id", Artikal.class).setParameter("id", idKor).getResultList();

        if (artikli.isEmpty()) return "OK;(nema artikala)";
        StringBuilder sb = new StringBuilder("OK;");
        for (Artikal a : artikli) {
            sb.append(a.getIdArt()).append(":").append(a.getNaziv()).append(":").append(a.getCena()).append(":").append(a.getProcenatPopusta()).append("%|");
        }
        return sb.toString();
    }

    // 19 – DOHVATI_KORPU;idKor
    private static String dohvatiKorpu(String[] p) {
        int idKor = Integer.parseInt(p[1]);
        List<Korpa> korpe = em.createQuery( "SELECT k FROM Korpa k WHERE k.idKor = :id", Korpa.class).setParameter("id", idKor).getResultList();

        if (korpe.isEmpty()) return "OK;Korpa je prazna";
        Korpa korpa = korpe.get(0);

        List<StavkaKorpe> stavke = em.createQuery("SELECT s FROM StavkaKorpe s JOIN FETCH s.artikal WHERE s.stavkaKorpePK.idKorpa = :ik",StavkaKorpe.class)
                                                    .setParameter("ik", korpa.getIdKorpa()).getResultList();

        StringBuilder sb = new StringBuilder("OK;ukupno:" + korpa.getUkupnaCena() + "|");
        for (StavkaKorpe s : stavke) {
            BigDecimal cena = s.getArtikal().getCena();
            if (s.getArtikal().getProcenatPopusta() != null &&
                s.getArtikal().getProcenatPopusta().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal faktor = BigDecimal.ONE.subtract(s.getArtikal().getProcenatPopusta().divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP));
                cena = cena.multiply(faktor).setScale(2, RoundingMode.HALF_UP);
            }
            sb.append(s.getArtikal().getIdArt()).append(":").append(s.getArtikal().getNaziv()).append(":")
              .append(s.getKolicina()).append("x").append(cena).append(":").append(s.getArtikal().getIdPro()).append("|");
        }
        return sb.toString();
    }

    // 20 – DOHVATI_WISHLIST;idKor
    private static String dohvatiWishlist(String[] p) {
        int idKor = Integer.parseInt(p[1]);
        List<ListaZelja> liste = em.createQuery("SELECT l FROM ListaZelja l WHERE l.idKor = :id", ListaZelja.class).setParameter("id", idKor).getResultList();

        if (liste.isEmpty()) return "OK;Lista želja je prazna";
        ListaZelja lista = liste.get(0);

        List<StavkaListeZelja> stavke = em.createQuery("SELECT s FROM StavkaListeZelja s JOIN FETCH s.artikal WHERE s.stavkaListeZeljaPK.idListe = :il", StavkaListeZelja.class)
                                                        .setParameter("il", lista.getIdListe()).getResultList();

        if (stavke.isEmpty()) return "OK;Lista želja je prazna";
        StringBuilder sb = new StringBuilder("OK;");
        for (StavkaListeZelja s : stavke) {
            sb.append(s.getArtikal().getIdArt()).append(":").append(s.getArtikal().getNaziv()).append(":") .append(s.getVremeDodavanja()).append("|");
        }
        return sb.toString();
    }

    private static String obrisiKorpu(String[] p) {
        int idKor = Integer.parseInt(p[1]);
        List<Korpa> korpe = em.createQuery("SELECT k FROM Korpa k WHERE k.idKor = :id", Korpa.class).setParameter("id", idKor).getResultList();

        if (korpe.isEmpty()) return "OK;Korpa je vec prazna";
        Korpa korpa = korpe.get(0);

        em.getTransaction().begin();
        em.createQuery("DELETE FROM StavkaKorpe s WHERE s.stavkaKorpePK.idKorpa = :ik").setParameter("ik", korpa.getIdKorpa()).executeUpdate();
        em.remove(em.merge(korpa));
        em.getTransaction().commit();
        return "OK;Korpa obrisana";
    }
}