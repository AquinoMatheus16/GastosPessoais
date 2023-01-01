package br.com.gastospessoais.domain.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

//@Entity
public class Titulo {

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "idTitulo")
//	private Long id;
//
//	@ManyToMany(mappedBy = "titulo")
//	@JsonBackReference
//	List<CentroDeCusto> centroDeCusto;
}
