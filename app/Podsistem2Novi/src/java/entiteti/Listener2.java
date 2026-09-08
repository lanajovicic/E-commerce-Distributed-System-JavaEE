/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;
//package podsistem2;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Listener2 {

    public static void main(String[] args) {
        // Povezivanje sa bazom (u persistence.xml mora biti "Podsistem2PU")
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem2NoviPU");
        EntityManager em = emf.createEntityManager();

        try {
            InitialContext ictx = new InitialContext();
            ConnectionFactory cf = (ConnectionFactory) ictx.lookup("jms/__defaultConnectionFactory");
            Queue queue = (Queue) ictx.lookup("jms/Podsistem2Queue");

            try (JMSContext context = cf.createContext()) {
                JMSConsumer consumer = context.createConsumer(queue);
                System.out.println("Podsistem 2 (Artikli/Korpa) sluša...");

                while (true) {
                    String msg = consumer.receiveBody(String.class);
                    System.out.println("P2 primio: " + msg);
                    
                    // Primer obrade: "DODAJ_ARTIKAL;Naziv;Cena"
                    // em.getTransaction().begin();
                    // ... tvoja logika ...
                    // em.getTransaction().commit();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
