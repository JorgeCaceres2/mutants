package com.company.mutants.controller;

import com.company.mutants.exception.MutantException;
import com.company.mutants.exception.StatsException;
import com.company.mutants.model.DNARequest;
import com.company.mutants.service.MutantsService;
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
public class MutantsController {

  private final MutantsService mutantsService;

  public MutantsController(MutantsService mutantsService) {
    this.mutantsService = mutantsService;
  }

  @PostMapping()
  public ResponseEntity<?> checkMutant(@RequestBody @Valid DNARequest dnaRequest) {
    try {
      if (mutantsService.isMutant(dnaRequest)) {
        return ResponseEntity.ok().build();
      } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }
    } catch (MutantException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid DNA: " + e.getMessage());
    }
  }

  @GetMapping("/stats")
  public ResponseEntity<?> getStats() {
    try {
      return ResponseEntity.ok(mutantsService.getStats());
    } catch (StatsException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Stats result error: " + e.getMessage());
    }
  }
}
