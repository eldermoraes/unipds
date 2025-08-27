package com.eldermoraes.aula02;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Ex2IoBoundVirtualThreadsExample {

    public static void main(String[] args) {
        // Cria um executor que lança uma nova thread virtual para cada tarefa.
        // Não há pooling de threads virtuais, pois elas são extremamente leves.
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 100_000).forEach(i -> {
                executor.submit(() -> {
                    // Simula uma chamada de rede bloqueante (I/O).
                    try {
                        System.out.println("Iniciando tarefa I/O-bound " + i + " em " + Thread.currentThread());
                        Thread.sleep(1000); // Simula 1 segundo de espera.
                        System.out.println("Tarefa I/O-bound " + i + " concluída.");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            });
        } // O executor aguarda a conclusão de todas as tarefas antes de fechar.
    }
}