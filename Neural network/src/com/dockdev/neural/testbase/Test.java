package com.dockdev.neural.testbase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.dockdev.neural.Main;
import com.dockdev.neural.Network;

public interface Test {

	/**
	 * Logs the supplied text to the console
	 * @param text the text to log to the console
	 */
	default void log(String text) {
		Main.logger.debug(text);
	}
	
	public void test(Network network) throws IOException;
	public void read(File input) throws FileNotFoundException;
}
