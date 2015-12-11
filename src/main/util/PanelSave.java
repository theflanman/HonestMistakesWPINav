package main.util;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class PanelSave {

	public void saveImage(JPanel panel, String name) {
		BufferedImage img = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
		panel.paint(img.getGraphics());
		try {
			ImageIO.write(img, "png", new File("src/data/pathimages/".concat(name.replace("jpg", "png"))));
			System.out.println("panel saved as image:".concat(name));

		} catch (Exception e) {
			System.out.println("panel not saved" + e.getMessage());
		}
	}
}

