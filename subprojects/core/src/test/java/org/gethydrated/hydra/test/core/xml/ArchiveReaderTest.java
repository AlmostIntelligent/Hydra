package org.gethydrated.hydra.test.core.xml;

import static junit.framework.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.gethydrated.hydra.core.internal.Archive;
import org.gethydrated.hydra.core.xml.ArchiveReader;
import org.junit.Test;
import org.xml.sax.SAXException;

public class ArchiveReaderTest {

    @Test
    public void testSimpleArchive() throws IOException,
            ParserConfigurationException, SAXException {
        final URL url = this.getClass().getResource("/xml/testArchive1.xml");
        final ArchiveReader r = new ArchiveReader();
        final Archive ar = r.parse(new FileInputStream(url.getFile()));

        assertNotNull(ar);
        System.out.println(ar);
    }

}