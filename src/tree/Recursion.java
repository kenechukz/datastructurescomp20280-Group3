package tree;

public class Recursion {

    public class FibonacciRecursive {
        public static long fibonacci(int n) {
            if (n <= 1) {
                return n; // Base cases: Fibonacci(0)=0, Fibonacci(1)=1
            }
            return fibonacci(n - 1) + fibonacci(n - 2);
        }

        public static long tribonacci(int n) {
            if (n == 0) {
                return 0;
            }
            if (n == 1) {
                return 0;
            }
            if (n == 2) {
                return 1;
            }
            return tribonacci(n - 1) + tribonacci(n - 2) + tribonacci(n - 3);
        }

    }


        public static int M(int n) {
            if (n > 100) {
                return n - 10;
            }
            return M(M(n + 11));
        }
    public static void main(String[] args) {
        int n = 5;
        System.out.println("Fibonacci(" + n + ") = " + FibonacciRecursive.fibonacci(n));
        System.out.println("tribonacci(9) = " + FibonacciRecursive.tribonacci(9));

        int input = 87;
        int result = M(input);
        System.out.println("M(" + input + ") = " + result);
    }
}
