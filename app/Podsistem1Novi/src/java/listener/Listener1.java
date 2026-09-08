/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listener;

import javax.jms.*;
import javax.naming.InitialContext;

public class Listener1 {

    public static void main(String[] args) {
        try {
            InitialContext ictx = new InitialContext(); //inicijal jndi
            
            ConnectionFactory cf = (ConnectionFactory) ictx.lookup("jms/__defaultConnectionFactory");
            Queue queue = (Queue) ictx.lookup("jms/Podsistem1Queue");

            try (JMSContext context = cf.createContext()) {
                JMSConsumer consumer = context.createConsumer(queue);
                System.out.println("Podsistem 1 sluša...");

                while (true) {
                    String msg = consumer.receiveBody(String.class);
                    System.out.println("Primljena poruka: " + msg);
                }
            }
        } catch (Exception e) {
            System.err.println("Greska u Podsistemu 1: " + e.getMessage());
            e.printStackTrace();
        }
    }
}