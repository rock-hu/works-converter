package org.works.converter;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.milyn.SmooksException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

@ContextConfiguration(locations = { "classpath*:common/applicationContext-common-registry-locale.xml",
		"classpath*:converter/applicationContext-converter-registry.xml" })
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class DefaultSmooksConverterTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	SmooksConverter smooksConverter;

	String basePath = "E:/Workspace/works-converter/src/main/resources/converter/i18n/";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConverterFileFileStringFilenameFilterFile() {
		File incoming = new File(basePath + "incoming");
		File outgoing = new File(basePath + "outgoing");
		String targetFileExt = ".xml";

		File smooksConfig = new File(
				"E:/Workspace/works-converter/src/main/resources/converter/smooks/smooks-xml-labels-xml-config.xml");

		FilenameFilter nameFilter = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".xml");
			}
		};
		try {
			smooksConverter.converter(incoming, outgoing, targetFileExt, nameFilter, smooksConfig);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

}
