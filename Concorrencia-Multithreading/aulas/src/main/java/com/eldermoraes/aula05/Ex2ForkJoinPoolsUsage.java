package com.eldermoraes.aula05;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Ex2ForkJoinPoolsUsage {

    // Reutilizando a classe SumTask do exemplo anterior.
    static class SumTask extends RecursiveTask<Long> {
        //... implementação idêntica ao exemplo 5.1...
        private static final int THRESHOLD = 10_000;
        private final long[] array;
        private final int start;
        private final int end;

        public SumTask(long[] array, int start, int end) {
            this.array = array; this.start = start; this.end = end;
        }
        protected Long compute() {
            if (end - start <= THRESHOLD) {
                long sum = 0; for (int i = start; i < end; i++) sum += array[i];
                return sum;
            }
            int mid = start + (end - start) / 2;
            SumTask left = new SumTask(array, start, mid);
            SumTask right = new SumTask(array, mid, end);
            left.fork();
            long rightResult = right.compute();
            long leftResult = left.join();
            return leftResult + rightResult;
        }
    }

    public static void main(String[] args) {
        long[] numbers = new long[1_000_000];
        for (int i = 0; i < numbers.length; i++) numbers[i] = i + 1;

        // 1. Usando o pool comum (commonPool)
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        System.out.println("Nível de paralelismo do commonPool: " + commonPool.getParallelism());
        long resultCommon = commonPool.invoke(new SumTask(numbers, 0, numbers.length));
        System.out.println("Resultado (commonPool): " + resultCommon);

        // 2. Usando um pool customizado
        // Ideal para isolar cargas de trabalho e evitar contenção no pool comum.
        int customParallelism = 4;
        try (ForkJoinPool customPool = new ForkJoinPool(customParallelism)) {
            System.out.println("Nível de paralelismo do customPool: " + customPool.getParallelism());
            long resultCustom = customPool.invoke(new SumTask(numbers, 0, numbers.length));
            System.out.println("Resultado (customPool): " + resultCustom);
        }
    }
}