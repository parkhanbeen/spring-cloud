package com.example.catalogservice.messagequeue;

import java.util.HashMap;
import java.util.Map;

import com.example.catalogservice.entity.CatalogEntity;
import com.example.catalogservice.entity.CatalogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaConsumer {
  private final CatalogRepository catalogRepository;

  @KafkaListener(topics = "example-catalog-topic")
  public void updateQuantity(String kafkaMessage) {
    log.info("kafka Message -> {}", kafkaMessage);

    Map<String, Object> map = new HashMap<>();

    ObjectMapper mapper = new ObjectMapper();

    try {
      map = mapper.readValue(kafkaMessage, new TypeReference<Map<String, Object>>() {});
    } catch (JsonProcessingException jpe) {
      log.error("JsonProcessingException -> ", jpe);
    }

    CatalogEntity findCatalog = catalogRepository
        .findByProductId((String) map.get("productId"));

    if (findCatalog != null) {
      findCatalog.updateStock(findCatalog.getStock() - (Integer) map.get("qty"));
    }

    catalogRepository.save(findCatalog);
  }
}
