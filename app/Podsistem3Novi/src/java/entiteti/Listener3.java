/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;
//package podsistem3;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Listener3 {

    public static void main(String[] args) {
        // Povezivanje sa bazom (u persistence.xml mora biti "Podsistem3PU")
        // Mora da bude tačno onako kako piše u name atributu persistence-unit-a
EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem3NoviPU");
        EntityManager em = emf.createEntityManager();

        try {
            InitialContext ictx = new InitialContext();
            ConnectionFactory cf = (ConnectionFactory) ictx.lookup("jms/__defaultConnectionFactory");
            Queue queue = (Queue) ictx.lookup("jms/Podsistem3Queue");

            try (JMSContext context = cf.createContext()) {
                JMSConsumer consumer = context.createConsumer(queue);
                System.out.println("Podsistem 3 (Narudžbine) sluša...");

                while (true) {
                    String msg = consumer.receiveBody(String.class);
                    System.out.println("P3 primio: " + msg);
                    
                    // Primer obrade: "KREIRAJ_NARUDZBINU;idKupca;ukupno..."
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}