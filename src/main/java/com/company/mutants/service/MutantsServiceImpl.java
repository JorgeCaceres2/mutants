package com.company.mutants.service;

import com.company.mutants.entity.DNAEntity;
import com.company.mutants.exception.MutantException;
import com.company.mutants.exception.StatsException;
import com.company.mutants.model.DNARequest;
import com.company.mutants.model.Stats;
import com.company.mutants.repository.MutantsRepository;
import java.text.DecimalFormat;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MutantsServiceImpl implements MutantsService {

  private final MutantsRepository mutantsRepository;

  private static final int ARRAY_LENGTH = 6;

  public MutantsServiceImpl(MutantsRepository mutantsRepository) {
    this.mutantsRepository = mutantsRepository;
  }

  @Override
  public boolean isMutant(DNARequest dnaRequest) {

    boolean isMutant = isMutantByHorizontalSequence(dnaRequest) || isMutantByVerticalSequence(dnaRequest) ||
        isMutantByAscDiagonalSequence(dnaRequest) || isMutantByDescDiagonalSequence(dnaRequest);

    try {
      if (!mutantsRepository.existsByDna(dnaRequest.getDna())) {
        DNAEntity dnaEntity = DNAEntity.builder()
            .dna(dnaRequest.getDna())
            .isMutant(isMutant)
            .build();
        mutantsRepository.save(dnaEntity);
      }
    } catch (Exception e) {
      log.error("Error checking existence or saving a new entity in database. Error: {}", e.getMessage());
      throw new MutantException("Error getting results or saving in repository");
    }

    return isMutant;
  }

  @Override
  public Stats getStats() {
    List<DNAEntity> dnaEntityList;

    try {
      dnaEntityList = mutantsRepository.findAll();
    } catch (Exception e) {
      log.error("Error getting database results. Error: {}", e.getMessage());
      throw new StatsException("Unable to build stats due to repository error");
    }

    int mutantCount = (int) dnaEntityList.stream().filter(DNAEntity::isMutant).count();
    int humanCount = dnaEntityList.size() - mutantCount;
    String ratio = getRatioResult(mutantCount, humanCount);

    Stats stats = Stats.builder()
        .countMutantDna(mutantCount)
        .countHumanDna(humanCount)
        .ratio(ratio)
        .build();

    log.info("Returning stats: {}", stats);
    return stats;
  }

  private boolean isMutantByHorizontalSequence(DNARequest dnaRequest) {
    for (int i = 0; i < dnaRequest.getDna().length; i++) {
      if (has4RepeatedCharacters(dnaRequest.getDna()[i])) {
        log.info("Horizontal repeated characters found {}", dnaRequest.getDna()[i]);
        return true;
      }
    }
    return false;
  }

  private boolean isMutantByVerticalSequence(DNARequest dnaRequest) {
    for (int i = 0; i < dnaRequest.getDna().length; i++) {
      StringBuilder stringBuilder = new StringBuilder();
      int rowLength = dnaRequest.getDna()[i].length();
      for (int j = 0; j < rowLength; j++) {
        stringBuilder.append(dnaRequest.getDna()[j].charAt(i));
      }
      if (has4RepeatedCharacters(stringBuilder.toString())) {
        log.info("Vertical repeated characters found {}", stringBuilder);
        return true;
      }
    }
    return false;
  }

  private boolean isMutantByDescDiagonalSequence(DNARequest dnaRequest) {
    for (int row = 0; row < ARRAY_LENGTH - 3; row++) {
      for (int col = ARRAY_LENGTH - 1; col > ARRAY_LENGTH - 3; col--) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
          stringBuilder.append(dnaRequest.getDna()[row + i].charAt(col - i));
        }
        if (has4RepeatedCharacters(stringBuilder.toString())) {
          log.info("Descendant diagonal repeated characters found {}", stringBuilder);
          return true;
        }
      }
    }
    return false;
  }

  private boolean isMutantByAscDiagonalSequence(DNARequest dnaRequest) {
    for (int row = 0; row < ARRAY_LENGTH - 3; row++) {
      for (int col = 0; col < ARRAY_LENGTH - 3; col++) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
          stringBuilder.append(dnaRequest.getDna()[row + i].charAt(col + i));
        }
        if (has4RepeatedCharacters(stringBuilder.toString())) {
          log.info("Ascendant diagonal repeated characters found {}", stringBuilder);
          return true;
        }
      }
    }
    return false;
  }


  private boolean has4RepeatedCharacters(String sequence) {
    for (int i = 0; i < sequence.length() - 3; i++) {
      if (sequence.charAt(i) == sequence.charAt(i + 1) &&
          sequence.charAt(i) == sequence.charAt(i + 2) &&
          sequence.charAt(i) == sequence.charAt(i + 3)) {
        return true;
      }
    }
    return false;
  }

  private String getRatioResult(int mutantCount, int humanCount) {
    DecimalFormat decimalFormat = new DecimalFormat("0.0");
    double ratio;

    if (mutantCount == 0 || humanCount == 0) {
      ratio = 0;
    } else if (mutantCount > humanCount) {
      ratio = (double) humanCount / mutantCount;
    } else {
      ratio = (double) mutantCount / humanCount;
    }

    return decimalFormat.format(ratio);
  }
}
