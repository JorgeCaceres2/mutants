package com.company.mutante.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
public class Stats {

  @JsonProperty("count_mutant_dna")
  private int countMutantDna;
  @JsonProperty("count_human_dna")
  private int countHumanDna;
  private String ratio;

}
