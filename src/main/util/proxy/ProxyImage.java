package main.util.proxy;

public class ProxyImage implements IProxyImage {

	private RealImage realImage;
	private String filename;
	
	public ProxyImage(String filename){
		this.filename = filename;
	}
	
	@Override
	public java.awt.Image getImage(String path){
		if (realImage == null){
			realImage = new RealImage(path, filename);
		}
		
		return realImage.getImage(path);
	}
}
