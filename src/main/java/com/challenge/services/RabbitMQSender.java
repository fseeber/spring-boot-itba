// src/main/java/com/tuporyecto/services/RabbitMQSender.java
package com.challenge.services;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RabbitMQSender {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQSender.class);

    private final AmqpTemplate rabbitTemplate;
    private final String queueName;

    public RabbitMQSender(AmqpTemplate rabbitTemplate, @Value("${app.rabbitmq.queue-name}") String queueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
    }

    /**
     * Envía un mensaje al RabbitMQ.
     *
     * @param message El objeto a enviar (Spring lo serializará a JSON por defecto si Jackson está en el classpath).
     */
    public void send(Object message) {
        rabbitTemplate.convertAndSend(queueName, message);
        logger.info("Mensaje enviado a la cola '{}': {}", queueName, message);
    }
}