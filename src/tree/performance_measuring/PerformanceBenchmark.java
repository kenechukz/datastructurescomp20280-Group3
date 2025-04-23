package tree.performance_measuring;


import interfaces.Map;

import tree.AVLTreeMap;
import tree.Treap;

import java.io.IOException;
import java.util.*;





public class PerformanceBenchmark {
    // 1. Data Generation
    static HashMap<Integer, Integer> generateData(String pattern, int size) {
        HashMap<Integer, Integer> data = new HashMap<>();
        switch (pattern) {
            case "random":
                for (int i = 0; i < size; i++) {
                    int randInt = (int) (Math.random() * 10000);
                    data.put( randInt, randInt);
                }

            case "ascending":
                // Fill with sorted keys (1..n)
                for (int i = 0; i < size; i++) {

                    data.put( i, i);

                }
                break;
            case "descending":
                for (int i = size; i > 0; i--) {

                    data.put( i, i);

                }
                break;
            case "partial":
                // 70% sorted, 30% random
                int lastIndex = 0;
                for (int i = 0; i < (int) (size * 0.7); i++) {
                    data.put( i, i);
                    lastIndex = i;
                }
                while(lastIndex < size) {
                    int randInt = (int) (Math.random() * 10000);
                    data.put( randInt, randInt);
                    lastIndex++;
                }
                break;
        }
        return data;
    }

    // 2. Benchmark Methods
    static long measureInsertion(Map<Integer, Integer> tree, HashMap<Integer, Integer> data) throws IOException {
        long start = System.nanoTime();
        // single-insertion
        for (HashMap.Entry<Integer, Integer> entry : data.entrySet()) {
            tree.put(entry.getKey(), entry.getValue());
        }
        return System.nanoTime() - start;
    }

    static long measureInsertion(TreeMap<Integer, Integer> tree, HashMap<Integer, Integer> data) {
        long start = System.nanoTime();
        // single-insertion
        for (HashMap.Entry<Integer, Integer> entry : data.entrySet()) {
            tree.put(entry.getKey(), entry.getValue());
        }
        return System.nanoTime() - start;
    }

    static long measureSearch(Map<Integer, Integer> tree, HashMap<Integer, Integer> data) throws IOException {
        // First insert all elements
        for (HashMap.Entry<Integer, Integer> entry : data.entrySet()) {
            tree.put(entry.getKey(), entry.getValue());
        }
        long start = System.nanoTime();
        int key = (int) (Math.random() * data.size());
        tree.get(key);
        return System.nanoTime() - start;
    }

    static long measureSearch(TreeMap<Integer, Integer> tree, HashMap<Integer, Integer> data) {
        // First insert all elements
        for (HashMap.Entry<Integer, Integer> entry : data.entrySet()) {
            tree.put(entry.getKey(), entry.getValue());
        }
        long start = System.nanoTime();
        int key = (int) (Math.random() * data.size());
        tree.get(key);
        return System.nanoTime() - start;
    }

    // ... (Similar methods for deletion/traversal)
    static long measureDeletion(Map<Integer, Integer> tree, HashMap<Integer, Integer> data) throws IOException {
        for (HashMap.Entry<Integer, Integer> entry : data.entrySet()) {
            tree.put(entry.getKey(), entry.getValue());
        }
        ArrayList<Integer> keys = new ArrayList<>(data.keySet());
        int index = new Random().nextInt(keys.size());
        int key = keys.get(index);

        long start = System.nanoTime();
        tree.remove(key);
        return System.nanoTime() - start;
    }

    static long measureDeletion(TreeMap<Integer, Integer> tree, HashMap<Integer, Integer> data) {
        for (HashMap.Entry<Integer, Integer> entry : data.entrySet()) {
            tree.put(entry.getKey(), entry.getValue());
        }
        ArrayList<Integer> keys = new ArrayList<>(data.keySet());
        int index = new Random().nextInt(keys.size());
        int key = keys.get(index);

        long start = System.nanoTime();
        tree.remove(key);
        return System.nanoTime() - start;
    }

    static long measureInorderTraversal(Map<Integer, Integer> tree, HashMap<Integer, Integer> data) throws IOException {
        for (HashMap.Entry<Integer, Integer> entry : data.entrySet()) {
            tree.put(entry.getKey(), entry.getValue());
        }

        long start = -1;
        if (tree instanceof Treap) {
             start = System.nanoTime();
            ((Treap<Integer, Integer>) tree).tree.inorder();
        } else if (tree instanceof AVLTreeMap) {
            start = System.nanoTime();
            ((AVLTreeMap<Integer, Integer>) tree).tree.inorder();
        }

        return System.nanoTime() - start;
    }

    static long measureInorderTraversal(TreeMap<Integer, Integer> tree, HashMap<Integer, Integer> data) {
        for (HashMap.Entry<Integer, Integer> entry : data.entrySet()) {
            tree.put(entry.getKey(), entry.getValue());
        }
        long start = System.nanoTime();
        tree.forEach((k, v) -> {});

        return System.nanoTime() - start;
    }



    // 3. Main Benchmark Runner
    public static void main(String[] args) throws IOException {
        String[] patterns = {"random", "ascending", "descending", "partial"};
        int[] sizes = {100, 1000, 10000};

        for (String pattern : patterns) {
            for (int size : sizes) {
                HashMap<Integer, Integer> data = generateData(pattern, size);

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
        System.out.println("Single Insertion: " + measureInsertion(tree, data) + " ns");
        System.out.println("Search (random key): " + measureSearch(tree, data) + " ns");
        System.out.println("Delete (random key): " + measureDeletion(tree, data) + " ns");
        System.out.println("Delete (random key): " + measureInorderTraversal(tree, data) + " ns");
        // ... (Other operations)
    }
    
    
    static void testStructure(TreeMap<Integer, Integer> tree,
                              HashMap<Integer, Integer> data, String pattern, int size) throws IOException {
        System.out.printf("\nTesting %s (n=%d, %s)\n", "TreeMap", size, pattern);
        System.out.println("Single Insertion: " + measureInsertion(tree, data) + " ns");
        System.out.println("Search (random key): " + measureSearch(tree, data) + " ns");
        System.out.println("Delete (random key): " + measureDeletion(tree, data) + " ns");
        System.out.println("Delete (random key): " + measureInorderTraversal(tree, data) + " ns");
        // ... (Other operations)
    }

}








