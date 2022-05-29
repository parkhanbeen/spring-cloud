package com.example.catalogservice.controller;

import com.example.catalogservice.entity.CatalogEntity;
import com.example.catalogservice.service.CatalogService;
import com.example.catalogservice.vo.ResponseCatalog;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/catalog-service")
public class CatalogController {

  private final Environment environment;
  private final CatalogService catalogService;

  @GetMapping("/health-check")
  public String status() {
    return String.format("It's Working In Catalog Service on Port %s",
        environment.getProperty("local.server.port"));
  }

  @GetMapping("/catalogs")
  public ResponseEntity<List<ResponseCatalog>> getCatalogs() {
    Iterable<CatalogEntity> catalogs = catalogService.getAllCatalogs();
    List<ResponseCatalog> result = new ArrayList<>();

    catalogs.forEach(user ->
        result.add(new ModelMapper().map(user, ResponseCatalog.class))
    );
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(result);
  }

}
