package com.eldermoraes.aula08;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Ex1MassiveConcurrencyWithVirtualThreads {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        // Simula o recebimento de 200.000 requisições, cada uma tratada
        // em sua própria thread virtual.
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 200_000).forEach(i -> {
                executor.submit(() -> {
                    // Simula uma tarefa que faz I/O, como uma chamada a um banco de dados
                    // ou a uma API externa.
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            });
        } // O try-with-resources garante que o executor espere todas as tarefas terminarem.

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Processadas 200.000 tarefas em " + duration + " ms.");
    }
}