package org.fedoraproject.japi.checker.web.dao;

// Generated Feb 26, 2013 4:59:29 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;

import org.fedoraproject.japi.checker.web.model.Difference;
import org.fedoraproject.japi.checker.web.persistence.HibernateUtil;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class Difference.
 * 
 * @see org.fedoraproject.japi.checker.web.dao2.Difference
 * @author Hibernate Tools
 */
public class DifferenceManager {

	private final SessionFactory sessionFactory = HibernateUtil
			.getSessionFactory();

	public void persist(Difference transientInstance) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			session.persist(transientInstance);
			session.getTransaction().commit();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void attachDirty(Difference instance) {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void attachClean(Difference instance) {
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void delete(Difference persistentInstance) {
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Difference merge(Difference detachedInstance) {
		try {
			Difference result = (Difference) sessionFactory.getCurrentSession()
					.merge(detachedInstance);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Difference findById(int id) {
		try {
			Difference instance = (Difference) sessionFactory
					.getCurrentSession()
					.get("org.fedoraproject.japi.checker.web.model.Difference",
							id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<Difference> findByExample(Difference instance) {
		try {
			List<Difference> results = (List<Difference>) sessionFactory
					.getCurrentSession()
					.createCriteria(
							"org.fedoraproject.japi.checker.web.model.Difference")
					.add(create(instance)).list();
			return results;
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
