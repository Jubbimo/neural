package com.dockdev.neural;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;

public class Window {

	@SuppressWarnings("unused")
	public JFrame getController() {
		JFrame frame = new JFrame();
		frame.setLayout(new GridLayout());
		
		JSlider layers = new JSlider(0, 10, 2), neurons = new JSlider(1, 100, 16), output = new JSlider(1, 100, 10);
		
		JLabel layersLabel = new JLabel("Number of layers"), neuronsLabel = new JLabel("Neurons per layer"), outputLabel = new JLabel("Output neurons");
		
		
		return frame;
	}
}
