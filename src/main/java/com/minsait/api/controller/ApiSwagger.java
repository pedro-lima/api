package com.minsait.api.controller;

import org.springframework.http.ResponseEntity;

import com.minsait.api.controller.dto.MessageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name ="endpoints do curso de práticas tecnológicas")
public interface ApiSwagger<U, T> {

    @Operation(summary = "Insere um novo registro",
        responses = {
            @ApiResponse(responseCode = "200", description = "Registro inserido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
        }
    )
    public ResponseEntity<T> insert(U request);

    @Operation(summary = "Atualiza um registro",
        responses = {
            @ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
        }
    )
    public ResponseEntity<T> update(U request);

    @Operation(summary = "Exclui um registro",
        responses = {
            @ApiResponse(responseCode = "200", description = "Registro excluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
        }
    )
    public ResponseEntity<MessageResponse> delete(Long id);

    @Operation(summary = "Busca um registro pelo id",
        responses = {
            @ApiResponse(responseCode = "200", description = "Dados do registro retornados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
        }
    )
    public ResponseEntity<T> findById(Long id);
}
