package com.ggs.cursomc.repositories;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
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
			repositoryOfEntity(obj).save(obj);
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> JpaRepository<T, Integer> repositoryOfEntity(T obj) {
		return repositoryOfEntityClass((Class<T>) obj.getClass());
	}

	@SuppressWarnings("unchecked")
	private static <T> JpaRepository<T, Integer> repositoryOfEntityClass(Class<T> entityClass) {
		JpaRepository<?, Integer> repo = repositories().get(entityClass);
		
		if(isNeedVerifySuperClass(entityClass, repo))
			repo = repositoryOfEntityClass(entityClass.getSuperclass());
		
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

	public static <T> Collection<T> save(Collection<T> obj) {
		if (obj != null)
			for (T t : obj)
				save(t);
		return obj;
	}

	public static <T> T findOne(Class<T> entityClass, Integer id) {
		return repositoryOfEntityClass(entityClass).findOne(id);
	}

	public static <T> List<T> findAll(Class<T> entityClass) {
		return repositoryOfEntityClass(entityClass).findAll();
	}

	public static <T> void delete(Class<T> entityClass, Integer id) {
		repositoryOfEntityClass(entityClass).delete(id);
	}

	public static <T> Page<T> findAll(Class<T> entityClass, PageRequest pageRequest) {
		return repositoryOfEntityClass(entityClass).findAll(pageRequest);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public static <T extends JpaRepository> T repository(Class<T> classRepo) {
		for (JpaRepository<?, Integer> jpaRepository : instance().repositories().values())
			for (Class interfaceClass : jpaRepository.getClass().getInterfaces())
				if(interfaceClass.equals(classRepo))
					return (T)jpaRepository;
		return null;
	}

}