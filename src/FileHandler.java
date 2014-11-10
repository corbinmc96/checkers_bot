// ALL CORBIN

import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;


public class FileHandler {

	private String filePath;
	private ArrayList<String> fileContents = new ArrayList<String>();
	private BufferedReader br;
	private BufferedWriter bw;

	public FileHandler(String path) { 
		try{
			filePath = path;
			br = new BufferedReader(new FileReader(path));
			String line;
			while ((line = br.readLine()) != null) {
				fileContents.add(line);
			}
			br.close();
		} catch (FileNotFoundException ex1) {
			System.out.println(ex1.toString());
			System.out.print("no file");
		} catch (IOException ex2) {
			System.out.println(ex2.toString());
		}
	}

	public String[] getAllLines() {
		String[] result = new String[fileContents.size()];
		result = fileContents.toArray(result);
		return result;
	}

	public void addLine(String line) {
		try {
			bw = new BufferedWriter(new FileWriter(this.filePath, true));
			bw.write(line);
			bw.newLine();
			bw.close();
		} catch (IOException ex) {
			System.out.println(ex.toString());
		}
	}

	public boolean containsLine(String s) {
		for (String line : fileContents) {
			if (line == s) {
				return true;
			}
		}
		return false;
	}
}
