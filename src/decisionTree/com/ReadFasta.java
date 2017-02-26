package decisionTree.com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadFasta {
	public static void main(String[] args){
		String dataRead = null;
		File file = new File("gene.txt");
		BufferedReader br;
		try {
			/*read authentic splice site data file*/
			br = new BufferedReader(new FileReader(file));
			/*store authentic splice site data to ArrayList AuthenticData*/
			while((dataRead=br.readLine())!=null){
				
			}
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}
