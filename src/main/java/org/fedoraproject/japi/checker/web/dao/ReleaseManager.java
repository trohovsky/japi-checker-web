package org.fedoraproject.japi.checker.web.dao;

// Generated Feb 26, 2013 4:59:29 PM by Hibernate Tools 3.4.0.CR1

import static org.hibernate.criterion.Example.create;

import java.util.List;

import org.fedoraproject.japi.checker.web.model.Release;
import org.fedoraproject.japi.checker.web.model.Class;
import org.fedoraproject.japi.checker.web.persistence.HibernateUtil;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Home object for domain model class Release.
 * 
 * @see org.fedoraproject.japi.checker.web.dao2.Release
 * @author Hibernate Tools
 */
public class ReleaseManager {

	private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	public void persist(Release transientInstance) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			
			session.persist(transientInstance);
			
			session.getTransaction().commit();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void attachDirty(Release instance) {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void attachClean(Release instance) {
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void delete(Release persistentInstance) {
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Release merge(Release detachedInstance) {
		try {
			Release result = (Release) sessionFactory.getCurrentSession()
					.merge(detachedInstance);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Release findById(int id) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			Release instance = (Release) session 
					.get("org.fedoraproject.japi.checker.web.model.Release", id);
			session.getTransaction().commit();
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<Release> findByExample(Release instance) {
		try {
			List<Release> results = (List<Release>) sessionFactory
					.getCurrentSession()
					.createCriteria(
							"org.fedoraproject.japi.checker.web.model.Release")
					.add(create(instance)).list();
			return results;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Class findClassByName(int id, String name) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Query query = session.createSQLQuery("select * from class where release_id = :releaseId and name = :name").addEntity(Class.class);
		query.setParameter("releaseId", id);
		query.setParameter("name", name);
		
		Class result = (Class) query.uniqueResult();
		session.getTransaction().commit();
				
		return result;
	}
}
