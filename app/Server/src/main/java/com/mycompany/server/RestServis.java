package com.mycompany.server;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@Path("/")
public class RestServis {

    private String posaljiJMS(String queueName, String poruka) { // salje poruke i ceka odg
        try {
            InitialContext ictx = new InitialContext();
            ConnectionFactory cf = (ConnectionFactory) ictx.lookup("jms/__defaultConnectionFactory");
            Queue queue = (Queue) ictx.lookup(queueName);

            try (JMSContext context = cf.createContext()) {
                TemporaryQueue replyQueue = context.createTemporaryQueue(); // privremeni queue za odgovor
                TextMessage msg = context.createTextMessage(poruka);
                msg.setJMSReplyTo(replyQueue);
                context.createProducer().send(queue, msg);

                JMSConsumer consumer = context.createConsumer(replyQueue);
                String odgovor = consumer.receiveBody(String.class, 20000); //do 20sek cekaaa
                return odgovor != null ? odgovor : "GRESKA - Nema odgovora od podsistema";
            }
        } catch (Exception e) {
            return "GRESKA - " + e.getMessage();
        }
    }

    private Response napravi(String odgovor) {
        if (odgovor != null && odgovor.startsWith("OK")) {
            return Response.ok(odgovor).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(odgovor).build();
    }

    // PODSISTEM 1

    // Zahtev 1 - Provera korisnika (login)
    // POST /api/login  body: korisnickoIme;sifra
    @POST
    @Path("/login")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(String body) {
        String odgovor = posaljiJMS("jms/Podsistem1Queue", "PROVERA_KORISNIKA;" + body);
        return napravi(odgovor);
    }

    // Zahtev 2 - Kreiranje grada (samo admin)
    // POST /api/grad  body: naziv
    @POST
    @Path("/grad")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response kreirajGrad(String naziv) {
        String odgovor = posaljiJMS("jms/Podsistem1Queue", "KREIRAJ_GRAD;" + naziv);
        return napravi(odgovor);
    }

    // Zahtev 3 - Kreiranje korisnika (samo admin)
    // POST /api/korisnik  body: korisnickoIme;sifra;ime;prezime;adresa;idGrada
    @POST
    @Path("/korisnik")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response kreirajKorisnika(String body) {
        String odgovor = posaljiJMS("jms/Podsistem1Queue", "KREIRAJ_KORISNIKA;" + body);
        return napravi(odgovor);
    }

    // Zahtev 4 - Dodavanje novca (samo admin)
    // PUT /api/korisnik/{idKor}/novac  body: iznos
    @PUT
    @Path("/korisnik/{idKor}/novac")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response dodajNovac(@PathParam("idKor") int idKor, String iznos) {
        String odgovor = posaljiJMS("jms/Podsistem1Queue", "DODAJ_NOVAC;" + idKor + ";" + iznos);
        return napravi(odgovor);
    }

    // Zahtev 5 - Promena adrese (samo admin)
    // PUT /api/korisnik/{idKor}/adresa  body: novaAdresa;idGrada
    @PUT
    @Path("/korisnik/{idKor}/adresa")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response promeniAdresu(@PathParam("idKor") int idKor, String body) {
        String odgovor = posaljiJMS("jms/Podsistem1Queue", "PROMENI_ADRESU;" + idKor + ";" + body);
        return napravi(odgovor);
    }

    // Zahtev 15 - Dohvati sve gradove
    // GET /api/grad
    @GET
    @Path("/grad")
    @Produces(MediaType.TEXT_PLAIN)
    public Response dohvatiGradove() {
        String odgovor = posaljiJMS("jms/Podsistem1Queue", "DOHVATI_GRADOVE");
        return napravi(odgovor);
    }

    // Zahtev 16 - Dohvati sve korisnike
    // GET /api/korisnik
    @GET
    @Path("/korisnik")
    @Produces(MediaType.TEXT_PLAIN)
    public Response dohvatiKorisnike() {
        String odgovor = posaljiJMS("jms/Podsistem1Queue", "DOHVATI_KORISNIKE");
        return napravi(odgovor);
    }

    // PODSISTEM 2

    // Zahtev 6 - Kreiranje kategorije
    // POST /api/kategorija  body: naziv;idNadkat (idNadkat moze biti prazan)
    @POST
    @Path("/kategorija")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response kreirajKategoriju(String body) {
        String odgovor = posaljiJMS("jms/Podsistem2Queue", "KREIRAJ_KATEGORIJU;" + body);
        return napravi(odgovor);
    }

    // Zahtev 7 - Kreiranje artikla
    // POST /api/artikal  body: naziv;opis;cena;procenatPopusta;idKat;idPro
    @POST
    @Path("/artikal")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response kreirajArtikal(String body) {
        String odgovor = posaljiJMS("jms/Podsistem2Queue", "KREIRAJ_ARTIKAL;" + body);
        return napravi(odgovor);
    }

    // Zahtev 8 - Menjanje cene artikla
    // PUT /api/artikal/{idArt}/cena  body: novaCena;idKor
    @PUT
    @Path("/artikal/{idArt}/cena")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response menjajCenu(@PathParam("idArt") int idArt, String body) {
        String odgovor = posaljiJMS("jms/Podsistem2Queue", "MENJAJ_CENU;" + idArt + ";" + body);
        return napravi(odgovor);
    }

    // Zahtev 9 - Postavljanje popusta
    // PUT /api/artikal/{idArt}/popust  body: popust;idKor
    @PUT
    @Path("/artikal/{idArt}/popust")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response postaviPopust(@PathParam("idArt") int idArt, String body) {
        String odgovor = posaljiJMS("jms/Podsistem2Queue", "POSTAVI_POPUST;" + idArt + ";" + body);
        return napravi(odgovor);
    }

    // Zahtev 10 - Dodavanje u korpu
    // POST /api/korpa  body: idKor;idArt;kolicina
    @POST
    @Path("/korpa")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response dodajUKorpu(String body) {
        String odgovor = posaljiJMS("jms/Podsistem2Queue", "DODAJ_U_KORPU;" + body);
        return napravi(odgovor);
    }

    // Zahtev 11 - Brisanje iz korpe
    // PUT /api/korpa/obrisi  body: idKor;idArt;kolicina
    @PUT
    @Path("/korpa/obrisi")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response obrisiIzKorpe(String body) {
        String odgovor = posaljiJMS("jms/Podsistem2Queue", "OBRISI_IZ_KORPE;" + body);
        return napravi(odgovor);
    }


    // Zahtev 12 - Dodavanje u wishlist
    // POST /api/wishlist  body: idKor;idArt
    @POST
    @Path("/wishlist")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response dodajUWishlist(String body) {
        String odgovor = posaljiJMS("jms/Podsistem2Queue", "DODAJ_U_WISHLIST;" + body);
        return napravi(odgovor);
    }

    // Zahtev 13 - Brisanje iz wishlist
    // PUT /api/wishlist/obrisi  body: idKor;idArt
    @PUT
    @Path("/wishlist/obrisi")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response obrisiIzWishlist(String body) {
        String odgovor = posaljiJMS("jms/Podsistem2Queue", "OBRISI_IZ_WISHLIST;" + body);
        return napravi(odgovor);
    }

    // Zahtev 17 - Dohvati sve kategorije
    // GET /api/kategorija
    @GET
    @Path("/kategorija")
    @Produces(MediaType.TEXT_PLAIN)
    public Response dohvatiKategorije() {
        String odgovor = posaljiJMS("jms/Podsistem2Queue", "DOHVATI_KATEGORIJE");
        return napravi(odgovor);
    }

    // Zahtev 18 - Dohvati artikle korisnika
    // GET /api/artikal/{idKor}
    @GET
    @Path("/artikal/{idKor}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response dohvatiArtikle(@PathParam("idKor") int idKor) {
        String odgovor = posaljiJMS("jms/Podsistem2Queue", "DOHVATI_ARTIKLE_KORISNIKA;" + idKor);
        return napravi(odgovor);
    }

    // Zahtev 19 - Dohvati korpu
    // GET /api/korpa/{idKor}
    @GET
    @Path("/korpa/{idKor}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response dohvatiKorpu(@PathParam("idKor") int idKor) {
        String odgovor = posaljiJMS("jms/Podsistem2Queue", "DOHVATI_KORPU;" + idKor);
        return napravi(odgovor);
    }

    // Zahtev 20 - Dohvati wishlist
    // GET /api/wishlist/{idKor}
    @GET
    @Path("/wishlist/{idKor}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response dohvatiWishlist(@PathParam("idKor") int idKor) {
        String odgovor = posaljiJMS("jms/Podsistem2Queue", "DOHVATI_WISHLIST;" + idKor);
        return napravi(odgovor);
    }

    // PODSISTEM 3

    // Zahtev 14 - Placanje
    // POST /api/placanje  body: idKor;adresaDostave;gradDostave;stavke
    @POST
    @Path("/placanje")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response placanje(String body) {
        // body: idKor;adresaDostave;gradDostave;stavke
        String[] delovi = body.split(";", 4);
        int idKor = Integer.parseInt(delovi[0]);
        String adresa = delovi[1];
        String grad = delovi[2];
        String stavke = delovi[3];

        // izracunaj ukupnu cenu iz stavki
        BigDecimal ukupno = BigDecimal.ZERO;
        for (String stavkaStr : stavke.split(",")) {
            String[] d = stavkaStr.split(":");
            int kolicina = Integer.parseInt(d[1]);
            BigDecimal cena = new BigDecimal(d[2]);
            ukupno = ukupno.add(cena.multiply(new BigDecimal(kolicina)));
        }

        // proveri stanje novca u podsistemu1
        String odgStanje = posaljiJMS("jms/Podsistem1Queue", "DOHVATI_STANJE;" + idKor);
        if (odgStanje.startsWith("GRESKA"))  return napravi("GRESKA;Ne mogu proveriti stanje: " + odgStanje);
        BigDecimal stanje = new BigDecimal(odgStanje.split(";")[1]);
        if (stanje.compareTo(ukupno) < 0) return napravi("GRESKA;Nemate dovoljno novca! Stanje: " + stanje + ", potrebno: " + ukupno);

        // kreira narudzbinu u podsistemu3
        String odgNar = posaljiJMS("jms/Podsistem3Queue", "KREIRAJ_NARUDZBINU;" + idKor + ";" + adresa + ";" + grad + ";" + stavke);
        if (odgNar.startsWith("GRESKA")) return napravi(odgNar);

        // skida novac u podsistemu1
        String odgSkini = posaljiJMS("jms/Podsistem1Queue", "SKINI_NOVAC;" + idKor + ";" + ukupno);
        if (odgSkini.startsWith("GRESKA")) System.out.println("UPOZORENJE: Novac nije skinut! " + odgSkini);

        // brise korpu u podsistemu2
        String odgKorpa = posaljiJMS("jms/Podsistem2Queue", "OBRISI_KORPU;" + idKor);
        if (odgKorpa.startsWith("GRESKA")) System.out.println("UPOZORENJE: Korpa nije obrisana! " + odgKorpa);
        return napravi(odgNar);
    }
    
    // Zahtev 21 - Dohvati narudzbine korisnika
    // GET /api/narudzbina/{idKor}
    @GET
    @Path("/narudzbina/{idKor}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response dohvatiNarudzbeKorisnika(@PathParam("idKor") int idKor) {
        String odgovor = posaljiJMS("jms/Podsistem3Queue", "DOHVATI_NARUDZBINE_KORISNIKA;" + idKor);
        return napravi(odgovor);
    }

    // Zahtev 22 - Dohvati sve narudzbine
    // GET /api/narudzbina
    @GET
    @Path("/narudzbina")
    @Produces(MediaType.TEXT_PLAIN)
    public Response dohvatiSveNarudzbine() {
        String odgovor = posaljiJMS("jms/Podsistem3Queue", "DOHVATI_SVE_NARUDZBINE");
        return napravi(odgovor);
    }

    // Zahtev 23 - Dohvati sve transakcije
    // GET /api/transakcija
    @GET
    @Path("/transakcija")
    @Produces(MediaType.TEXT_PLAIN)
    public Response dohvatiTransakcije() {
        String odgovor = posaljiJMS("jms/Podsistem3Queue", "DOHVATI_TRANSAKCIJE");
        return napravi(odgovor);
    }
}
