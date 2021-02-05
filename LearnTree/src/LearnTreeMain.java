import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class LearnTreeMain {

    public static void main(String[] args) {
        if(!checkArgs(args)) return;
        Program program = new Program();
        try {
            program.run(args[3], args[4], Double.parseDouble(args[1]), Integer.parseInt(args[2]));
        }catch (Exception e) {
            System.out.println("Error: Exception caught.\nMessage: " + e.getMessage());
        }
    }

    public static boolean checkArgs(String[] args) {
        if(args.length < 5) {
            System.out.println("Error: missing an argument.\nUsage: <version> <L> <percentage> <trainingFile> <outputFile>\nExiting...");
            return false;
        }
        return true;
    }
}
