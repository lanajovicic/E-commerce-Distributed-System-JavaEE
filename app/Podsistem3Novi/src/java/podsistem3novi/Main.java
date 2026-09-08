package podsistem3novi;

import entiteti.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem3NoviPU");
    private static EntityManager em = emf.createEntityManager();

    public static void main(String[] args) {
        try {
            InitialContext ictx = new InitialContext();
            ConnectionFactory cf = (ConnectionFactory) ictx.lookup("jms/__defaultConnectionFactory");
            Queue queue = (Queue) ictx.lookup("jms/Podsistem3Queue");

            try (JMSContext context = cf.createContext()) {
                JMSConsumer consumer = context.createConsumer(queue);
                System.out.println("Podsistem 3 pokrenut i čeka poruke...");

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
                case "PLACANJE": return placanje(p);
                case "DOHVATI_NARUDZBINE_KORISNIKA": return dohvatiNarudzbineKorisnika(p);
                case "DOHVATI_SVE_NARUDZBINE": return dohvatiSveNarudzbine();
                case "DOHVATI_TRANSAKCIJE": return dohvatiTransakcije();
                case "KREIRAJ_NARUDZBINU": return kreirajNarudzbinu(p);
                default: return "GRESKA - Nepoznata komanda: " + komanda;
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "GRESKA - " + e.getMessage();
        }
    }

    // 14 – PLACANJE;idKupca;adresaDostave;gradDostave;stavke
    // stavke formataa: idArt:kolicina:jedinicnaCena:idProdavca,...
    private static String placanje(String[] p) {
        int idKupca = Integer.parseInt(p[1]);
        String adresaDostave = p[2];
        String gradDostave = p[3];
        String[] stavkeStr = p[4].split(",");

        em.getTransaction().begin();

        Narudzbina nar = new Narudzbina();
        nar.setIdKupca(idKupca);
        nar.setAdresaDostave(adresaDostave);
        nar.setGradDostave(gradDostave);
        nar.setVremeNarudzbine(new Date());
        nar.setUkupnaCena(BigDecimal.ZERO);
        em.persist(nar);
        em.flush();

        BigDecimal ukupno = BigDecimal.ZERO;

        for (String stavkaStr : stavkeStr) {
            String[] d = stavkaStr.split(":");
            int idArt = Integer.parseInt(d[0]);
            int kolicina = Integer.parseInt(d[1]);
            BigDecimal jedinicnaCena = new BigDecimal(d[2]);
            int idProdavca = Integer.parseInt(d[3]);

            StavkaNarudzbine stavka = new StavkaNarudzbine();
            stavka.setIdNar(nar);
            stavka.setIdArt(idArt);
            stavka.setKolicina(kolicina);
            stavka.setJedinicnaCena(jedinicnaCena);
            stavka.setIdProdavca(idProdavca);
            em.persist(stavka);

            ukupno = ukupno.add(jedinicnaCena.multiply(new BigDecimal(kolicina)));
        }

        nar.setUkupnaCena(ukupno);
        em.merge(nar);

        Transakcija trans = new Transakcija();
        trans.setSuma(ukupno);
        trans.setIdNar(nar);
        trans.setVremePlacanja(new Date());
        em.persist(trans);

        em.getTransaction().commit();
        return "OK;Placanje uspesno|narId:" + nar.getIdNar() + "|transId:" + trans.getIdTrans()  + "|ukupno:" + ukupno;
    }

    // 21 – DOHVATI_NARUDZBINE_KORISNIKA;idKor
    private static String dohvatiNarudzbineKorisnika(String[] p) {
        int idKor = Integer.parseInt(p[1]);
        List<Narudzbina> narudzbine = em.createQuery("SELECT n FROM Narudzbina n WHERE n.idKupca = :id", Narudzbina.class).setParameter("id", idKor).getResultList();

        if (narudzbine.isEmpty()) return "OK;(nema narudžbina)";
        StringBuilder sb = new StringBuilder("OK;");
        for (Narudzbina n : narudzbine) {
            sb.append(n.getIdNar()).append(":").append(n.getVremeNarudzbine()).append(":").append(n.getUkupnaCena()).append(":")
                                   .append(n.getAdresaDostave()).append(":").append(n.getGradDostave()).append("|");
        }
        return sb.toString();
    }

    //  22 – DOHVATI_SVE_NARUDZBINE
    private static String dohvatiSveNarudzbine() {
        List<Narudzbina> narudzbine = em.createNamedQuery("Narudzbina.findAll", Narudzbina.class).getResultList();

        if (narudzbine.isEmpty()) return "OK;(nema narudžbina)";
        StringBuilder sb = new StringBuilder("OK;");
        for (Narudzbina n : narudzbine) {
            sb.append(n.getIdNar()).append(":").append(n.getIdKupca()).append(":").append(n.getVremeNarudzbine()).append(":")
                .append(n.getUkupnaCena()).append(":").append(n.getAdresaDostave()).append("|");
        }
        return sb.toString();
    }

    // 23 – DOHVATI_TRANSAKCIJE
    private static String dohvatiTransakcije() {
        List<Transakcija> transakcije = em.createNamedQuery("Transakcija.findAll", Transakcija.class).getResultList();

        if (transakcije.isEmpty()) return "OK;(nema transakcija)";
        StringBuilder sb = new StringBuilder("OK;");
        for (Transakcija t : transakcije) {
            sb.append(t.getIdTrans()).append(":").append(t.getSuma()).append(":")
              .append(t.getVremePlacanja()).append(":").append(t.getIdNar().getIdNar()).append("|");
        }
        return sb.toString();
    }
    // KREIRAJ_NARUDZBINU;idKupca;adresaDostave;gradDostave;stavke
    private static String kreirajNarudzbinu(String[] p) {
        int idKupca = Integer.parseInt(p[1]);
        String adresaDostave = p[2];
        String gradDostave = p[3];
        String[] stavkeStr = p[4].split(",");

        BigDecimal ukupno = BigDecimal.ZERO;
        for (String s : stavkeStr) {
            String[] d = s.split(":");
            ukupno = ukupno.add(new BigDecimal(d[2]).multiply(new BigDecimal(d[1])));
        }

        em.getTransaction().begin();

        Narudzbina nar = new Narudzbina();
        nar.setIdKupca(idKupca);
        nar.setAdresaDostave(adresaDostave);
        nar.setGradDostave(gradDostave);
        nar.setVremeNarudzbine(new Date());
        nar.setUkupnaCena(ukupno);
        em.persist(nar);
        em.flush();

        for (String stavkaStr : stavkeStr) {
            String[] d = stavkaStr.split(":");
            StavkaNarudzbine stavka = new StavkaNarudzbine();
            stavka.setIdNar(nar);
            stavka.setIdArt(Integer.parseInt(d[0]));
            stavka.setKolicina(Integer.parseInt(d[1]));
            stavka.setJedinicnaCena(new BigDecimal(d[2]));
            stavka.setIdProdavca(Integer.parseInt(d[3]));
            em.persist(stavka);
        }

        Transakcija trans = new Transakcija();
        trans.setSuma(ukupno);
        trans.setIdNar(nar);
        trans.setVremePlacanja(new Date());
        em.persist(trans);

        em.getTransaction().commit();
        return "OK;Placanje uspesno|narId:" + nar.getIdNar() + "|transId:" + trans.getIdTrans() + "|ukupno:" + ukupno;
    }
}