
import java.io.*;

public class CSVReader {

    private BufferedReader br = null;
    private boolean hasNextLine = true;
    private String nextLine;

    public CSVReader(String csvFile) throws IOException {
        try {
            br = new BufferedReader(new FileReader(csvFile));
            nextLine = br.readLine();
            //lines = br.lines();
        } catch (FileNotFoundException e) {
            System.out.println("There was a problem with one of the files.");
        }
    }

    public Picture getNextPicture() throws IOException {
        int label;
        int[] pixelVector = new int[Utils.VEC_SIZE];
        if (nextLine != null) {
            label = nextLine.charAt(0) - 48;
            int pixel = 0;
            int index = 0;
            for (int i = 2; i < nextLine.length(); i++) {
                if (nextLine.charAt(i) == ',') {
                    pixelVector[index++] = pixel;
                    pixel = 0;
                    continue;
                }
                pixel *= 10;
                pixel += (nextLine.charAt(i) - 48);
            }
            hasNextLine = (nextLine = br.readLine()) != null;
            return new Picture(label, pixelVector);
        } else {
            throw new RuntimeException("no more");
        }

    }

    public boolean hasNextLine() {
        return hasNextLine;
    }

    public void close() throws IOException {
        br.close();
    }
}