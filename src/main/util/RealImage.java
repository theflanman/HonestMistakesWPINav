package main.util;

import java.io.File;
import java.io.IOException;

import javax.imageio.*;

public class RealImage implements IProxyImage {

	private String filename;
	private java.awt.Image image;
	
	public RealImage(String filename){
		this.filename = filename;
		loadIn(filename);
	}
	
	@Override
	public java.awt.Image getImage(){
		return image;
	}
	
	public void loadIn(String filename){
		try{
			image = ImageIO.read(new File(Constants.IMAGES_PATH + "/" + filename));
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
	}

}
