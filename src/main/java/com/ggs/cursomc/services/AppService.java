package com.ggs.cursomc.services;

import static java.lang.String.format;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ggs.cursomc.domain.AppEntity;
import com.ggs.cursomc.services.exceptions.DataIntegrityException;
import com.ggs.cursomc.services.exceptions.ObjectNotFoundException;

public abstract class AppService<T extends AppEntity> {

	public T find(Integer id) {
		T obj = repository().findOne(id);
		if (obj != null)
			return obj;
		throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + nameClass());
	}

	private String nameClass() {
		return classEntity().getTypeName();
	}

	@SuppressWarnings("unchecked")
	private Class<T> classEntity() {
		return (Class<T>)((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	protected abstract JpaRepository<T, Integer> repository();

	public T insert(T c) {
		c.setId(null);
		return repository().save(c);
	}

	public T update(T c) {
		T obj = find(c.getId());
		updateData(obj, c);
		return repository().save(obj);
	}
	
	protected void updateData(T oldObj, T newObj) {};

	public void delete(Integer id) {
		find(id);
		try {
			repository().delete(id);
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException(format("Não é possível excluir %s que possua associações", classEntity().getSimpleName()));
		}
	}
	
	public List<T> findAll(){
		return repository().findAll();
	}
	
	public Page<T> findPage(Integer page, Integer size, String order, String direction){
		PageRequest pageRequest = new PageRequest(page, size, Direction.valueOf(direction), order);
		return repository().findAll(pageRequest);
	}

}
