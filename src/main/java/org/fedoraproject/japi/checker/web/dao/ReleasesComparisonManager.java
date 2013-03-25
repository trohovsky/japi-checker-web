package org.fedoraproject.japi.checker.web.dao;

// Generated Feb 26, 2013 4:59:29 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;

import org.fedoraproject.japi.checker.web.model.ReleasesComparison;
import org.fedoraproject.japi.checker.web.persistence.HibernateUtil;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class ReleasesComparison.
 * 
 * @see org.fedoraproject.japi.checker.web.dao2.ReleasesComparison
 * @author Hibernate Tools
 */
public class ReleasesComparisonManager {

	private final SessionFactory sessionFactory = HibernateUtil
			.getSessionFactory();

	public void persist(ReleasesComparison transientInstance) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			session.persist(transientInstance);
			session.getTransaction().commit();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void save(ReleasesComparison instance) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			session.save(instance);
			session.getTransaction().commit();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void attachClean(ReleasesComparison instance) {
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void delete(ReleasesComparison persistentInstance) {
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public ReleasesComparison merge(ReleasesComparison detachedInstance) {
		try {
			ReleasesComparison result = (ReleasesComparison) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public ReleasesComparison findById(int id) {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			ReleasesComparison instance = (ReleasesComparison) session.get("org.fedoraproject.japi.checker.web.model.ReleasesComparison", id);
			session.getTransaction().commit();
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<ReleasesComparison> findByExample(ReleasesComparison instance) {
		try {
			List<ReleasesComparison> results = (List<ReleasesComparison>) sessionFactory
					.getCurrentSession()
					.createCriteria(
							"org.fedoraproject.japi.checker.web.model.ReleasesComparison")
					.add(create(instance)).list();
			return results;
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
