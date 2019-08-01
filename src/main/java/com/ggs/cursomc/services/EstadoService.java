package com.ggs.cursomc.services;

import static com.ggs.cursomc.repositories.DBRepository.repository;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ggs.cursomc.domain.Cidade;
import com.ggs.cursomc.domain.Estado;
import com.ggs.cursomc.dto.CidadeDTO;
import com.ggs.cursomc.dto.EstadoDTO;
import com.ggs.cursomc.repositories.CidadeRepository;
import com.ggs.cursomc.repositories.EstadoRepository;

@Service
public class EstadoService extends AppService<Estado> {

	public List<EstadoDTO> findAllDTO() {
		return repository(EstadoRepository.class)
				.findAllByOrderByNome().stream()
				.map(e -> newEstadoDTO(e)).collect(toList());
	}

	private static EstadoDTO newEstadoDTO(Estado e) {
		EstadoDTO dto = new EstadoDTO();
		dto.setId(e.getId());
		dto.setNome(e.getNome());
		return dto;
	}

	public List<CidadeDTO> findAllCidadesDoEstadoDTO(Integer id) {
		return repository(CidadeRepository.class)
				.findByEstadoOrderByNome(estado(id))
				.stream().map(c -> newCidadeDTO(c))
				.collect(toList());
	}

	private static Estado estado(Integer id) {
		return repository(EstadoRepository.class).findOne(id);
	}
	
	private static CidadeDTO newCidadeDTO(Cidade c) {
		CidadeDTO dto = new CidadeDTO();
		dto.setId(c.getId());
		dto.setNome(c.getNome());
		dto.setEstado(newEstadoDTO(c.getEstado()));
		return dto;
	}

}
