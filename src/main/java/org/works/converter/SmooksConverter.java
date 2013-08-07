package org.works.converter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.milyn.SmooksException;

public interface SmooksConverter {
	/**
	 * converter multi incoming files(folder) with nameFilter
	 * 
	 * @param incoming
	 * @param outgoing
	 * @param nameFilter
	 * @param smooksConfig
	 * @throws SmooksException
	 */
	public void converter(File incoming, File outgoing, String targetFileExt,
			FilenameFilter nameFilter, File smooksConfig)
			throws SmooksException,IOException;

	/**
	 * converter single file
	 * 
	 * @param incoming
	 * @param outgoing
	 * @param smooksConfig
	 * @throws SmooksException
	 */
	public void converter(File incoming, File outgoing, File smooksConfig)
			throws SmooksException,IOException;
}
