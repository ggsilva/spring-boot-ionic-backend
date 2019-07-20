package com.ggs.cursomc.services;

import static java.lang.String.format;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

import com.ggs.cursomc.domain.AppEntity;
import com.ggs.cursomc.repositories.DBRepository;
import com.ggs.cursomc.services.exceptions.DataIntegrityException;
import com.ggs.cursomc.services.exceptions.ObjectNotFoundException;

public abstract class AppService<T extends AppEntity> {

	public T find(Integer id) {
		T obj = DBRepository.findOne(entityClass(), id);
		if (obj != null)
			return obj;
		throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + nameClass());
	}

	private String nameClass() {
		return entityClass().getTypeName();
	}

	@SuppressWarnings("unchecked")
	private Class<T> entityClass() {
		return (Class<T>)((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@Transactional
	public T insert(T c) {
		c.setId(null);
		return DBRepository.save(c);
	}

	public T update(T c) {
		T obj = find(c.getId());
		updateData(obj, c);
		return DBRepository.save(obj);
	}
	
	protected void updateData(T oldObj, T newObj) {};

	public void delete(Integer id) {
		find(id);
		try {
			DBRepository.delete(entityClass(), id);
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException(format("Não é possível excluir %s que possua associações", entityClass().getSimpleName()));
		}
	}
	
	public List<T> findAll(){
		return DBRepository.findAll(entityClass());
	}
	
	public Page<T> findPage(Integer page, Integer size, String order, String direction){
		PageRequest pageRequest = new PageRequest(page, size, Direction.valueOf(direction), order);
		return DBRepository.findAll(entityClass(), pageRequest);
	}

}
