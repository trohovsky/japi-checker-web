package org.fedoraproject.japi.checker.web.utils;

import java.util.Date;

import org.fedoraproject.japi.checker.web.model.Artifact;
import org.fedoraproject.japi.checker.web.model.ArtifactVersion;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ArtifactDataHandler extends DefaultHandler {

    private ArtifactVersion version;
    private char element;
    private Artifact artifact;

    public ArtifactDataHandler(Artifact artifact) {
        this.artifact = artifact;
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String content;
        switch (element) {
        case 'P':
            content = new String(ch, start, length);
            if (content.toString().equals("N/A") || content.toString().equals("bundle")) {
                version.setPackaging("jar");
            } else {
                version.setPackaging(content.toString());
            }
            break;
        case 'T':
            content = new String(ch, start, length);
            version.setTimestamp(new Date(Long.parseLong(content.toString())));
            break;
        case 'V':
            content = new String(ch, start, length);
            version.setVersion(content.toString());
        }
    }

    public void startElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException {
        if (localName.equals("doc")) {
            version = new ArtifactVersion(artifact);
        } else if (localName.equals("str")) {
            if (atts.getLength() > 0) {
                String name = atts.getValue(0);
                if (name.equals("p")) {
                    element = 'P';
                } else if (name.equals("v")) {
                    element = 'V';
                }
            }
        } else if (localName.equals("long")) {
            if (atts.getLength() > 0) {
                String name = atts.getValue(0);
                if (name.equals("timestamp")) {
                    element = 'T';
                }
            }
        }
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        element = '-';
        if (localName.equals("doc")) {
            artifact.addVersion(version);
        }
    }
}
