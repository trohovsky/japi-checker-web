package org.fedoraproject.japi.checker.web.dao.impl;

import java.util.List;

import org.fedoraproject.japi.checker.web.dao.ReleaseDAO;
import org.fedoraproject.japi.checker.web.model.Release;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ReleaseDAOImpl implements ReleaseDAO {

	@Autowired
	private SessionFactory sessionFactory;

	// TODO TODO rather use merge
	public void save(Release release) {
		sessionFactory.getCurrentSession().saveOrUpdate(release);
	}
	
	@SuppressWarnings("unchecked")
	public List<Release> findAll() {
		return sessionFactory.getCurrentSession().createQuery("from Release rel order by rel.name").list();
	}

	public Release findById(int id) {
		return (Release) sessionFactory.getCurrentSession().get(Release.class, id);
	}
	
	public Release findWithClassesById(int id) {
		return (Release) sessionFactory.getCurrentSession()
				.createCriteria(Release.class)
				.setFetchMode("classes", FetchMode.JOIN)
				.add(Restrictions.idEq(id)).uniqueResult(); 
	}

    public Release findPrevious(Release release) {
	    Release previousRelease = (Release) sessionFactory.getCurrentSession()
	            .createCriteria(Release.class)
	            .add(Restrictions.eq("library", release.getLibrary()))
	            .add(Restrictions.lt("date", release.getDate()))
	            .addOrder(Order.desc("date")).setMaxResults(1).uniqueResult();
	    if (previousRelease != null) {
	        return findWithClassesById(previousRelease.getId());
	    }
	    return previousRelease;
    }

    public Release findNext(Release release) {
        Release nextRelease = (Release) sessionFactory.getCurrentSession()
                .createCriteria(Release.class)
                .add(Restrictions.eq("library", release.getLibrary()))
                .add(Restrictions.gt("date", release.getDate()))
                .addOrder(Order.asc("date")).setMaxResults(1).uniqueResult();
        if (nextRelease != null) {
            return findWithClassesById(nextRelease.getId());
        }
        return nextRelease;
    }

	@SuppressWarnings("unchecked")
	public List<Release> findByName(String name) {
		Query query = sessionFactory.getCurrentSession().createQuery("from Release rel where rel.name like :name");
		query.setParameter("name", name);
		return query.list();
	}
	
	public void delete(Release release) {
		sessionFactory.getCurrentSession().delete(release);
	}

}
