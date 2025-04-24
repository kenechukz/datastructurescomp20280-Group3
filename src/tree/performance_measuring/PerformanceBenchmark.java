package tree.performance_measuring;

import interfaces.Entry;
import tree.AVLTreeMap;
import tree.Treap;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class PerformanceBenchmark {
    private static final int WARMUP_RUNS = 5;
    private static final int MEASUREMENT_RUNS = 10;
    private static final List<String[]> results = new ArrayList<>();
    private static final String[] HEADERS = {
            "Structure", "Size", "Pattern",
            "Insertion(ns)", "Search(ns)", "Delete(ns)", "Traversal(ns)"
    };

    static HashMap<Integer, Integer> generateData(String pattern, int size) {
        HashMap<Integer, Integer> data = new HashMap<>();
        List<Integer> keys = new ArrayList<>(size);

        switch (pattern) {
            case "random":
                // generate unique keys 0..size-1 randomly
                for (int i = 0; i < size; i++) keys.add(i);
                Collections.shuffle(keys);
                for (int k : keys) data.put(k, k);
                break;

            case "ascending":
                for (int i = 0; i < size; i++) {
                    data.put(i, i);
                }
                break;

            case "descending":
                for (int i = size-1; i >= 0; i--) {
                    data.put(i, i);
                }
                break;

            case "partial":
                int sortedCount = (int) (size * 0.7);
                // first sorted part
                for (int i = 0; i < sortedCount; i++) data.put(i, i);
                // then random unique keys for the rest
                for (int i = sortedCount; i < size; i++) keys.add(i);
                Collections.shuffle(keys);
                for (int k : keys) data.put(k, k);
                break;
        }
        return data;
    }

    static void warmup(tree.TreeMap<Integer, Integer> tree, HashMap<Integer, Integer> data) throws IOException {
        for (int i = 0; i < WARMUP_RUNS; i++) {
            tree.clear();
            for (java.util.Map.Entry<Integer, Integer> entry : data.entrySet()) {
                tree.put(entry.getKey(), entry.getValue());
            }
        }
    }

    static long measureInsertion(tree.TreeMap<Integer, Integer> tree, HashMap<Integer, Integer> data) throws IOException {
        long total = 0;
        for (int i = 0; i < MEASUREMENT_RUNS; i++) {
            tree.clear();
            long start = System.nanoTime();
            for (HashMap.Entry<Integer, Integer> entry : data.entrySet()) {
                tree.put(entry.getKey(), entry.getValue());
            }
            total += System.nanoTime() - start;
        }
        return total / MEASUREMENT_RUNS;
    }


    static long measureInsertion(TreeMap<Integer, Integer> tree, HashMap<Integer, Integer> data) {
        long total = 0;
        for (int i = 0; i < MEASUREMENT_RUNS; i++) {
            tree.clear();
            long start = System.nanoTime();
            tree.putAll(data);
            total += System.nanoTime() - start;
        }
        return total / MEASUREMENT_RUNS;
    }

    static long measureSearch(tree.TreeMap<Integer, Integer> tree, HashMap<Integer, Integer> data) throws IOException {
        long total = 0;
        Integer[] keys = data.keySet().toArray(new Integer[0]);
        for (int i = 0; i < MEASUREMENT_RUNS; i++) {
            tree.clear();                     // ← clear
            for (Map.Entry<Integer, Integer> e : data.entrySet())
                tree.put(e.getKey(), e.getValue());  // ← rebuild
            int key = keys[ThreadLocalRandom.current().nextInt(keys.length)];
            long start = System.nanoTime();
            tree.get(key);
            total += System.nanoTime() - start;
        }
        return total / MEASUREMENT_RUNS;
    }


    static long measureSearch(TreeMap<Integer, Integer> tree, HashMap<Integer, Integer> data) {
        tree.clear();
        tree.putAll(data);
        Integer[] keys = data.keySet().toArray(new Integer[0]);
        long total = 0;
        for (int i = 0; i < MEASUREMENT_RUNS; i++) {
            int key = keys[ThreadLocalRandom.current().nextInt(keys.length)];
            long start = System.nanoTime();
            tree.get(key);
            total += System.nanoTime() - start;
        }
        return total / MEASUREMENT_RUNS;
    }

    static long measureDeletion(tree.TreeMap<Integer, Integer> tree, HashMap<Integer, Integer> data) throws IOException {
        Integer[] keys = data.keySet().toArray(new Integer[0]);
        long total = 0;
        for (int i = 0; i < MEASUREMENT_RUNS; i++) {
            // Rebuild tree for each measurement
            tree.clear();
            for (java.util.Map.Entry<Integer, Integer> entry : data.entrySet()) {
                tree.put(entry.getKey(), entry.getValue());
            }

            int key = keys[ThreadLocalRandom.current().nextInt(keys.length)];
            long start = System.nanoTime();
            tree.remove(key);
            total += System.nanoTime() - start;
        }
        return total / MEASUREMENT_RUNS;
    }

    static long measureDeletion(TreeMap<Integer, Integer> tree, HashMap<Integer, Integer> data) {
        Integer[] keys = data.keySet().toArray(new Integer[0]);
        long total = 0;
        for (int i = 0; i < MEASUREMENT_RUNS; i++) {
            // Rebuild tree for each measurement
            tree.clear();
            tree.putAll(data);

            int key = keys[ThreadLocalRandom.current().nextInt(keys.length)];
            long start = System.nanoTime();
            tree.remove(key);
            total += System.nanoTime() - start;
        }
        return total / MEASUREMENT_RUNS;
    }

    static long measureInorderTraversal(tree.TreeMap<Integer, Integer> tree, HashMap<Integer, Integer> data) throws IOException {
        long total = 0;
        for (int i = 0; i < MEASUREMENT_RUNS; i++) {
            tree.clear();
            for (HashMap.Entry<Integer, Integer> entry : data.entrySet()) {
                tree.put(entry.getKey(), entry.getValue());
            }
            long start = System.nanoTime();
            if (tree instanceof Treap) {
                ((Treap<Integer, Integer>) tree).tree.inorder();
            } else if (tree instanceof AVLTreeMap) {
                ((AVLTreeMap<Integer, Integer>) tree).tree.inorder();
            }
            total += System.nanoTime() - start;
        }
        return total / MEASUREMENT_RUNS;
    }

    static long measureInorderTraversal(TreeMap<Integer, Integer> tree, HashMap<Integer, Integer> data) {
        long total = 0;
        for (int i = 0; i < MEASUREMENT_RUNS; i++) {
            tree.clear();
            tree.putAll(data);
            long start = System.nanoTime();
            tree.forEach((k,v) -> {});
            total += System.nanoTime() - start;
        }
        return total / MEASUREMENT_RUNS;
    }

    public static void main(String[] args) throws IOException {
        String[] patterns = {"random", "ascending", "descending", "partial"};
        int[] sizes = {100, 1000, 10000};

        for (String pattern : patterns) {
            for (int size : sizes) {
                HashMap<Integer, Integer> data = generateData(pattern, size);

                // Force GC between tests
                System.gc();
                try { Thread.sleep(100); } catch (InterruptedException e) {}

                // Test all structures
                testStructure(new Treap<>(), "Treap", data, pattern, size);
                testStructure(new AVLTreeMap<>(), "AVLTree", data, pattern, size);
                testStructure(new TreeMap<>(), data, pattern, size);
            }
        }

        // Write results to CSV
        try (FileWriter writer = new FileWriter("src\\tree\\performance_measuring\\csv\\benchmark_results.csv")) {
            // Write headers
            writer.write(String.join(",", HEADERS) + "\n");

            // Write data
            for (String[] row : results) {
                writer.write(String.join(",", row) + "\n");
            }
        }
    }

    static void testStructure(tree.TreeMap<Integer, Integer> tree, String name,
                              HashMap<Integer, Integer> data, String pattern, int size) throws IOException {
        System.out.printf("\nTesting %s (n=%d, %s)\n", name, size, pattern);

        // Warmup
        warmup(tree, data);


        // Tests
        long insertionTime = measureInsertion(tree, data);
        long searchTime = measureSearch(tree, data);
        long deleteTime = measureDeletion(tree, data);
        long traversalTime = measureInorderTraversal(tree, data);

        // Insertion
        System.out.println("Single Insertion: " + insertionTime + " ns");

        // Search (ensure tree is populated)
        
        for (java.util.Map.Entry<Integer, Integer> entry : data.entrySet()) {
            tree.put(entry.getKey(), entry.getValue());
        }
        System.out.println("Search (random key): " + searchTime + " ns");

        // Deletion
        System.out.println("Delete (random key): " + deleteTime + " ns");

        // Traversal
        System.out.println("Inorder Traversal: " + traversalTime + " ns");

        results.add(new String[]{
                name,
                String.valueOf(size),
                pattern,
                String.valueOf(insertionTime),
                String.valueOf(searchTime),
                String.valueOf(deleteTime),
                String.valueOf(traversalTime)
        });
    }

    static void testStructure(TreeMap<Integer, Integer> tree,
                              HashMap<Integer, Integer> data, String pattern, int size) throws IOException {
        System.out.printf("\nTesting %s (n=%d, %s)\n", "TreeMap", size, pattern);

        // Warmup
        for (int i = 0; i < WARMUP_RUNS; i++) {
            tree.clear();
            tree.putAll(data);
        }


        long insertionTime = measureInsertion(tree, data);
        long searchTime = measureSearch(tree, data);
        long deleteTime = measureDeletion(tree, data);
        long traversalTime = measureInorderTraversal(tree, data);

        // Insertion
        System.out.println("Single Insertion: " + insertionTime + " ns");

        // Search
        System.out.println("Search (random key): " + searchTime + " ns");

        // Deletion
        System.out.println("Delete (random key): " + deleteTime + " ns");

        // Traversal
        System.out.println("Inorder Traversal: " + traversalTime + " ns");

        results.add(new String[]{
                "TreeMap",
                String.valueOf(size),
                pattern,
                String.valueOf(insertionTime),
                String.valueOf(searchTime),
                String.valueOf(deleteTime),
                String.valueOf(traversalTime)
        });


    }
}