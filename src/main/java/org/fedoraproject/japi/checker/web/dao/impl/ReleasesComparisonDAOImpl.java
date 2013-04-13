package org.fedoraproject.japi.checker.web.dao.impl;

import java.util.List;

import org.fedoraproject.japi.checker.web.dao.ReleasesComparisonDAO;
import org.fedoraproject.japi.checker.web.model.ReleasesComparison;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ReleasesComparisonDAOImpl implements ReleasesComparisonDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public void save(ReleasesComparison releasesComparison) {
		sessionFactory.getCurrentSession().persist(releasesComparison);
	}
	
	@SuppressWarnings("unchecked")
	public List<ReleasesComparison> findAll() {
		return sessionFactory.getCurrentSession().createQuery("from ReleasesComparison comp").list();
	}

	public ReleasesComparison findByReleasesIds(int referenceId, int newId) {
        return (ReleasesComparison) sessionFactory.getCurrentSession()
                .createCriteria(ReleasesComparison.class)
                .add(Restrictions.eq("referenceRelease.id", referenceId))
                .add(Restrictions.eq("newRelease.id", newId)).uniqueResult();
	}

	public void delete(ReleasesComparison releasesComparison) {
		sessionFactory.getCurrentSession().delete(releasesComparison);
	}
}
