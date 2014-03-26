import java.io.*;
import java.util.Arrays;

public class FileHandler {

	private String filePath;
	private ArrayList<String> fileContents = new ArrayList();
	private BufferedReader br;
	private BufferedWriter bw;

	public FileHandler(String path) {
		filePath = path;
		br = new BufferedReader(new FileReader(path));
		while ((String line = br.readLine()) != null) {
			if (line[-1]=='/n') {
				fileContents.append(line.substring(0,length(line)-1));
			} else {
				fileContents.append(line);
			}
		}
		bw = new BufferedWriter(new FileWriter(path));
	}

	public String[] getAllLines() {
		return fileContents;
	}

	public void addLine(String line) {
		bw.write(line);
		bw.newLine();
	}

}