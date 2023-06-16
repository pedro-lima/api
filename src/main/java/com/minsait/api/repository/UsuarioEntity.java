package com.minsait.api.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USUARIO", schema = "API")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "API.SQ_ID_USUARIO")
    @SequenceGenerator(name="API.SQ_ID_USUARIO", sequenceName = "API.SQ_ID_USUARIO", allocationSize = 1)
    @Column(name = "ID_USUARIO")
    private Long id;

    @Column(name = "NOME", nullable = false, length = 200)
    private String nome;

    @Column(name = "LOGIN", nullable = false, length = 200)
    private String login;

    @Column(name = "SENHA", nullable = false, length = 200)
    private String senha;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "PERMISSOES")
    private String permissoes;
    
	public void setSenhaAndEncode(String senha) {
		if (senha != null) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			senha = encoder.encode(senha);
		}

		this.senha = senha;
	}
    
    public Specification<UsuarioEntity> usuarioEntitySpecification() {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (this.getNome() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")),
                        "%" + this.getNome().trim().toLowerCase() + "%"));
            }

            if (this.getLogin() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("login")),
                        "%" + this.getLogin().trim().toLowerCase() + "%"));
            }

            if (this.getEmail() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),
                        "%" + this.getEmail().trim().toLowerCase() + "%"));
            }            

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
