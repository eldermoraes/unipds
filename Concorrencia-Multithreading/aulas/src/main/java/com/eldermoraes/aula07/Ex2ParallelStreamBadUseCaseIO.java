package com.eldermoraes.aula07;

import java.util.List;
import java.util.stream.IntStream;

public class Ex2ParallelStreamBadUseCaseIO {

    public static void main(String[] args) {
        List<Integer> ids = IntStream.rangeClosed(1, 20).boxed().toList();

        System.out.println("Iniciando chamadas de API com parallelStream()...");
        long startTime = System.currentTimeMillis();

        ids.parallelStream().forEach(id -> {
            // Simula uma chamada de rede bloqueante.
            try {
                System.out.println("Buscando dados para o ID " + id + " em " + Thread.currentThread());
                Thread.sleep(1000); // Bloqueia a thread por 1 segundo.
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Tempo total: " + duration + " ms.");
        // O tempo total será muito maior do que o esperado
    }
}