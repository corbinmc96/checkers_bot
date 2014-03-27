import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;


public class FileHandler {

	private String filePath;
	private ArrayList<String> fileContents = new ArrayList();
	private BufferedReader br;
	private BufferedWriter bw;

	public FileHandler(String path) {
		filePath = path;
		br = new BufferedReader(new FileReader(path));
		String line;
		while ((line = br.readLine()) != null) {
			if (line.substring(-1)=="/n") {
				fileContents.add(line.substring(0,line.length()-1));
			} else {
				fileContents.add(line);
			}
		}
		bw = new BufferedWriter(new FileWriter(path));
	}

	public String[] getAllLines() {
		String[] result = new String[fileContents.size()];
		result = fileContents.toArray(result);
		return result;
	}

	public void addLine(String line) {
		bw.write(line);
		bw.newLine();
	}

}