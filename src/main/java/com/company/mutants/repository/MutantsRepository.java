package com.company.mutants.repository;

import com.company.mutants.entity.DNAEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MutantsRepository extends JpaRepository<DNAEntity, Integer> {

  boolean existsByDna(String[] dna);

}
