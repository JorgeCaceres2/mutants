package com.company.mutante.repository;

import com.company.mutante.entity.DNAEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MutantRepository extends JpaRepository<DNAEntity, Integer> {

  boolean existsByDna(String[] dna);

}
