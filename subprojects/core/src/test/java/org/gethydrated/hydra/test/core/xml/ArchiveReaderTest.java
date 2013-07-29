package org.gethydrated.hydra.test.core.xml;

import org.gethydrated.hydra.api.service.deploy.ArchiveSpec.Builder;
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
    public void testSimpleArchive() throws IOException,
            ParserConfigurationException, SAXException {
        final URL url = this.getClass().getResource("/xml/testArchive1.xml");
        final ArchiveReader r = new ArchiveReader();
        final Builder ar = r.parse(new FileInputStream(url.getFile()));

        assertNotNull(ar);
        System.out.println(ar);
    }

}