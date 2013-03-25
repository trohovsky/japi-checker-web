package org.fedoraproject.japi.checker.web.dao;

// Generated Feb 26, 2013 4:59:29 PM by Hibernate Tools 3.4.0.CR1

import static org.hibernate.criterion.Example.create;

import java.util.List;

import org.fedoraproject.japi.checker.web.model.Class;
import org.fedoraproject.japi.checker.web.persistence.HibernateUtil;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Home object for domain model class Class.
 * 
 * @see org.fedoraproject.japi.checker.web.dao2.Class
 * @author Hibernate Tools
 */
public class ClassManager {

	private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	public void persist(Class transientInstance) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			
			session.persist(transientInstance);
			
			session.getTransaction().commit();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void attachDirty(Class instance) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			session.saveOrUpdate(instance);
			session.getTransaction().commit();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void attachClean(Class instance) {
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void delete(Class persistentInstance) {
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Class merge(Class detachedInstance) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			Class result = (Class) session.merge(
					detachedInstance);
			session.getTransaction().commit();
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Class findById(int id) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			Class instance = (Class) session.get(
					"org.fedoraproject.japi.checker.web.model.Class", id);
			session.getTransaction().commit();
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<Class> findByExample(Class instance) {
		try {
			List<Class> results = (List<Class>) sessionFactory
					.getCurrentSession()
					.createCriteria(
							"org.fedoraproject.japi.checker.web.model.Class")
					.add(create(instance)).list();
			return results;
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
