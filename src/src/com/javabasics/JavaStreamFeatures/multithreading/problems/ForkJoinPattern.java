package com.javabasics.JavaStreamFeatures.multithreading.problems;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;


/*
9. Fork/Join Pattern
Description: Implement the fork/join pattern where a task is divided into smaller sub-tasks that can be executed concurrently.
After the tasks are completed, their results are combined.
Key Concepts: Divide-and-conquer, parallel execution, recursion.
Extension: Implement a task scheduler to optimize load balancing.
 */

public class ForkJoinPattern {

    public static void main(String[] args) {
        int[] arr = new int[100];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i + 1;
        }

        ForkJoinPool pool = new ForkJoinPool();
        Integer result = pool.invoke(new InnerForkJoinPattern(0, arr.length, arr));
        System.out.println("Blocked the thread, Results : "+result);
    }

    static class InnerForkJoinPattern extends RecursiveTask<Integer> {

        int start, end;
        int[] arr;

        InnerForkJoinPattern(int start, int end, int[] arr) {
            this.start = start;
            this.end = end;
            this.arr = arr;
        }

        @Override
        protected Integer compute() {
            int sum = 0;
            if (end - start > 20) {
                int mid = (end + start) / 2;
                InnerForkJoinPattern left = new InnerForkJoinPattern(start, mid, arr);
                InnerForkJoinPattern right = new InnerForkJoinPattern(mid, end, arr);
                right.fork();
                left.fork();
                Integer leftData = left.join();
                Integer rightData = right.join();
                return leftData + rightData;
            } else {
                for (int i = start; i < end; i++) {
                    sum += arr[i];
                }
                return sum;
            }
        }
    }

}


//ChatGPT answer
class ForkJoinSumExample {

    // Threshold for the size of the array that should no longer be divided further
    private static final int THRESHOLD = 10;

    public static void main(String[] args) {
        // Example array to sum
        int[] array = new int[100];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1;  // Assign values 1, 2, 3, ..., 100
        }

        // Create a ForkJoinPool to execute the tasks
        ForkJoinPool pool = new ForkJoinPool();

        // Create and invoke the task to sum the array
        SumTask task = new SumTask(array, 0, array.length);
        Integer result = pool.invoke(task);

        // Output the result
        System.out.println("The sum is: " + result);
    }

    // Recursive task to calculate the sum of an array segment
    static class SumTask extends RecursiveTask<Integer> {
        private final int[] array;
        private final int start;
        private final int end;

        public SumTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            System.out.println("Computing result for "+start+" "+end);
            // If the range is small enough, compute the sum directly
            if (end - start <= THRESHOLD) {
                int sum = 0;
                for (int i = start; i < end; i++) {
                    sum += array[i];
                }
                return sum;
            } else {
                // Otherwise, split the task into two smaller tasks
                int middle = (start + end) / 2;
                SumTask leftTask = new SumTask(array, start, middle);
                SumTask rightTask = new SumTask(array, middle, end);

                // Fork the tasks
                leftTask.fork();
                rightTask.fork();

                // Wait for the results and combine them
                int leftResult = leftTask.join();
                int rightResult = rightTask.join();

                return leftResult + rightResult;
            }
        }
    }
}
