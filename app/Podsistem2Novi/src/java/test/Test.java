/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author User
 */

import javax.jms.*;
import javax.naming.InitialContext;

public class Test {
    public static void main(String[] args) {
        try {
            InitialContext ictx = new InitialContext();
            ConnectionFactory cf = (ConnectionFactory) ictx.lookup("jms/__defaultConnectionFactory");
            Queue queue = (Queue) ictx.lookup("jms/Podsistem3Queue");

            try (JMSContext context = cf.createContext()) {
                System.out.println("Slanje test poruke...");
                context.createProducer().send(queue, "TEST_PORUKA_OD_PRODUCENTA");
                System.out.println("Poruka poslata!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
