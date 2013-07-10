package org.fedoraproject.japi.checker.web.dao.impl;

import java.util.List;

import org.fedoraproject.japi.checker.web.dao.LibraryDAO;
import org.fedoraproject.japi.checker.web.model.Library;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class LibraryDAOImpl implements LibraryDAO {

    @Autowired
    private SessionFactory sessionFactory;

    // TODO rather use merge
    public void save(Library library) {
        sessionFactory.getCurrentSession().saveOrUpdate(library);
    }

    @SuppressWarnings("unchecked")
    public List<Library> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Library lib order by lib.name").list();
    }

    public Library findById(int id) {
        return (Library) sessionFactory.getCurrentSession().get(Library.class,id);
    }

    public Library findWithReleasesById(int id) {
        return (Library) sessionFactory.getCurrentSession()
                .createCriteria(Library.class)
                .setFetchMode("releases", FetchMode.JOIN)
                .add(Restrictions.idEq(id)).uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<Library> findByName(String name) {
        Query query = sessionFactory.getCurrentSession().createQuery(
                "from Library lib where lib.name like :name");
        query.setParameter("name", name);
        return query.list();
    }

    public void delete(Library library) {
        sessionFactory.getCurrentSession().delete(library);
    }
}
