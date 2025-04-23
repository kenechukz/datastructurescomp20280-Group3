package tree.performance_measuring;

import interfaces.Entry;
import interfaces.Map;
import tree.AVLTreeMap;
import tree.Treap;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class PerformanceBenchmark {
    private static final int WARMUP_RUNS = 5;
    private static final int MEASUREMENT_RUNS = 10;

    static HashMap<Integer, Integer> generateData(String pattern, int size) {
        HashMap<Integer, Integer> data = new HashMap<>();
        Random rand = ThreadLocalRandom.current();

        switch (pattern) {
            case "random":
                for (int i = 0; i < size; i++) {
                    int randInt = rand.nextInt(10000);
                    data.put(randInt, randInt);
                }
                break;

            case "ascending":
                for (int i = 0; i < size; i++) {
                    data.put(i, i);
                }
                break;

            case "descending":
                for (int i = size; i > 0; i--) {
                    data.put(i, i);
                }
                break;

            case "partial":
                int sortedCount = (int)(size * 0.7);
                for (int i = 0; i < sortedCount; i++) {
                    data.put(i, i);
                }
                for (int i = sortedCount; i < size; i++) {
                    int randInt = rand.nextInt(10000);
                    data.put(randInt, randInt);
                }
                break;
        }
        return data;
    }

    static void warmup(Map<Integer, Integer> tree, HashMap<Integer, Integer> data) throws IOException {
        for (int i = 0; i < WARMUP_RUNS; i++) {
            
            for (java.util.Map.Entry<Integer, Integer> entry : data.entrySet()) {
                tree.put(entry.getKey(), entry.getValue());
            }
        }
    }

    static long measureInsertion(Map<Integer, Integer> tree, HashMap<Integer, Integer> data) throws IOException {
        long total = 0;
        for (int i = 0; i < MEASUREMENT_RUNS; i++) {
            
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
            
            long start = System.nanoTime();
            tree.putAll(data);
            total += System.nanoTime() - start;
        }
        return total / MEASUREMENT_RUNS;
    }

    static long measureSearch(Map<Integer, Integer> tree, HashMap<Integer, Integer> data) throws IOException {
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

    static long measureSearch(TreeMap<Integer, Integer> tree, HashMap<Integer, Integer> data) {
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

    static long measureDeletion(Map<Integer, Integer> tree, HashMap<Integer, Integer> data) throws IOException {
        Integer[] keys = data.keySet().toArray(new Integer[0]);
        long total = 0;
        for (int i = 0; i < MEASUREMENT_RUNS; i++) {
            // Rebuild tree for each measurement
            
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
            
            tree.putAll(data);

            int key = keys[ThreadLocalRandom.current().nextInt(keys.length)];
            long start = System.nanoTime();
            tree.remove(key);
            total += System.nanoTime() - start;
        }
        return total / MEASUREMENT_RUNS;
    }

    static long measureInorderTraversal(Map<Integer, Integer> tree, HashMap<Integer, Integer> data) throws IOException {
        long total = 0;
        for (int i = 0; i < MEASUREMENT_RUNS; i++) {
            
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
    }

    static void testStructure(Map<Integer, Integer> tree, String name,
                              HashMap<Integer, Integer> data, String pattern, int size) throws IOException {
        System.out.printf("\nTesting %s (n=%d, %s)\n", name, size, pattern);

        // Warmup
        warmup(tree, data);

        // Insertion
        System.out.println("Single Insertion: " + measureInsertion(tree, data) + " ns");

        // Search (ensure tree is populated)
        
        for (java.util.Map.Entry<Integer, Integer> entry : data.entrySet()) {
            tree.put(entry.getKey(), entry.getValue());
        }
        System.out.println("Search (random key): " + measureSearch(tree, data) + " ns");

        // Deletion
        System.out.println("Delete (random key): " + measureDeletion(tree, data) + " ns");

        // Traversal
        System.out.println("Inorder Traversal: " + measureInorderTraversal(tree, data) + " ns");
    }

    static void testStructure(TreeMap<Integer, Integer> tree,
                              HashMap<Integer, Integer> data, String pattern, int size) throws IOException {
        System.out.printf("\nTesting %s (n=%d, %s)\n", "TreeMap", size, pattern);

        // Warmup
        for (int i = 0; i < WARMUP_RUNS; i++) {
            
            tree.putAll(data);
        }

        // Insertion
        System.out.println("Single Insertion: " + measureInsertion(tree, data) + " ns");

        // Search
        System.out.println("Search (random key): " + measureSearch(tree, data) + " ns");

        // Deletion
        System.out.println("Delete (random key): " + measureDeletion(tree, data) + " ns");

        // Traversal
        System.out.println("Inorder Traversal: " + measureInorderTraversal(tree, data) + " ns");
    }
}