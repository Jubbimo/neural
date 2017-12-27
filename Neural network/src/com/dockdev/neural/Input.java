package com.dockdev.neural;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

import com.dockdev.neural.Network.Neuron;

public class Input {

	public static BufferedImage image;
	
	public static Neuron[] read() {
		LinkedList<Neuron> inputs = new LinkedList<>();
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				inputs.add(new Neuron(((image.getRGB(x, y) >> 16) & 0x000000FF)/255));
				inputs.add(new Neuron(((image.getRGB(x, y) >> 8) & 0x000000FF)/255));
				inputs.add(new Neuron(((image.getRGB(x, y)) & 0x000000FF)/255));
			}
		}
		return inputs.toArray(new Neuron[inputs.size()]);
	}
}
