package com.company.mutante.entity;


import com.company.mutante.model.DNARequest;
import com.company.mutante.validator.ValidDNA;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Entity
public class DNAEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ValidDNA
  private String [] dna;

  private boolean isMutant;

}
