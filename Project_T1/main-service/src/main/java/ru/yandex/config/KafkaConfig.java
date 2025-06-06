package ru.yandex.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    NewTopic createTopic1() {
        return TopicBuilder.name("t1_demo_metrics")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    NewTopic createTopic2() {
        return TopicBuilder.name("t1_demo_transaction_accept")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    NewTopic createTopic3() {
        return TopicBuilder.name("t1_demo_transaction_result")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    NewTopic createTopic4() {
        return TopicBuilder.name("t1_demo_transactions")
                .partitions(3)
                .replicas(1)
                .build();
    }

}
