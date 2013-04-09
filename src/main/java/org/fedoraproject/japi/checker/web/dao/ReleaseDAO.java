package org.fedoraproject.japi.checker.web.dao;

import java.util.List;

import org.fedoraproject.japi.checker.web.model.Release;

public interface ReleaseDAO {

	public void save(Release release);

	public List<Release> findAll();

	public Release findById(int id);
	
	public Release findWithClassesById(int id);

	public List<Release> findByName(String name);

	public void delete(Release release);

}