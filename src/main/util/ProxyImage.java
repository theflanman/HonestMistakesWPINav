package main.util;

public class ProxyImage implements ProxyImageInterface {

	private RealImage realImage;
	private String filename;
	
	public ProxyImage(String filename){
		this.filename = filename;
	}
	
	@Override
	public java.awt.Image getImage(){
		if (realImage == null){
			realImage = new RealImage(filename);
		}
		return realImage.getImage();
	}

}
