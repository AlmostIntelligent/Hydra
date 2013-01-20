package org.gethydrated.hydra.test.core.xml;

import org.gethydrated.hydra.core.internal.Archive;
import org.gethydrated.hydra.core.xml.ArchiveReader;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import static junit.framework.Assert.assertNotNull;

public class ArchiveReaderTest {

    @Test
    public void testSimpleArchive() throws IOException, ParserConfigurationException, SAXException {
        URL url = this.getClass().getResource("/xml/testArchive1.xml");
        ArchiveReader r = new ArchiveReader();
        Archive ar = r.parse(new FileInputStream(url.getFile()));

        assertNotNull(ar);
        System.out.println(ar);
    }

}