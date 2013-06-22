package org.fedoraproject.japi.checker.web.dao;

import java.util.List;

import org.fedoraproject.japi.checker.web.model.Release;

public interface ReleaseDAO {

    public void save(Release release);

    public List<Release> findByLibraryId(int libraryId);

    public Release findById(int id);

    public Release findWithClassesById(int id);

    public Release findPrevious(Release release);

    public Release findNext(Release release);

    public List<Release> findByName(String name);

    public void delete(Release release);

}