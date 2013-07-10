package org.fedoraproject.japi.checker.web.model;

import java.io.File;
import java.util.Date;

import org.fedoraproject.japi.checker.web.utils.Utils;

public class ArtifactVersion {

    private Artifact artifact;
    private String version;
    private Date timestamp;
    private String packaging;
    private File file;

    public ArtifactVersion(Artifact artifact) {
        this.artifact = artifact;
    }

    /**
     * Return name of artifact version.
     * 
     * @return
     */
    public String getName() {
        return artifact.getArtifactId() + "-" + version;
    }

    /**
     * Return filename.
     * 
     * @return
     */
    public String getFileName() {
        return this.getName() + "." + packaging;
    }

    /**
     * Download the artifact.
     * 
     * @param destDir
     * @return artifact file
     */
    public File download(String destDir) {
        // com/jolira/guice/3.0.0/guice-3.0.0.jar
        String filePath = artifact.getGroupId().replace('.', '/') + "/"
                + artifact.getArtifactId() + "/" + version + "/"
                + artifact.getArtifactId() + "-" + version + "." + packaging;
        String url = "http://search.maven.org/remotecontent?filepath="
                + filePath;
        file = new File(destDir + "/" + this.getFileName());
        // System.out.println("Downloading from " + url);
        // download
        Utils.downloadFile(url, file);
        return file;
    }

    public Artifact getArtifact() {
        return artifact;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public File getFile() {
        return file;
    }
}
