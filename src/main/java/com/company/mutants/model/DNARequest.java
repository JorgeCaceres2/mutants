package com.company.mutants.model;

import com.company.mutants.validator.ValidDNA;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DNARequest {

  @ValidDNA
  private String [] dna;

}
