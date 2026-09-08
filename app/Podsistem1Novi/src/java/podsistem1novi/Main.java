package podsistem1novi;

import entiteti.*;
import java.util.List;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;

public class Main {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem1NoviPU");
    private static EntityManager em = emf.createEntityManager();

    public static void main(String[] args) {
        try {
            InitialContext ictx = new InitialContext();
            ConnectionFactory cf = (ConnectionFactory) ictx.lookup("jms/__defaultConnectionFactory");
            Queue queue = (Queue) ictx.lookup("jms/Podsistem1Queue");

            try (JMSContext context = cf.createContext()) {
                JMSConsumer consumer = context.createConsumer(queue);
                System.out.println("Podsistem 1 pokrenut i čeka poruke...");

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
                case "PROVERA_KORISNIKA": return proveraKorisnika(p);
                case "KREIRAJ_GRAD": return kreirajGrad(p);
                case "KREIRAJ_KORISNIKA": return kreirajKorisnika(p);
                case "DODAJ_NOVAC": return dodajNovac(p);
                case "PROMENI_ADRESU": return promeniAdresu(p);
                case "DOHVATI_GRADOVE": return dohvatiGradove();
                case "DOHVATI_KORISNIKE": return dohvatiKorisnike();
                case "DOHVATI_STANJE": return dohvatiStanje(p);
                case "SKINI_NOVAC":    return skiniNovac(p);
                default: return "GRESKA Nepoznata komanda: " + komanda;
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "GRESKA;" + e.getMessage();
        }
    }

    private static String proveraKorisnika(String[] p) {
    List<Korisnik> rezultat = em.createQuery("SELECT k FROM Korisnik k WHERE k.korisnickoIme = :ki AND k.sifra = :s", Korisnik.class)
                                            .setParameter("ki", p[1]).setParameter("s", p[2]).getResultList();
    if (!rezultat.isEmpty()) {
        Korisnik k = rezultat.get(0);

        String uloge = "Kupac"; // default
        if (k.getUlogaList() != null && !k.getUlogaList().isEmpty()) {
            uloge = k.getUlogaList().stream().map(Uloga::getNaziv).collect(java.util.stream.Collectors.joining(",")); //"Prodavac,Kupac"
        }

        return "OK;" + k.getIdKor() + ";" + k.getKorisnickoIme() + ";" + k.getIme() + ";" + k.getPrezime() + ";"
                     + k.getStanjeNovca() + ";" + uloge + ";"+ k.getAdresa() + ";" + k.getIdGrad().getNaziv();
    }
    return "GRESKA;Korisnik nije pronađen";
}

    // 2 - kreiranje grada: KREIRAJ_GRAD;naziv
    private static String kreirajGrad(String[] p) {
        em.getTransaction().begin();
        Grad g = new Grad();
        g.setNaziv(p[1]);
        em.persist(g);
        em.getTransaction().commit();
        return "OK;Grad kreiran: " + g.getNaziv();
    }

    // 3 - kreiranje korisnika: KREIRAJ_KORISNIKA;korisnickoIme;sifra;ime;prezime;adresa;idGrada
    private static String kreirajKorisnika(String[] p) {
        Grad grad = em.find(Grad.class, Integer.parseInt(p[6]));
        if (grad == null) return "GRESKA;Grad nije pronađen";

        em.getTransaction().begin();
        Korisnik k = new Korisnik();
        k.setKorisnickoIme(p[1]);
        k.setSifra(p[2]);
        k.setIme(p[3]);
        k.setPrezime(p[4]);
        k.setAdresa(p[5]);
        k.setIdGrad(grad);
        k.setStanjeNovca(0);
        em.persist(k);
        em.getTransaction().commit();
        return "OK;Korisnik kreiran: " + k.getKorisnickoIme();
    }

    // 4 - dodavanje novca korisniku: DODAJ_NOVAC;idKor;iznos
    private static String dodajNovac(String[] p) {
        Korisnik k = em.find(Korisnik.class, Integer.parseInt(p[1]));
        if (k == null) return "GRESKA;Korisnik nije pronađen";

        em.getTransaction().begin();
        k.setStanjeNovca(k.getStanjeNovca() + Integer.parseInt(p[2]));
        em.merge(k);
        em.getTransaction().commit();
        return "OK;Novo stanje: " + k.getStanjeNovca();
    }

    // 5 – promena adrese i grada korisnika: PROMENI_ADRESU;idKor;novaAdresa;idGrada
    private static String promeniAdresu(String[] p) {
        Korisnik k = em.find(Korisnik.class, Integer.parseInt(p[1]));
        Grad g = em.find(Grad.class, Integer.parseInt(p[3]));
        if (k == null) return "GRESKA;Korisnik nije pronađen";
        if (g == null) return "GRESKA;Grad nije pronađen";

        em.getTransaction().begin();
        k.setAdresa(p[2]);
        k.setIdGrad(g);
        em.merge(k);
        em.getTransaction().commit();
        return "OK;Adresa izmenjena";
    }

    // 15 – dohvatanje svih gradova: DOHVATI_GRADOVE
    private static String dohvatiGradove() {
        List<Grad> gradovi = em.createNamedQuery("Grad.findAll", Grad.class).getResultList();
        if (gradovi.isEmpty()) return "OK;(nema gradova)";

        StringBuilder sb = new StringBuilder("OK;");
        for (Grad g : gradovi) {
            sb.append(g.getIdGrad()).append(":").append(g.getNaziv()).append("|");
        }
        return sb.toString();
    }

    // 16 – dohvatanje svih korisnika: DOHVATI_KORISNIKE
    private static String dohvatiKorisnike() {
        List<Korisnik> korisnici = em.createNamedQuery("Korisnik.findAll", Korisnik.class).getResultList();
        if (korisnici.isEmpty()) return "OK;(nema korisnika)";

        StringBuilder sb = new StringBuilder("OK;");
        for (Korisnik k : korisnici) {
            sb.append(k.getIdKor()).append(":")
              .append(k.getKorisnickoIme()).append(":")
              .append(k.getIme()).append(":")
              .append(k.getPrezime()).append(":")
              .append(k.getAdresa()).append(":")
              .append(k.getIdGrad().getNaziv()).append("|");
        }
        return sb.toString();
    }
    
    private static String dohvatiStanje(String[] p) {
        Korisnik k = em.find(Korisnik.class, Integer.parseInt(p[1]));
        if (k == null) return "GRESKA;Korisnik nije pronađen";
        return "OK;" + k.getStanjeNovca();
    }

    private static String skiniNovac(String[] p) {
        Korisnik k = em.find(Korisnik.class, Integer.parseInt(p[1]));
        if (k == null) return "GRESKA;Korisnik nije pronađen";

        BigDecimal iznos = new BigDecimal(p[2]);
        BigDecimal novoStanje = new BigDecimal(k.getStanjeNovca()).subtract(iznos);
        if (novoStanje.compareTo(BigDecimal.ZERO) < 0)
            return "GRESKA;Nedovoljno sredstava";

        em.getTransaction().begin();
        k.setStanjeNovca(novoStanje.intValue());
        em.merge(k);
        em.getTransaction().commit();
        return "OK;Novo stanje: " + k.getStanjeNovca();
    }
}