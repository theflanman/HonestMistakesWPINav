package main.util.proxy;

import java.io.File;
import java.io.IOException;

import javax.imageio.*;

public class RealImage implements IProxyImage {

	private java.awt.Image image;
	
	protected RealImage(String path, String filename){
		loadIn(path, filename);
	}
	
	@Override
	public java.awt.Image getImage(String path){
		return image;
	}
	
	private void loadIn(String path, String fileName){
		try{
			image = ImageIO.read(new File(path + "/" + fileName));
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
}
