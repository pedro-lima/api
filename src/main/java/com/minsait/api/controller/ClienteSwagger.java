package com.minsait.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.minsait.api.controller.dto.ClienteRequest;
import com.minsait.api.controller.dto.ClienteResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface ClienteSwagger extends ApiSwagger<ClienteRequest, ClienteResponse> {

	@Operation(summary = "Busca todos os registros", responses = {
			@ApiResponse(responseCode = "200", description = "Dados do registro retornados com sucesso"),
			@ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
			@ApiResponse(responseCode = "500", description = "Erro interno"),
			@ApiResponse(responseCode = "403", description = "Acesso negado"), })
	public ResponseEntity<Page<ClienteResponse>> findAll(String nome, String endereco, int page, int pagesize);

}
