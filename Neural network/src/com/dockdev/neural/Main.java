package com.dockdev.neural;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import com.dockdev.neural.testbase.Test;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;

public class Main {

	private static Scanner console;
	public static Logger logger = new Logger();

	public static ArrayList<String> metadata = new ArrayList<>();
	
	@SuppressWarnings(
		"unchecked"
	)
	public static void main(String[] args) throws Exception {
		/*
		 * This file will contain the data telling the code what to do. 
		 * Line 1: An integer determining the number of layers
		 * Line 2: An integer determining the number of neurons per layer
		 * Line 3: An integer determining the number of output neurons
		 * Line 4: The classpath of the test class
		 * Line 5: The input file path
		 */
		File meta = new File("metadata.txt");
		
		//Reads the file and saves it to the metadata list
		Scanner metascanner = new Scanner(meta);
		while (metascanner.hasNextLine()) {
			metadata.add(metascanner.nextLine());
		} metascanner.close();
		
		//Creates the network
		Network network = new Network(Integer.valueOf(metadata.get(0)), Integer.valueOf(metadata.get(1))).setOutput(Integer.valueOf(metadata.get(2)));
		
		//Scans the input test file and tries to link it to a class
		ScanResult classpath = new FastClasspathScanner(metadata.get(3)).scan();
		Class<?> cls = classpath.classNameToClassRef(classpath.getNamesOfAllClasses().get(0));

		Class<? extends Test> testcls = null;
		try {
			Object instance = cls.newInstance();
			if (instance instanceof Test) {
				testcls = (Class<? extends Test>) cls;
			} else {
				throw new IllegalArgumentException("the given test file is not an instance of com.dockdev.neural.Test");
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException e) {
			logger.error(e.toString());
		}
		
		Test test = testcls.newInstance();
		try {
			//Runs test in the set class
			test.read(new File(metadata.get(4)));
			test.test(network);
		} catch (IOException e) {
			logger.error(e.toString());
		}
	}
	
	public static double random(double min, double d) {
		return ThreadLocalRandom.current().nextDouble(min, d + 1);
	}
	
	public static double sigmoid(double x) {
		return 1 / (1 + Math.exp(-x));
	}
}
