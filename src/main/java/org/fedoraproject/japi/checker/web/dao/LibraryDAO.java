package org.fedoraproject.japi.checker.web.dao;

import java.util.List;

import org.fedoraproject.japi.checker.web.model.Library;

public interface LibraryDAO {

	public void save(Library library);

	public List<Library> findAll();

	public Library findById(int id);
	
	public Library findWithReleasesById(int id);

	public List<Library> findByName(String name);

	public void delete(Library library);

}