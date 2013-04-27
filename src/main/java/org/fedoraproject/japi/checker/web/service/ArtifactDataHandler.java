package org.fedoraproject.japi.checker.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ArtifactDataHandler extends DefaultHandler {

    private ArtifactData data;
    private char element;
    private List<ArtifactData> artifactDataList = new ArrayList<ArtifactData>();

    List<ArtifactData> getArtifactDataList() {
        return this.artifactDataList;
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String content;
        switch (element) {
        case 'P':
            content = new String(ch, start, length);
            if (content.toString().equals("N/A")) {
                data.setPackaging("jar");
            } else {
                data.setPackaging(content.toString());
            }
            break;
        case 'T':
            content = new String(ch, start, length);
            data.setTimestamp(new Date(Long.parseLong(content.toString())));
            break;
        case 'V':
            content = new String(ch, start, length);
            data.setVersion(content.toString());
        }
    }

    public void startElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException {
        if (localName.equals("doc")) {
            data = new ArtifactData();
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
            artifactDataList.add(data);
        }
    }
}
