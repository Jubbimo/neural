package com.dockdev.neural;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;

import com.dockdev.neural.Network.Neuron;

public class Main {

	private static Scanner console;

	public static void main(String[] args) throws IOException {
		if (System.console() == null) {
//			throw new RuntimeException("Launch the application from the console");
		}
		console = new Scanner(System.in);
		
		System.out.println("Type a text file path:");
		File file = new File(console.nextLine());
		if (!(file.isFile() && file.exists())) {
			throw new FileNotFoundException("The file must be an existing file on your filesystem");
		}
		Map<File, String[]> ioass = new LinkedHashMap<>();
		
		Scanner test = new Scanner(file);
		for (int i = 0; test.hasNextLine(); i++) {
			String[] line = test.nextLine().split("-");
			try {
				File img = new File(line[0]);
				if (!img.exists()) throw new FileNotFoundException();
				ioass.put(img, line[1].split(","));
			} catch (FileNotFoundException e) {
				System.out.println("File path at line " + i + " could not be resolved. Skipping.");
			}
		}
		test.close();
		
		/*
		 * This file will contain the data telling the code what to do. 
		 * Line 1: An integer determining the number of layers
		 * Line 2: An integer determining the number of neurons per layer
		 * Line 3: An integer determining the number of output neurons
		 */
		File meta = new File("metadata.txt");
		
		int[] metadata = new int[3];
		Scanner metascanner = new Scanner(meta);
		
		metadata[0] = metascanner.nextInt();
		metadata[1] = metascanner.nextInt();
		metadata[2] = metascanner.nextInt();
		
		metascanner.close();
		
		//TODO: Change to iterative method
		Input.image = ImageIO.read(ioass.keySet().toArray(new File[0])[0]);
		
		Network network = new Network(metadata[0], metadata[1]).setOutput(metadata[2]);
		
		System.out.println("\nReading input");
		network.setInput(Input.read());
		System.out.println("Randomizing weights");
		network.linkLayers();
		network.randomise();
		
		System.out.println("Computing result");
		Neuron[] output = network.compute();
		
		for (Neuron neuron : output) {
			System.out.println(neuron);
		}
		
		return;
	}
	
	public static double random(double min, double d) {
		return ThreadLocalRandom.current().nextDouble(min, d + 1);
	}
	
	public static double sigmoid(double x) {
		return 1 / (1 + Math.exp(-x));
	}
}
