package tree;

public class MergeSort {
    static final int MaxValue = Integer.MAX_VALUE;

    public static void mergeSort(final int[] A) {
        if(A == null || A.length < 2){
            return;
        }
        mergeSortList(A,0,A.length -1);
    }

    private static void mergeSortList(int[] A, int p, int r) {
        if(p >= r){
            return;
        }
        int q = (p + r)/2;
        mergeSortList(A,p,q);
        mergeSortList(A,q+1,r);
        mergeList(A,p,q,r);
    }

    private static void mergeList(int[] A, int p, int q, int r) {
        int n1 = q - p + 1;
        int n2 = r - q;
        int[] left  = new int[n1];
        int[] right = new int[n2];

        for (int i = 0; i < n1; i++)  left[i] = A[p + i];
        for (int j = 0; j < n2; j++)  right[j] = A[q + 1 + j];

        int i = 0, j = 0, k = p;
        // merge until one side runs out
        while (i < n1 && j < n2) {
            if (left[i] <= right[j]) {
                A[k++] = left[i++];
            } else {
                A[k++] = right[j++];
            }
        }
        // copy any remaining
        while (i < n1) A[k++] = left[i++];
        while (j < n2) A[k++] = right[j++];
    }
}
