package com.company.mutante.controller;

import com.company.mutante.entity.DNAEntity;
import com.company.mutante.model.DNARequest;
import com.company.mutante.model.Stats;
import com.company.mutante.service.MutantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mutants")
public class MutantController {

  private final MutantService mutantService;

  public MutantController(MutantService mutantService) {
    this.mutantService = mutantService;
  }

  @PostMapping()
  public ResponseEntity<?> checkMutant(@RequestBody @Valid DNARequest dnaRequest) {
    if (mutantService.isMutant(dnaRequest)) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
  }

  @GetMapping("/stats")
  public ResponseEntity<Stats> getStats () {
    return ResponseEntity.ok(mutantService.getStats());
  }
}
