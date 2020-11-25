
import java.io.*;
import java.util.stream.Stream;

public class CSVReader  {

    public final int VEC_SIZE  = 785;
    private BufferedReader br = null;
    private int lineNum = 0;
    private boolean hasNextLine = true;
    private String nextLine;
    private String csvFile;
    private Stream<String> lines;

    public CSVReader (String csvFile) throws IOException {

        this.csvFile = csvFile;
        try {
            br = new BufferedReader(new FileReader(csvFile));
            lines = br.lines();
        }catch (FileNotFoundException e) {
            System.out.println("There was a problem with one of the files.");
        }
    }

    public long getNumLines() throws IOException {


        long num  = lines.count();
        br = new BufferedReader(new FileReader(csvFile));
        nextLine = br.readLine();

        return num;
    }

    public int[] getPixelNextVector() throws IOException {

        int[] pixelVector = new int[VEC_SIZE];
        if (nextLine != null) {
            String[] pixelVectorStr = nextLine.split(",");
            for (int i = 0; i < VEC_SIZE; i++) {
                pixelVector[i] = Integer.parseInt(pixelVectorStr[i]);
            }
            nextLine = br.readLine();
            if (nextLine == null)
                hasNextLine = false;
            return pixelVector;
        }
        else
        {
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