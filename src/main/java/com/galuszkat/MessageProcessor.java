package com.galuszkat;


import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MessageProcessor implements Runnable {

    public static void main(String[] args) {
        Runnable mp = new MessageProcessor();
        Thread t = new Thread(mp);
        t.start();
    }

    @Override
    public void run() {
        Connection connection = null;
        try {
            ActiveMQConnectionFactory jmsConnectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            connection = jmsConnectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue photoQueue = session.createQueue("photos");
            MessageConsumer consumer = session.createConsumer(photoQueue);

            while(true) {
                TextMessage message = (TextMessage) consumer.receive();
                String stringPayload = message.getText();
                System.out.println(stringPayload);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
