package tree;

public class QuickSort {

    // Main quicksort method
    public static void quickSort(int[] data, int p, int r) {
        if (p < r) {
            int q = partition(data, p, r); // Partition the array
            quickSort(data, p, q - 1); // Sort the left subarray
            quickSort(data, q + 1, r); // Sort the right subarray
        }
    }

    // Partition method to rearrange elements around a pivot
    private static int partition(int[] data, int p, int r) {
        int pivot = data[r];
        int i = p - 1;
        for (int j = p; j < r; j++) {
            if (data[j] <= pivot) {
                i++;
                // Swap elements smaller than the pivot to the left
                int temp = data[i];
                data[i] = data[j];
                data[j] = temp;
            }
        }
        // Place the pivot in its correct position
        int temp = data[i+1];
        data[i+1] = data[r];
        data[r] = temp;
        return i + 1;
    }
}

