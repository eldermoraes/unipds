package com.eldermoraes.aula07;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Ex1ParallelStreamGoodUseCase {

    public static void main(String[] args) {
        List<Long> numbers = LongStream.rangeClosed(1, 10_000_000)
                .boxed()
                .collect(Collectors.toList());

        // Tarefa: encontrar a soma dos quadrados de todos os números primos na lista.
        // É CPU-bound, stateless e a fonte (ArrayList) é eficientemente divisível.

        // Execução Sequencial
        long startTimeSeq = System.nanoTime();
        long sumSeq = numbers.stream()
                .filter(Ex1ParallelStreamGoodUseCase::isPrime)
                .mapToLong(n -> n * n)
                .sum();
        long durationSeq = (System.nanoTime() - startTimeSeq) / 1_000_000;
        System.out.println("Resultado Sequencial: " + sumSeq + " em " + durationSeq + " ms.");

        // Execução Paralela
        long startTimePar = System.nanoTime();
        long sumPar = numbers.parallelStream()
                .filter(Ex1ParallelStreamGoodUseCase::isPrime)
                .mapToLong(n -> n * n)
                .sum();
        long durationPar = (System.nanoTime() - startTimePar) / 1_000_000;
        System.out.println("Resultado Paralelo: " + sumPar + " em " + durationPar + " ms.");
    }

    private static boolean isPrime(long n) {
        if (n <= 1) return false;
        for (long i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
}