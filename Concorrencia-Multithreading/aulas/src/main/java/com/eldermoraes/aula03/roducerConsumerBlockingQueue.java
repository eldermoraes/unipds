package com.eldermoraes.aula03;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class roducerConsumerBlockingQueue {
  public static void main(String[] args) {
    // Uma fila com capacidade fixa para 10 itens.
    BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      // Produtor: gera números e os coloca na fila.
      executor.submit(() -> {
        try {
          for (int i = 0; i < 100; i++) {
            System.out.println("Produzindo: " + i);
            queue.put(i); // Bloqueia se a fila estiver cheia.
            Thread.sleep(50); // Simula tempo de produção.
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      });
      // Consumidor: retira números da fila e os processa.
      executor.submit(() -> {
        try {
          while (true) {
            Integer value = queue.take(); // Bloqueia se a fila estiver vazia.
            System.out.println("Consumindo: " + value);
            Thread.sleep(100); // Simula tempo de processamento.
            if (value == 99) break; // Condição de parada.
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      });
    }
  }
}