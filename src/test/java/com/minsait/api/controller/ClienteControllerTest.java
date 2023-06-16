package com.minsait.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.minsait.api.controller.dto.ClienteRequest;
import com.minsait.api.repository.ClienteRepository;
import com.minsait.api.sicurity.util.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

@DisplayName("Teste endpoints de cliente")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ClienteControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    JWTUtil jwtUtil;

    private String token;
    private ObjectWriter ow;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @BeforeEach
    public void init(){
        ow = MAPPER.writer().withDefaultPrettyPrinter();
        ArrayList<String> authorities = new ArrayList<>();
        authorities.add("LEITURA_CLIENTE");
        authorities.add("ESCRITA_CLIENTE");
        token = jwtUtil.generateToken("admin", authorities, 5);
    }

    @Test
    @DisplayName("Deve retornar todos os clientes")
    void clienteFindAll() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/cliente")
                        .header("Authorization", "Bearer ".concat(token))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Deve inserir um cliente")
    void insert() throws Exception {
        final var clienteRequest = new ClienteRequest();
        clienteRequest.setEmail("abdias@outlook.com");
        clienteRequest.setNome("Abdias");
        clienteRequest.setEndereco("Rua K, 123");
        clienteRequest.setTelefone("53 02938423478");
        mvc.perform(MockMvcRequestBuilders.post("/api/cliente")
                        .header("Authorization", "Bearer ".concat(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(clienteRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Abdias"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Deve atualizar um cliente")
    void update() throws Exception {
        final var clienteRequest = new ClienteRequest();
        clienteRequest.setId(5L);
        clienteRequest.setEmail("bismark@outlook.com");
        clienteRequest.setNome("Bismark update");
        clienteRequest.setEndereco("Rua H, 345");
        clienteRequest.setTelefone("53 020980980987");
        mvc.perform(MockMvcRequestBuilders.put("/api/cliente")
                        .header("Authorization", "Bearer ".concat(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(clienteRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Bismark update"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Deve retornar 404 quando tentar atualizar cliente não encontrado")
    void updateNotFound() throws Exception {
        final var clienteRequest = new ClienteRequest();
        clienteRequest.setId(10L);
        clienteRequest.setEmail("bismark@outlook.com");
        clienteRequest.setNome("Bismark update");
        clienteRequest.setEndereco("Rua H, 345");
        clienteRequest.setTelefone("53 020980980987");
        mvc.perform(MockMvcRequestBuilders.put("/api/cliente")
                        .header("Authorization", "Bearer ".concat(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(clienteRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Deve excluir um cliente")
    void delete() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/cliente/2")
                        .header("Authorization", "Bearer ".concat(token))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Deve retornar 404 quando tentar excluir cliente não encontrado")
    void deleteNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/cliente/10")
                        .header("Authorization", "Bearer ".concat(token))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Deve encontrar um cliente pelo id")
    void findById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/cliente/1")
                        .header("Authorization", "Bearer ".concat(token))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}