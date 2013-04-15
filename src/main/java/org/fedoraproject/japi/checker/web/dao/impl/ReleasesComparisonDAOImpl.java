package org.fedoraproject.japi.checker.web.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.fedoraproject.japi.checker.web.dao.ReleasesComparisonDAO;
import org.fedoraproject.japi.checker.web.model.ReleasesComparison;
import org.hibernate.FetchMode;
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
	public List<ReleasesComparison> findByReleasesIds(List<Integer> ids) {
        if (ids.size() > 1) {
            StringBuffer sql = new StringBuffer("from ReleasesComparison as c where");
            // comparison between first two releases
            sql.append(" c.referenceRelease.id = ");
            sql.append(ids.get(1));
            sql.append(" and c.newRelease.id =");
            sql.append(ids.get(0));
            // and the rest
            for (int i = 1; i < ids.size() - 1; i++) {
                sql.append(" or c.referenceRelease.id = ");
                sql.append(ids.get(i + 1));
                sql.append(" and c.newRelease.id = ");
                sql.append(ids.get(i));
            }
            sql.append(" order by c.newRelease.date desc");
            return sessionFactory.getCurrentSession().createQuery(sql.toString()).list();
        } else {
            return new ArrayList<ReleasesComparison>(0);
        }
	}

	public ReleasesComparison findByReleasesIds(int referenceId, int newId) {
        return (ReleasesComparison) sessionFactory.getCurrentSession()
                .createCriteria(ReleasesComparison.class)
                .setFetchMode("differences", FetchMode.JOIN)
                .add(Restrictions.eq("referenceRelease.id", referenceId))
                .add(Restrictions.eq("newRelease.id", newId)).uniqueResult();
	}

	public void delete(ReleasesComparison releasesComparison) {
		sessionFactory.getCurrentSession().delete(releasesComparison);
	}
}
