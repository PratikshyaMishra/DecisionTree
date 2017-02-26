package decisionTree.com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
/**
 * 
 * @author Pratikshya Mishra
 * This class provides two ArrayLists of data read from the input 
 * file AutenticSpliceSite and CrypticSpliceSite on which the decision
 * tree will be built
 *
 */
public class ReadFile {
	private ArrayList<String> data9Mer;
	/*
	 * The ctor reads files and creates  ArrayList
	 * for the authentic/cryptic splice site
	 */
	public ReadFile(String fileName) {
		super();
		data9Mer = new ArrayList<String>();
		String dataRead = null;
		File file = new File(fileName);
		BufferedReader br;
		try {
			/*read authentic splice site data file*/
			br = new BufferedReader(new FileReader(file));
			/*store authentic splice site data to ArrayList AuthenticData*/
			while((dataRead=br.readLine())!=null){
				data9Mer.add(dataRead.toUpperCase());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}	
	}
	/*
	 * Getters for the ArrayList
	 */
	public ArrayList<String> get9MerData() {
		return data9Mer;
	}
}
