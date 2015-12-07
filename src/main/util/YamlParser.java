package main.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.Yaml;

/** This class is capable of getting any # of arguments from any # of files.
 * 
 * @author NathanGeorge
 */
public class YamlParser {	
	private ArrayList <Map<String,Double>> list;
	private FileInputStream fis;
	private HashMap<String, Double> argList;

	@SuppressWarnings("unchecked")
	public YamlParser(String[] files) {

		list = new ArrayList <Map<String,Double>>();
		this.argList = new HashMap<String, Double>();

		// for each input file
		for (String path : files){

			// read the file and parse it
			this.fis = null;
			try {
				fis = new FileInputStream(path);

				// parse the yml file
				Yaml yaml = new Yaml();
				Map<String, Double> obj = (Map<String, Double>) yaml.load(fis);
				this.list.add(obj);

				this.argList = this.getAllArguments();

				fis.close();
			} catch (FileNotFoundException e){
				// TODO: make this log.info
				System.out.println("\nEither no configuration file was given or it could not "
						+ "be found...using default values\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// to see what's in the yml
	public void printAll(){
		// for each pair in the list
		for(Map<String,Double> row : list){
			
			Set<String> keys = row.keySet();
			
			for(String key : keys){
				System.out.println("Key: " + key); 
				System.out.println("Value: " + row.get(key) + "\n");
			}

		}

	}

	// sets the argList member
	private HashMap<String, Double> getAllArguments(){
		HashMap<String, Double> arguments = new HashMap<String, Double>();

		for(Map<String, Double> row : list){
			
			Set<String> keys = row.keySet();

			for(String key : keys){
				arguments.put(key, row.get(key));
			}
		}

		return arguments;
	}

	public HashMap<String, Double> getArgList() {
		return argList;
	}

	public void setArgList(HashMap<String, Double> argList) {
		this.argList = argList;
	}


	
}
