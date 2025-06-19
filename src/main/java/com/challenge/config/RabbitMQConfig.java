package com.challenge.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${app.rabbitmq.queue-name}")
    private String queueName;

    /**
     * Define la cola de RabbitMQ. Spring AMQP la creará automáticamente si no existe.
     */
    @Bean
    public Queue queue() {
        return new Queue(queueName, true);
    }

    /**
     * Bean para el convertidor de mensajes. Usamos Jackson para serializar/deserializar JSON.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Configura el RabbitTemplate para usar nuestro convertidor JSON.
     * Este bean sobreescribe el RabbitTemplate por defecto de Spring Boot.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
