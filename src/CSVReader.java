
import java.io.*;
import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CSVReader {

    public final int VEC_SIZE = 785;
    private BufferedReader br = null;
    private int lineNum = 0;
    private boolean hasNextLine = true;
    private String nextLine;
    private String csvFile;
    private Stream<String> lines;

    public CSVReader(String csvFile) throws IOException {
        this.csvFile = csvFile;
        try {
            br = new BufferedReader(new FileReader(csvFile));
            nextLine = br.readLine();
            //lines = br.lines();
        } catch (FileNotFoundException e) {
            System.out.println("There was a problem with one of the files.");
        }
    }

/*    public long getNumLines() throws IOException {


        long num  = lines.count();
        br = new BufferedReader(new FileReader(csvFile));
        nextLine = br.readLine();

        return num;
    }*/

/*    public int[] getPixelNextVector() throws IOException {

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

    }*/

    public Picture getNextPicture() throws IOException {
        int label;
        int[] pixelVector = new int[VEC_SIZE];
        if (nextLine != null) {

            label = nextLine.charAt(0) - 48;
            int pixel = 0;
            int index = 0;
            for (int i = 2; i < VEC_SIZE; i++) {
                if(nextLine.charAt(i) == ',') {
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