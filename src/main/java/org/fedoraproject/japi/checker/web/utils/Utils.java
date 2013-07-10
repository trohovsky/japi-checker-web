package org.fedoraproject.japi.checker.web.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Utils {

    /**
     * Download the file from URL to specified the file.
     * 
     * @param url
     * @param filename
     */
    public static void downloadFile(String url, File file) {
        URL website;
        try {
            website = new URL(url);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(file);
            // 16 MB per file could be enough
            fos.getChannel().transferFrom(rbc, 0, 1 << 24);
            fos.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
