package com.company.mutants;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.company.mutants.entity.DNAEntity;
import com.company.mutants.model.DNARequest;
import com.company.mutants.model.Stats;
import com.company.mutants.repository.MutantsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class MutantApplicationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MutantsRepository mutantsRepository;


  @BeforeEach
  public void setup() {
    mutantsRepository.deleteAll();
  }

  @Test
  void shouldFindMutantHorizontal() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    DNARequest dnaRequest = DNARequest.builder()
        .dna(new String[]{"abcbbc", "baacca", "accbba", "bbbaca", "baaaac", "baacca"})
        .build();

    String request = objectMapper.writeValueAsString(dnaRequest);
    mockMvc.perform(post("/mutants")
            .contentType("application/json")
            .content(request))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn().getResponse();
  }

  @Test
  void shouldFindMutantVertical() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    DNARequest dnaRequest = DNARequest.builder()
        .dna(new String[]{"abcbbc", "baacca", "baacca", "baacca", "daacca", "aaacca"})
        .build();

    String request = objectMapper.writeValueAsString(dnaRequest);
    mockMvc.perform(post("/mutants")
            .contentType("application/json")
            .content(request))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn().getResponse();
  }

  @Test
  void shouldFindMutantAscDiagonal() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    DNARequest dnaRequest = DNARequest.builder()
        .dna(new String[]{
            "abcbbc",
            "cbccca",
            "bcadcc",
            "bcbaea",
            "cadcaf",
            "acccca"})
        .build();

    String request = objectMapper.writeValueAsString(dnaRequest);
    mockMvc.perform(post("/mutants")
            .contentType("application/json")
            .content(request))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn().getResponse();
  }

  @Test
  void shouldFindMutantDescDiagonal() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    DNARequest dnaRequest = DNARequest.builder()
        .dna(new String[]{
            "abcbbc",
            "cbccaa",
            "bcaacc",
            "bcaaea",
            "cadccf",
            "acccca"})
        .build();

    String request = objectMapper.writeValueAsString(dnaRequest);
    mockMvc.perform(post("/mutants")
            .contentType("application/json")
            .content(request))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn().getResponse();
  }

  @Test
  void shouldFailDueToValidation() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    DNARequest dnaRequest = DNARequest.builder()
        .dna(new String[]{"abcbbc", "baacca", "baacca", "baacca", "baacca"})
        .build();

    String request = objectMapper.writeValueAsString(dnaRequest);
    mockMvc.perform(post("/mutants")
            .contentType("application/json")
            .content(request))
        .andDo(print())
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
        .andReturn().getResponse();
  }

  @Test
  void shouldFailDueToValidation2() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    DNARequest dnaRequest = DNARequest.builder()
        .dna(new String[]{"abcbbc", "baacca", "baacca", "baacca", "baacca", "baac"})
        .build();

    String request = objectMapper.writeValueAsString(dnaRequest);
    mockMvc.perform(post("/mutants")
            .contentType("application/json")
            .content(request))
        .andDo(print())
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
        .andReturn().getResponse();
  }

	@Test
	void shouldFailDueToValidation3() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		DNARequest dnaRequest = DNARequest.builder()
				.dna(new String[]{null, "baacca", "baacca", "baacca", "baacca", "baac"})
				.build();

		String request = objectMapper.writeValueAsString(dnaRequest);
		mockMvc.perform(post("/mutants")
						.contentType("application/json")
						.content(request))
				.andDo(print())
				.andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andReturn().getResponse();
	}

  @Test
  void statsShouldReturn0Ratio() throws Exception {
    Stats expectedStats = Stats.builder()
        .countHumanDna(0)
        .countMutantDna(4)
        .ratio("0.0")
        .build();

    saveDnaEntityList0RatioCase();

    mockMvc.perform(get("/mutants/stats")
            .contentType("application/json"))
        .andDo(print())
        .andExpect(jsonPath("$.count_mutant_dna").value(expectedStats.getCountMutantDna()))
        .andExpect(jsonPath("$.count_human_dna").value(expectedStats.getCountHumanDna()))
        .andExpect(jsonPath("$.ratio").value(expectedStats.getRatio()))
        .andExpect(status().isOk())
        .andReturn().getResponse();
  }

	@Test
	void statsShouldReturn05Ratio() throws Exception {
		Stats expectedStats = Stats.builder()
				.countHumanDna(2)
				.countMutantDna(4)
				.ratio("0.5")
				.build();

		saveDnaEntityList05RatioCase();

		mockMvc.perform(get("/mutants/stats")
						.contentType("application/json"))
				.andDo(print())
				.andExpect(jsonPath("$.count_mutant_dna").value(expectedStats.getCountMutantDna()))
				.andExpect(jsonPath("$.count_human_dna").value(expectedStats.getCountHumanDna()))
				.andExpect(jsonPath("$.ratio").value(expectedStats.getRatio()))
				.andExpect(status().isOk())
				.andReturn().getResponse();
	}

  private void saveDnaEntityList0RatioCase() {
    mutantsRepository.saveAll(get4MutantDNAs());
  }

	private void saveDnaEntityList05RatioCase () {
		mutantsRepository.saveAll(get4MutantDNAs());
		mutantsRepository.saveAll(get2HumanDNAs());
	}

  private List<DNAEntity> get4MutantDNAs() {
    DNAEntity dna1 = DNAEntity.builder()
        .id(1)
        .dna(new String[]{"abcbbc", "baacca", "baacca", "baacca", "baacca", "baacca"})
        .isMutant(true)
        .build();

    DNAEntity dna2 = DNAEntity.builder()
        .id(2)
        .dna(new String[]{"abcbbc", "baacca", "baacca", "baacca", "daacca", "aaacca"})
        .isMutant(true)
        .build();

    DNAEntity dna3 = DNAEntity.builder()
        .id(3)
        .dna(new String[]{"abcbbc", "cbccaa", "bcaacc", "bcaaea", "cadccf", "acccca"})
        .isMutant(true)
        .build();

    DNAEntity dna4 = DNAEntity.builder()
        .id(4)
        .dna(new String[]{"bbcbbc", "baacca", "bbccca", "baacca", "baacca", "bcccca"})
        .isMutant(true)
        .build();

    return List.of(dna1, dna2, dna3, dna4);
  }

  private List<DNAEntity> get2HumanDNAs() {
    DNAEntity dna5 = DNAEntity.builder()
        .id(5)
        .dna(new String[]{"abcbbc", "baacca", "accbba", "bbbaca", "baaacc", "caabbb"})
        .isMutant(false)
        .build();

    DNAEntity dna6 = DNAEntity.builder()
        .id(6)
        .dna(new String[]{"abcbbc", "baacca", "accbba", "bbbaca", "baaacc", "cccbbb"})
        .isMutant(false)
        .build();

    return List.of(dna5, dna6);
  }
}
