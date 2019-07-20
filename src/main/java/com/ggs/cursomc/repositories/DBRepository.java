package com.ggs.cursomc.repositories;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;

import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ggs.cursomc.CursomcApplication;

public class DBRepository {

	private HashMap<Class<?>, JpaRepository<?, Integer>> repositories = new HashMap<Class<?>, JpaRepository<?, Integer>>();

	private static DBRepository instance;

	private ApplicationContext appContext;

	public static DBRepository instance() {
		if (instance == null)
			instance = new DBRepository();
		return instance;
	}
	
	public void setContext(ApplicationContext appContext) {
		this.appContext = appContext;
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
		JpaRepository<?, Integer> repo = repositories().get(entityClass);
		
		if(isNeedVerifySuperClass(entityClass, repo))
			repo = repository(entityClass.getSuperclass());
		
		return (JpaRepository<T, Integer>) repo;
	}

	private static <T> boolean isNeedVerifySuperClass(Class<T> entityClass, JpaRepository<?, Integer> repo) {
		return repo == null && isSuperClassEntity(entityClass);
	}

	private static <T> boolean isSuperClassEntity(Class<T> entityClass) {
		Entity[] annotationSuperClass = entityClass.getSuperclass().getAnnotationsByType(Entity.class);
		return annotationSuperClass != null && annotationSuperClass.length > 0;
	}

	private static HashMap<Class<?>, JpaRepository<?, Integer>> repositories() {
		if(notInit())
			instance().init();
		return instance().repositories;
	}

	private static boolean notInit() {
		return instance().repositories == null || instance().repositories.isEmpty();
	}

	private void init() {
		for (Class<?> class1 : repositoriosAnotados())
			repositories.put(entityClass(class1), newRepo(class1));
	}

	private static Set<Class<?>> repositoriosAnotados() {
		return new Reflections("com.").getTypesAnnotatedWith(Repository.class);
	}

	@SuppressWarnings("unchecked")
	private static JpaRepository<?, Integer> newRepo(Class<?> class1) {
		return (JpaRepository<?, Integer>) instance().appContext.getBean(class1);
	}

	private static Class<?> entityClass(Class<?> entityClass) {
		return (Class<?>)((ParameterizedType)entityClass.getGenericInterfaces()[0]).getActualTypeArguments()[0];
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