package tree.performance_measuring;

import priorityqueue.PQSort;
import tree.MergeSort;
import tree.QuickSort;
import tree.TreapSort;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

public class SortingBenchmark {
    public static void main(String[] args) throws IOException {
        long start, end;

        Path dir = Paths.get("src/tree/performance_measuring/CSVs/");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path path : stream) {
                if (!Files.isRegularFile(path)) {
                    throw new FileNotFoundException(path.toString());
                }

                File file = path.toFile();
                Integer[] arr = read(file);

                start = System.nanoTime();
                // ---- Change for each algo -----
                System.out.print("Collections.sort on " + file.getName() + ": ");
                Collections.sort(Arrays.asList(arr));
                // -------------------------------
                end = System.nanoTime();
                System.out.println((end - start) / 1000 + "us");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Integer[] read(File file) throws IOException {
        Integer[] result;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line = bufferedReader.readLine();

            if (line != null && !line.trim().isEmpty()) {
                result = Arrays.stream(line.split(","))
                        .map(Integer::parseInt)
                        .toArray(Integer[]::new); // Split on commas and convert to int
                return result;
            } else {
                System.out.println("CSV file is empty.");
                return new Integer[0]; // Return an empty array
            }
        } catch (IOException ex) {
            System.err.println("Error reading file: " + ex.getMessage());
            throw ex;
        }
    }
}
