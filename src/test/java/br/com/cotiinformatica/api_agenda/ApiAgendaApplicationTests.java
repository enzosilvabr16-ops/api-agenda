package br.com.cotiinformatica.api_agenda;

import br.com.cotiinformatica.api_agenda.dtos.CategoriaRequest;
import br.com.cotiinformatica.api_agenda.dtos.CategoriaResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiAgendaApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@Test
	@DisplayName("Deve cadastrar uma categoria com sucesso.")
	void cadastrarCategoriaTest() {
		try {

			var faker = new Faker();
			var request = new CategoriaRequest(faker.commerce().department());

			var response = mockMvc.perform(post("/api/v1/categorias/cadastrar")
							.contentType("application/json")
							.content(mapper.writeValueAsString(request)))
					.andExpect(status().isCreated())
					.andReturn();

			var content = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
			var categoria = mapper.readValue(content, CategoriaResponse.class);

			assertTrue(categoria.id() > 0);
			assertEquals(categoria.nome(), request.nome());
		}
		catch(Exception e) {
			fail("Falha ao cadastrar categoria: " + e.getMessage());
		}
	}

	@Test
	@DisplayName("Deve atualizar uma categoria com sucesso.")
	void atualizarCategoriaTest() {
		try {

			var faker = new Faker();

			// Primeiro cadastra uma categoria
			var requestCadastro = new CategoriaRequest(faker.commerce().department());

			var responseCadastro = mockMvc.perform(post("/api/v1/categorias/cadastrar")
							.contentType("application/json")
							.content(mapper.writeValueAsString(requestCadastro)))
					.andExpect(status().isCreated())
					.andReturn();

			var contentCadastro = responseCadastro.getResponse().getContentAsString(StandardCharsets.UTF_8);
			var categoriaCadastrada = mapper.readValue(contentCadastro, CategoriaResponse.class);

			// Depois atualiza a categoria cadastrada
			var requestAtualizacao = new CategoriaRequest(faker.commerce().department());

			var responseAtualizacao = mockMvc.perform(put("/api/v1/categorias/atualizar/" + categoriaCadastrada.id())
							.contentType("application/json")
							.content(mapper.writeValueAsString(requestAtualizacao)))
					.andExpect(status().isOk())
					.andReturn();

			var contentAtualizacao = responseAtualizacao.getResponse().getContentAsString(StandardCharsets.UTF_8);
			var categoriaAtualizada = mapper.readValue(contentAtualizacao, CategoriaResponse.class);

			assertEquals(categoriaCadastrada.id(), categoriaAtualizada.id());
			assertEquals(requestAtualizacao.nome(), categoriaAtualizada.nome());
			assertNotEquals(requestCadastro.nome(), categoriaAtualizada.nome());
		}
		catch(Exception e) {
			fail("Falha ao atualizar categoria: " + e.getMessage());
		}
	}

	@Test
	@DisplayName("Deve excluir uma categoria com sucesso.")
	void excluirCategoriaTest() {
		try {

			var faker = new Faker();

			// Primeiro cadastra uma categoria
			var request = new CategoriaRequest(faker.commerce().department());

			var responseCadastro = mockMvc.perform(post("/api/v1/categorias/cadastrar")
							.contentType("application/json")
							.content(mapper.writeValueAsString(request)))
					.andExpect(status().isCreated())
					.andReturn();

			var contentCadastro = responseCadastro.getResponse().getContentAsString(StandardCharsets.UTF_8);
			var categoriaCadastrada = mapper.readValue(contentCadastro, CategoriaResponse.class);

			// Depois exclui a categoria cadastrada
			mockMvc.perform(delete("/api/v1/categorias/excluir/" + categoriaCadastrada.id()))
					.andExpect(status().isOk());

			// Confirma se a categoria não pode mais ser obtida
			mockMvc.perform(get("/api/v1/categorias/obter/" + categoriaCadastrada.id()))
					.andExpect(status().isNotFound());
		}
		catch(Exception e) {
			fail("Falha ao excluir categoria: " + e.getMessage());
		}
	}

	@Test
	@DisplayName("Deve consultar todas as categorias com sucesso.")
	void consultarCategoriasTest() {
		try {

			var faker = new Faker();

			// Cadastra uma categoria para garantir que haverá pelo menos um registro
			var request = new CategoriaRequest(faker.commerce().department());

			mockMvc.perform(post("/api/v1/categorias/cadastrar")
							.contentType("application/json")
							.content(mapper.writeValueAsString(request)))
					.andExpect(status().isCreated());

			// Consulta todas as categorias
			var response = mockMvc.perform(get("/api/v1/categorias/consultar"))
					.andExpect(status().isOk())
					.andReturn();

			var content = response.getResponse().getContentAsString(StandardCharsets.UTF_8);

			var categorias = mapper.readValue(
					content,
					new TypeReference<List<CategoriaResponse>>() {}
			);

			assertNotNull(categorias);
			assertFalse(categorias.isEmpty());
		}
		catch(Exception e) {
			fail("Falha ao consultar categorias: " + e.getMessage());
		}
	}

	@Test
	@DisplayName("Deve obter 1 categoria com sucesso através do ID.")
	void obterCategoriaPorIdTest() {
		try {

			var faker = new Faker();

			// Primeiro cadastra uma categoria
			var request = new CategoriaRequest(faker.commerce().department());

			var responseCadastro = mockMvc.perform(post("/api/v1/categorias/cadastrar")
							.contentType("application/json")
							.content(mapper.writeValueAsString(request)))
					.andExpect(status().isCreated())
					.andReturn();

			var contentCadastro = responseCadastro.getResponse().getContentAsString(StandardCharsets.UTF_8);
			var categoriaCadastrada = mapper.readValue(contentCadastro, CategoriaResponse.class);

			// Depois busca a categoria pelo ID
			var responseConsulta = mockMvc.perform(get("/api/v1/categorias/obter/" + categoriaCadastrada.id()))
					.andExpect(status().isOk())
					.andReturn();

			var contentConsulta = responseConsulta.getResponse().getContentAsString(StandardCharsets.UTF_8);
			var categoriaObtida = mapper.readValue(contentConsulta, CategoriaResponse.class);

			assertEquals(categoriaCadastrada.id(), categoriaObtida.id());
			assertEquals(categoriaCadastrada.nome(), categoriaObtida.nome());
			assertEquals(request.nome(), categoriaObtida.nome());
		}
		catch(Exception e) {
			fail("Falha ao obter categoria por ID: " + e.getMessage());
		}
	}
}