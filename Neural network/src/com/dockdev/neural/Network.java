package com.dockdev.neural;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class Network {
	
	public static class Perceptron {
		//I'm not sure if you would want this
	}

	public static class Neuron {

		private Map<Neuron, Double> weights = new LinkedHashMap<>();
		
		public double bias = 0;
		
		private volatile double value;

		private Random gaussian = new Random();
		
		public Map<Neuron, Double> getWeights() {return weights;}
		public double getValue() {return value;}
		public void setValue(double value) {this.value = value;}

		public Neuron() {
			this(0);
		}
		
		public Neuron(double d) {
			value = d;
		}
		
		public Neuron randomWeights(Layer input) {
			for (Neuron neuron : input.getContents()) {
				weights.put(neuron, gaussian.nextGaussian());
			}
			return this;
		}
		
		public Neuron setWeight(Neuron input, double bias) {
			weights.put(input, bias);
			return this;
		}
		
		public Neuron feed(Layer input) {
			double newValue = 0;
			for (Neuron neuron : input.contents) {
				double weight;
				if (weights.containsKey(neuron)) weight = weights.get(neuron);
				else {
					weight = gaussian.nextGaussian();
					weights.put(neuron, weight);
				}
				newValue+= weight * neuron.value;
			}
			newValue-= bias;
			value = Main.sigmoid(newValue / input.size());
			System.out.println(newValue/input.size() + " -> " + value);
			return this;
		}
		
		@Override
		public String toString() {
			return String.valueOf(value);
		}
	}

	public static class Layer {

		public Layer(int neurons) {
			contents = new Neuron[neurons];
			for (int s = 0; s < neurons; s++) {
				contents[s] = new Neuron();
			}
		}
		
		public double size() {
			return contents.length;
		}

		public Layer(Neuron[] neurons) {
			contents = neurons;
		}
		
		private Layer previous;
		private Layer next;

		public Layer getPrevious() {
			return previous;
		}
		public Layer getNext() {
			return next;
		}
		public Layer setPrevious(Layer previous) {
			this.previous = previous;
			return this;
		}
		public Layer setNext(Layer next) {
			this.next = next;
			return this;
		}
		
		private Neuron[] contents;
		
		public Neuron[] getContents() {
			return contents;
		}
		
		public Layer randomise() {
			for (Neuron neuron : contents) {
				neuron.randomWeights(previous);
			}
			return this;
		}
		
		public Layer update(Layer input) {
			for (Neuron neuron : contents) {
				neuron.feed(input);
			}
			return this;
		}
		
		public void updateForward() {
			update(previous);
			if (next == null) return;
			next.updateForward();
		}
	}

	private Neuron[] inputNeurons;
	private Neuron[] outputNeurons;
	
	private Layer inputLayer;
	private Layer outputLayer;
	
	public Layer[] layers;
	
	public Network(int layers, int neurons) {
		this.layers = new Layer[layers];
		for (int s = 0; s < layers; s++) {
			this.layers[s] = new Layer(neurons);
		}
	}
	
	public Network setInput(Neuron[] input) {
		inputNeurons = input;
		return this;
	}
	
	public Network setOutput(int output) {
		outputNeurons = new Neuron[10];
		for (int i = 0; i < output; i++) {
			outputNeurons[i] = new Neuron();
		}
		outputLayer = new Layer(outputNeurons);
		return this;
	}
	
	public Network linkLayers() {
		layers[0].setPrevious(inputLayer = new Layer(inputNeurons)).setNext(layers[1]);
		for (int i = 1; i < layers.length-1; i++) {
			layers[i].setPrevious(layers[i-1]).setNext(layers[i+1]);
		}
		layers[layers.length-1].setPrevious(layers[layers.length-2]).setNext(outputLayer);
		outputLayer.setPrevious(layers[layers.length-2]);
		return this;
	}
	
	public Network randomise() {
		for (Layer layer : layers) {
			layer.randomise();
		}
		outputLayer.randomise();
		return this;
	}
	
	public Neuron[] compute() {
		for (Layer layer : layers) {
			layer.update(layer.previous);
		}
		outputLayer.update(layers[layers.length-1]);
		return outputNeurons;
	}
}
