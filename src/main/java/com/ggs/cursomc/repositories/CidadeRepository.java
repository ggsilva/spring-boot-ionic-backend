package com.ggs.cursomc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ggs.cursomc.domain.Cidade;
import com.ggs.cursomc.domain.Estado;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer>{

	@Transactional
	List<Cidade> findByEstadoOrderByNome(Estado estado);
	
}
