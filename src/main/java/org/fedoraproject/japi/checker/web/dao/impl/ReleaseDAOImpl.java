package org.fedoraproject.japi.checker.web.dao.impl;

import java.util.List;

import org.fedoraproject.japi.checker.web.dao.ReleaseDAO;
import org.fedoraproject.japi.checker.web.model.Class;
import org.fedoraproject.japi.checker.web.model.Release;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
		sessionFactory.getCurrentSession().persist(release);
	}
	
	@SuppressWarnings("unchecked")
	public List<Release> findAll() {
		return sessionFactory.getCurrentSession().createQuery("from Release rel order by rel.name").list();
	}

	public Release findById(int id) {
		return (Release) sessionFactory.getCurrentSession().get(Release.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Release> findByName(String name) {
		Query query = sessionFactory.getCurrentSession().createQuery("from Release rel where rel.name like :name");
		query.setParameter("name", name);
		return query.list();
	}

	public Class findClassByName(int id, String name) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery("select * from class where release_id = :releaseId and name = :name").addEntity(Class.class);
		query.setParameter("releaseId", id);
		query.setParameter("name", name);
		
		Class clazz = (Class) query.uniqueResult();
				
		return clazz;
	}
	
	public void delete(Release release) {
		sessionFactory.getCurrentSession().delete(release);
	}

}
