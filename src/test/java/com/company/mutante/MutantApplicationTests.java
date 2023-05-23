package com.company.mutante;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.company.mutante.entity.DNAEntity;
import com.company.mutante.model.DNARequest;
import com.company.mutante.repository.MutantRepository;
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
	private MutantRepository mutantRepository;



	@BeforeEach
	public void setup() {
		mutantRepository.deleteAll();
	}

  @Test
  void shouldFindMutantHorizontal() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    DNARequest dnaRequest = DNARequest.builder()
        .dna(new String[]{"abcbbc", "baacca", "baacca", "baacca", "baacca", "baacca"})
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
				.dna(new String[]{"abcbbc", "baacca", "baacca", "baacca", "baacca","baac"})
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
	void checkStats () throws Exception {
		saveDnaEntityList();

		mockMvc.perform(get("/mutants/stats")
						.contentType("application/json"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn().getResponse();


	}

	private void saveDnaEntityList() {
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

		mutantRepository.saveAll(List.of(dna1, dna2, dna3, dna4));
	}
}
