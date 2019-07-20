package com.ggs.cursomc.repositories;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ggs.cursomc.CursomcApplication;

public class DBRepository {

	private HashMap<Class<?>, JpaRepository<?, Integer>> repositories = new HashMap<Class<?>, JpaRepository<?, Integer>>();

	private static DBRepository instance;

	private DBRepository() {
		for (Class<?> class1 : repositoriosAnotados())
			repositories.put(entityClass(class1), newRepo(class1));
	}

	private static Set<Class<?>> repositoriosAnotados() {
		return new Reflections("com.").getTypesAnnotatedWith(Repository.class);
	}

	@SuppressWarnings("unchecked")
	private static JpaRepository<?, Integer> newRepo(Class<?> class1) {
		String name = class1.getSimpleName();
		name = name.substring(0, 1).toLowerCase() + name.substring(1);
		return (JpaRepository<?, Integer>) CursomcApplication.contextUniversal.getBeanFactory().getBean(class1);
	}

	private static Class<?> entityClass(Class<?> entityClass) {
		return (Class<?>)((ParameterizedType)entityClass.getGenericInterfaces()[0]).getActualTypeArguments()[0];
	}

	public static DBRepository instance() {
		if (instance == null)
			instance = new DBRepository();
		return instance;
	}

	public static <T> T save(T obj) {
		if (obj != null)
			repository(obj).save(obj);
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> JpaRepository<T, Integer> repository(T obj) {
		return repository((Class<T>) obj.getClass());
	}

	@SuppressWarnings("unchecked")
	private static <T> JpaRepository<T, Integer> repository(Class<T> entityClass) {
		return (JpaRepository<T, Integer>) instance().repositories.get(entityClass);
	}

	public static <T> List<T> save(List<T> obj) {
		if (obj != null)
			for (T t : obj)
				save(t);
		return obj;
	}

	public static <T> T findOne(Class<T> entityClass, Integer id) {
		return repository(entityClass).findOne(id);
	}

	public static <T> List<T> findAll(Class<T> entityClass) {
		return repository(entityClass).findAll();
	}

	public static <T> void delete(Class<T> entityClass, Integer id) {
		repository(entityClass).delete(id);
	}

	public static <T> Page<T> findAll(Class<T> entityClass, PageRequest pageRequest) {
		return repository(entityClass).findAll(pageRequest);
	}

}