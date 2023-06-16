package com.minsait.api.controller;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.minsait.api.controller.dto.MessageResponse;
import com.minsait.api.controller.dto.UsuarioRequest;
import com.minsait.api.controller.dto.UsuarioResponse;
import com.minsait.api.repository.UsuarioEntity;
import com.minsait.api.repository.UsuarioRepository;
import com.minsait.api.util.ObjectMapperUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class UsuarioController implements UsuarioSwagger {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@PreAuthorize("hasAuthority('LEITURA_USUARIO')")
	@GetMapping("/usuario")
	public ResponseEntity<Page<UsuarioResponse>> findAll(@RequestParam(required = false) String nome,
																@RequestParam(required = false) String login,
																@RequestParam(required = false) String email,
																@RequestParam(required = false, defaultValue = "0") int page,
																@RequestParam(required = false, defaultValue = "10") int pageSize) {
		final var usuarioEntity = new UsuarioEntity();
		usuarioEntity.setLogin(login);
		usuarioEntity.setEmail(email);
		Pageable pageable = PageRequest.of(page, pageSize);

		final Page<UsuarioEntity> usuarioEntityListPage = usuarioRepository.findAll(usuarioEntity.usuarioEntitySpecification(), pageable);
		final  Page<UsuarioResponse> usuarioResponseList = ObjectMapperUtil.mapAll(usuarioEntityListPage, UsuarioResponse.class);
		return ResponseEntity.ok(usuarioResponseList);
	}

	@PreAuthorize("hasAuthority('ESCRITA_USUARIO')")
	@PostMapping("/usuario")
	public ResponseEntity<UsuarioResponse> insert(@RequestBody UsuarioRequest request){

		final var usuarioEntity = ObjectMapperUtil.map(request, UsuarioEntity.class);
		usuarioEntity.setSenhaAndEncode(request.getSenha());
		
		final var usuarioInserted = usuarioRepository.save(usuarioEntity);
		final var usuarioResponse = ObjectMapperUtil.map(usuarioInserted, UsuarioResponse.class);

		return new ResponseEntity<>(usuarioResponse, HttpStatus.CREATED);
	}

	@PreAuthorize("hasAuthority('ESCRITA_USUARIO')")
	@PutMapping("/usuario")
	public ResponseEntity<UsuarioResponse> update(@RequestBody UsuarioRequest request){
		final UsuarioEntity usuarioRequest = ObjectMapperUtil.map(request, UsuarioEntity.class);
		final var usuarioEntityFound = usuarioRepository.findById(usuarioRequest.getId());

		AtomicReference<Optional<UsuarioEntity>> usuarioRetorno = new AtomicReference<>();
		usuarioEntityFound.ifPresentOrElse((usuarioEntity) -> {
			Optional.ofNullable(usuarioRequest.getNome()).ifPresent(usuarioEntity::setNome);
			Optional.ofNullable(usuarioRequest.getLogin()).ifPresent(usuarioEntity::setLogin);
			Optional.ofNullable(usuarioRequest.getSenha()).ifPresent(usuarioEntity::setSenhaAndEncode);
			Optional.ofNullable(usuarioRequest.getEmail()).ifPresent(usuarioEntity::setEmail);
			Optional.ofNullable(usuarioRequest.getPermissoes()).ifPresent(usuarioEntity::setPermissoes);

			usuarioRetorno.set(Optional.of(usuarioRepository.save(usuarioEntity)));
		}, () -> {
			usuarioRetorno.set(Optional.empty());
		});

		if (usuarioRetorno.get().isEmpty()) {
			return new ResponseEntity<>(new UsuarioResponse(), HttpStatus.NOT_FOUND);
		}

		final var usuarioResponse = ObjectMapperUtil.map(usuarioRetorno.get().get(), UsuarioResponse.class);

		return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('ESCRITA_USUARIO')")
	@DeleteMapping("/usuario/{id}")
	public ResponseEntity<MessageResponse> delete(@PathVariable Long id){
		final var usuarioEntityFound = usuarioRepository.findById(id);
		if(usuarioEntityFound.isPresent()){
			usuarioRepository.delete(usuarioEntityFound.get());
		}else{
			return new ResponseEntity<>(MessageResponse.builder()
					.message("Usuario não encontrado!")
					.date(LocalDateTime.now())
					.error(false)
					.build(), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(MessageResponse.builder()
				.message("OK")
				.date(LocalDateTime.now())
				.error(false)
				.build(), HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('LEITURA_USUARIO')")
	@GetMapping("/usuario/{id}")
	public ResponseEntity<UsuarioResponse> findById(@PathVariable Long id){
		final var usuarioEntity = usuarioRepository.findById(id);
		UsuarioResponse usuarioResponse = new UsuarioResponse();

		if (usuarioEntity.isPresent()){
			usuarioResponse = ObjectMapperUtil.map(usuarioEntity.get(), UsuarioResponse.class);
		}else{
			return new ResponseEntity<>(usuarioResponse, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
	}

}
