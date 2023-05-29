package com.company.mutants.service;

import com.company.mutants.model.DNARequest;
import com.company.mutants.model.Stats;

public interface MutantsService {

  boolean isMutant(DNARequest dnaRequest);

  Stats getStats();

}
