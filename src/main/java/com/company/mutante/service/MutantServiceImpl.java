package com.company.mutante.service;

import com.company.mutante.entity.DNAEntity;
import com.company.mutante.model.DNARequest;
import com.company.mutante.model.Stats;
import com.company.mutante.repository.MutantRepository;
import java.text.DecimalFormat;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MutantServiceImpl implements MutantService {

  private final MutantRepository mutantRepository;

  private static final int ARRAY_LENGTH = 6;

  public MutantServiceImpl(MutantRepository mutantRepository) {
    this.mutantRepository = mutantRepository;
  }

  @Override
  public boolean isMutant(DNARequest dnaRequest) {

    boolean isMutant = isMutantByHorizontalSequence(dnaRequest) || isMutantByVerticalSequence(dnaRequest) ||
        isMutantByAscDiagonalSequence(dnaRequest) || isMutantByDescDiagonalSequence(dnaRequest);

    if (!mutantRepository.existsByDna(dnaRequest.getDna())) {
      DNAEntity dnaEntity = DNAEntity.builder()
          .dna(dnaRequest.getDna())
          .isMutant(isMutant)
          .build();
      mutantRepository.save(dnaEntity);
    }

    return isMutant;
  }

  @Override
  public Stats getStats() {
    List<DNAEntity> dnaEntityList = mutantRepository.findAll();
    int mutantCount = (int) dnaEntityList.stream().filter(DNAEntity::isMutant).count();
    int humanCount = dnaEntityList.size() - mutantCount;
    double ratio = (double) mutantCount /humanCount;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");
    Stats stats = Stats.builder()
        .countMutantDna(mutantCount)
        .countHumanDna(humanCount)
        .ratio(decimalFormat.format(ratio))
        .build();
    log.info("Returning stats: {}", stats);
    return stats;
  }

  public boolean isMutantByHorizontalSequence(DNARequest dnaRequest) {
    for (int i = 0; i < dnaRequest.getDna().length; i++) {
      if (has4RepeatedCharacters(dnaRequest.getDna()[i])) {
        log.info("Horizontal repeated characters found {}", dnaRequest.getDna()[i]);
        return true;
      }
    }
    return false;
  }

  public boolean isMutantByVerticalSequence(DNARequest dnaRequest) {
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

  public boolean isMutantByDescDiagonalSequence(DNARequest dnaRequest) {
    for (int row = 0; row < ARRAY_LENGTH - 3; row++) {
      for (int col = ARRAY_LENGTH - 1; col > ARRAY_LENGTH - 3; col--) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
          stringBuilder.append(dnaRequest.getDna()[row + i].charAt(col - i));
        }
        log.info("DescDiagonal row[{}] col[{}] string:{}", row, col, stringBuilder);
        if (has4RepeatedCharacters(stringBuilder.toString())) {
          log.info("Repeated characters found in Descendant Diagonal {}", stringBuilder);
          return true;
        }
      }
    }
    return false;
  }

  public boolean isMutantByAscDiagonalSequence(DNARequest dnaRequest) {
    for (int row = 0; row < ARRAY_LENGTH - 3; row++) {
      for (int col = 0; col < ARRAY_LENGTH - 3; col++) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
          stringBuilder.append(dnaRequest.getDna()[row + i].charAt(col + i));
        }
        log.info("AscDiagonal row[{}] col[{}] string:{}", row, col, stringBuilder);
        if (has4RepeatedCharacters(stringBuilder.toString())) {
          log.info("Repeated characters found in Ascendant Diagonal {}", stringBuilder);
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
}
