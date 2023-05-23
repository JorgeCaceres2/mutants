package com.company.mutante.service;

import com.company.mutante.model.DNARequest;
import com.company.mutante.model.Stats;

public interface MutantService {

  boolean isMutant(DNARequest dnaRequest);

  Stats getStats();

}
