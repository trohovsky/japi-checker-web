package org.fedoraproject.japi.checker.web.dao;

// Generated Feb 26, 2013 4:59:29 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;

import org.fedoraproject.japi.checker.web.model.Library;
import org.fedoraproject.japi.checker.web.persistence.HibernateUtil;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class Library.
 * 
 * @see org.fedoraproject.japi.checker.web.dao2.Library
 * @author Hibernate Tools
 */
public class LibraryManager {

	private final SessionFactory sessionFactory = HibernateUtil
			.getSessionFactory();

	/*
	 * protected SessionFactory getSessionFactory() { try { return
	 * (SessionFactory) new InitialContext() .lookup("SessionFactory"); } catch
	 * (Exception e) { throw new IllegalStateException(
	 * "Could not locate SessionFactory in JNDI"); } }
	 */

	public void persist(Library transientInstance) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			
			session.persist(transientInstance);
			
			session.getTransaction().commit();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void attachDirty(Library instance) {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void attachClean(Library instance) {
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void delete(Library persistentInstance) {
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Library merge(Library detachedInstance) {
		try {
			Library result = (Library) sessionFactory.getCurrentSession()
					.merge(detachedInstance);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Library findById(int id) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			
			Library instance = (Library) session 
					.get("org.fedoraproject.japi.checker.web.model.Library", id);
			
			session.getTransaction().commit();
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<Library> findByExample(Library instance) {
		try {
			List<Library> results = (List<Library>) sessionFactory
					.getCurrentSession()
					.createCriteria(
							"org.fedoraproject.japi.checker.web.model.Library")
					.add(create(instance)).list();
			return results;
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
