package com.minsait.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.minsait.api.controller.dto.UsuarioRequest;
import com.minsait.api.controller.dto.UsuarioResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface UsuarioSwagger extends ApiSwagger<UsuarioRequest, UsuarioResponse> {

	@Operation(summary = "Busca todos os registros", responses = {
			@ApiResponse(responseCode = "200", description = "Dados do registro retornados com sucesso"),
			@ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
			@ApiResponse(responseCode = "500", description = "Erro interno"),
			@ApiResponse(responseCode = "403", description = "Acesso negado"), })
	public ResponseEntity<Page<UsuarioResponse>> findAll(String nome, String login, String email, int page,
			int pageSize);

}
