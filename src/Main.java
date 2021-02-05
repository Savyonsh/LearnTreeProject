import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Program program = new Program();
        try {
            program.run(args[3], args[4], Double.parseDouble(args[1]), Integer.parseInt(args[2]));
        }catch (Exception e) {
            System.out.println("Error: Exception caught.\nMessage: " + e.getMessage());
        }
    }
}
