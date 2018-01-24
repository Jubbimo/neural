package com.dockdev.neural;

import java.util.ArrayList;
import java.util.Map;

import com.dockdev.neural.Network.*;

public class Backpropogator {

	public ArrayList<com.dockdev.neural.Backpropogator.BackpropItem.Weight> weights;
	public ArrayList<com.dockdev.neural.Backpropogator.BackpropItem.Bias> biases;
	
	private Network network;
	
	public Backpropogator(Network network) {
		this.network = network;
		weights = new ArrayList<>(network.weightSum());
		biases = new ArrayList<>(network.size());
	}
	
	public void backpropogate(Layer expected) {
		backpropogate(expected.getContents());
	}
	
	public void backpropogate(Neuron[] expected) {
		Layer output = network.getOutput();
		for (Neuron i : output) {
			i.getValue();
		}
	}
	
	private double derWeightToActivation(Neuron activation) {
		return derInputToActivation(activation.lastSigmoid, activation.getWeights().size())
				* derWeightToInput(activation.getValue());
	}
	private double derBiasToActivation(Neuron activation) {
		return derInputToActivation(activation.lastSigmoid, activation.getWeights().size());
	}
	private double derActivationToActivation(Neuron a1, Neuron a2) {
		return derInputToActivation(a1.lastSigmoid, a1.getWeights().size())
				* derActivationToInput(a1.getWeights().get(a2));
	}
	
		private double derCostToActivation(double activation, double expected) {
			return 2 * activation - expected;
		}
		private double derInputToActivation(double input, int inputs) {
			return Main.sigmoid(input) * (1 - Main.sigmoid(input)) * inputs;
		}
		private double derWeightToInput(double activation) {
			return activation;
		}
		private double derActivationToInput(double weight) {
			return weight;
		}

	@SuppressWarnings(
		"unused"
	)
	private double backpropActivation(Neuron activation) {
		Map<Neuron, Double> weights = activation.getWeights();
		double bias = activation.bias;
		return 0;
	}
	
	private void putSpecial(ArrayList<BackpropItem> list, BackpropItem item) {
		if (list.contains(item)) {
			int index = list.indexOf(item);
			list.remove(item);
			list.add(index, item);
		} else {
			list.add(item);
		}
	}
	
	private static abstract class BackpropItem {
		
		public static class Bias extends BackpropItem {

			private final double value;
			private Neuron of;
			
			public Bias(double value) {
				this.value = value;
			}
			
			@Override
			public double getValue() {
				return value;
			}

		}

		public abstract double getValue();
		
		public static class Weight extends BackpropItem {
			
			private final double value;
			
			private Neuron of;
			private Neuron from;
			
			public Weight(double value) {
				this.value = value;
			}
			
			public double getValue() {
				return value;
			}
		}
	}
}

