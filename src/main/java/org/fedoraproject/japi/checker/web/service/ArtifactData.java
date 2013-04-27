package org.fedoraproject.japi.checker.web.service;

import java.util.Date;

public class ArtifactData {

    private String version;
    private Date timestamp;
    private String packaging;

    public ArtifactData() {
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

    @Override
    public String toString() {
        return version + " " + timestamp + " " + packaging;
    }
}