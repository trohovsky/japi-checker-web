package org.fedoraproject.japi.checker.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.fedoraproject.japi.checker.web.model.Artifact;

public class ArtifactsForm {

    private String artifactsText;

    public String getArtifactsText() {
        return artifactsText;
    }

    public void setArtifactsText(String artifactsText) {
        this.artifactsText = artifactsText;
    }

    public List<Artifact> getArtifacts() {
        List<Artifact> artifactList = new ArrayList<Artifact>();
        Scanner scanner = new Scanner(artifactsText);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            // process the line
            String words[] = line.split("\\s+");
            if (words.length >= 2) {
                Artifact artifact = new Artifact(words[0], words[1]);
                artifactList.add(artifact);
            }
        }
        scanner.close();
        return artifactList;
    }
}
