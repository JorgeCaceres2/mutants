package com.company.mutants.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DNAValidator implements ConstraintValidator<ValidDNA, String[]> {

  @Override
  public boolean isValid(String[] dna, ConstraintValidatorContext context) {
    if (dna == null || dna.length != 6) {
      return false;
    }

    for (String sequence : dna) {
      if (sequence == null || sequence.length() != 6) {
        return false;
      }
    }

    return true;
  }
}
