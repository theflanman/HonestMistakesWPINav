package main.util;
import java.io.File;
import java.io.IOException;

import javax.imageio.*;

public class RealImage implements IProxyImage {

	private String filename;
	private java.awt.Image image;
	
	protected RealImage(String path, String filename){
		this.filename = filename;
		loadIn(path, filename);
	}
	
	@Override
	public java.awt.Image getImage(String path){
		return image;
	}
	
	private void loadIn(String path, String fileName){
		try{
			System.out.println("RealImage.loadIn: " + path + "/" + fileName);
			image = ImageIO.read(new File(Constants.IMAGES_PATH + "/" + path));//"/" + fileName));
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
}
