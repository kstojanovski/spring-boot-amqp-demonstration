package com.acme.springamqp_demonstration.message.importanttopics;

import com.acme.springamqp_demonstration.message.CurrentDateTimeProvider;
import com.acme.springamqp_demonstration.message.importanttopics.model.ImportantTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@PropertySource("classpath:important-topics.properties")
@Service
public class ImportantTopicsForErrorSender {

  private static final Logger LOGGER = LoggerFactory.getLogger(ImportantTopicsForErrorSender.class);

  @Value("${important.topics.exchange.name2}")
  public String IMPORTANT_TOPICS_EXCHANGE_NAME_2;

  private final RabbitTemplate rabbitTemplate;

  private final CurrentDateTimeProvider currentDateTimeProvider = new CurrentDateTimeProvider();

  public ImportantTopicsForErrorSender(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @Scheduled(cron = "${important.topics.sender2.cron}")
  private void reportCurrentTime() {
    sendImportantTopicsObjects(rabbitTemplate);
  }

  private void sendImportantTopicsObjects(RabbitTemplate rabbitTemplate) {
    String message = "Important Topics - general 2";
    String currentDateTime = currentDateTimeProvider.getCurrentDateTime();
    LOGGER.info("Sending following Important Topics {} and current date time {}", message, currentDateTime);
    rabbitTemplate.convertAndSend(IMPORTANT_TOPICS_EXCHANGE_NAME_2, "com.acme.general", new ImportantTopic(message, currentDateTime));
  }

}
