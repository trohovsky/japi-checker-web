package org.fedoraproject.japi.checker.web.model;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.fedoraproject.japi.checker.web.utils.ArtifactDataHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class Artifact {

    private String groupId;
    private String artifactId;
    private List<ArtifactVersion> versions = new ArrayList<ArtifactVersion>();

    public Artifact(String groupId, String artifactId) {
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    /**
     * Initialize artifact's versions.
     */
    public void initVersions() {

        String url = "http://search.maven.org/solrsearch/select?q=g:\""
                + groupId + "\"+AND+a:\"" + artifactId
                + "\"&core=gav&rows=1000&wt=xml";

        ArtifactDataHandler handler = new ArtifactDataHandler(this);

        XMLReader myReader;
        try {
            InputSource source = new InputSource(new URL(url).openStream());
            myReader = XMLReaderFactory.createXMLReader();
            myReader.setContentHandler(handler);
            myReader.parse(source);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public List<ArtifactVersion> getVersions() {
        return versions;
    }

    public void addVersion(ArtifactVersion version) {
        this.versions.add(version);
    }
}
