package com.dockdev.neural.testbase;

import static com.dockdev.neural.Main.logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;

import com.dockdev.neural.Input;
import com.dockdev.neural.Main;
import com.dockdev.neural.Network;
import com.dockdev.neural.Network.Neuron;

public class ImageTest implements Test {

	private Map<File, String[]> test = new HashMap<>();
	
	@Override
	public void test(Network network) throws IOException {
		for (Map.Entry<File, String[]> entry : test.entrySet()) {
			log("\nReading input file: " + entry.getKey());
			Input.image = ImageIO.read(entry.getKey());
			network.setInput(Input.read());
			
			Neuron[] wantedResult = new Neuron[Integer.valueOf(Main.metadata.get(2))];
			
			ArrayList<Neuron> temp = new ArrayList<>();
			for (String string : entry.getValue()) {
				temp.add(new Neuron(Integer.valueOf(string)));
			}
			wantedResult = temp.toArray(wantedResult);
			
			log("Randomizing weights");
			network.linkLayers();
			network.randomise();
		
			log("Computing result");
			Neuron[] output = network.compute();
			
			log("Computing cost\n");
			double cost = network.findCost(wantedResult);
			
			String result = "Result: ";
			for (Neuron neuron : output) {
				result = result + (neuron + ", ");
			}
			log(result);
			
			log("Cost: " + cost);
			System.out.println();
			
			log("Backpropogating");
		}
	}

	@Override
	public void read(File input) throws FileNotFoundException {
		Scanner testscn = new Scanner(input);
		for (int i = 0; testscn.hasNextLine(); i++) {
			String[] line = testscn.nextLine().split("-");
			try {
				File img = new File(line[0]);
				if (!img.exists()) throw new FileNotFoundException();
				test.put(img, line[1].split(","));
			} catch (FileNotFoundException e) {
				logger.warn("File path at line " + i + " could not be resolved. Skipping.");
			}
		}
		testscn.close();
	}

}
