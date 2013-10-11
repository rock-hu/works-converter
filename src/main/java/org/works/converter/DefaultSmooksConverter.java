package org.works.converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.milyn.Smooks;
import org.milyn.SmooksException;
import org.milyn.container.ExecutionContext;
import org.milyn.payload.ByteResult;
import org.xml.sax.SAXException;

public class DefaultSmooksConverter implements SmooksConverter {

	private static final Logger logger = Logger.getLogger(DefaultSmooksConverter.class);

	private static final int BUFFER_SIZE = 1024;

	@Override
	public void converter(File incoming, File outgoing, String targetFileExt, FilenameFilter nameFilter,
			File smooksConfig) throws SmooksException, IOException {

		if (incoming == null || outgoing == null || smooksConfig == null) {
			throw new SmooksException("incoming folder,outgoing folder or smooks config file can,t be null.");
		}

		if (!incoming.exists() || !incoming.isDirectory()) {
			throw new SmooksException("incoming folder must exist and only support folder.");
		}
		if (!outgoing.exists() || !outgoing.isDirectory()) {
			throw new SmooksException("outgoing folder must exist and only support folder.");
		}

		if (!smooksConfig.exists() || !smooksConfig.isFile()) {
			throw new SmooksException("smooks config file must exist.");
		}

		String[] baseFilenames = incoming.list(nameFilter);

		String incomgBasePath = incoming.getAbsolutePath();
		String outgoingBasePath = outgoing.getAbsolutePath();

		for (String filename : baseFilenames) {
			// FIXME:
			File source = new File(incomgBasePath + File.separator + filename);

			File target = new File(outgoingBasePath + File.separator + filename.substring(0, filename.lastIndexOf("."))
					+ targetFileExt);
			if (!target.exists()) {
				target.createNewFile();
			}
			converter(source, target, smooksConfig);
		}

	}

	@Override
	public void converter(File incoming, File outgoing, File smooksConfig) throws SmooksException, IOException {

		if (incoming == null || outgoing == null || smooksConfig == null) {
			throw new SmooksException("incoming file,outgoing file or smooks config file can,t be null.");
		}

		if (!incoming.exists() || !incoming.isFile()) {
			throw new SmooksException("incoming file must exist and only support single file.");
		}
		if (!outgoing.exists() || !outgoing.isFile()) {
			throw new SmooksException("outgoing file must exist and only support single file.");
		}

		if (!smooksConfig.exists() || !smooksConfig.isFile()) {
			throw new SmooksException("smooks config file must exist.");
		}

		Smooks smooks = null;
		InputStream is = null;
		ByteResult outputByte = null;
		ExecutionContext executionContext = null;
		FileChannel fc = null;
		ByteBuffer bb = null;
		try {
			outputByte = new ByteResult();
			is = new FileInputStream(incoming);
			smooks = new Smooks(new FileInputStream(smooksConfig));
			executionContext = smooks.createExecutionContext();

			smooks.filterSource(executionContext, new StreamSource(new InputStreamReader(is)), outputByte);

			fc = new FileOutputStream(outgoing).getChannel();
			bb = ByteBuffer.allocate(outputByte.getResult().length);
			bb.put(outputByte.getResult());
			bb.flip();
			fc.write(bb);
			fc.close();
			is.close();
			smooks.close();
		} catch (IOException e) {
			throw e;
		} catch (SAXException e) {
			throw new SmooksException(e.getMessage(), e);
		} finally {
			if (fc != null && fc.isOpen()) {
				fc.close();
			}
		}

	}
}
