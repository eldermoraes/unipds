package com.eldermoraes.aula04;

import com.eldermoraes.aula04.Ex3SemaphoreResourcePoolExample.ConnectionPool;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;
public class semaphoreResourcePool {
  // Simula um pool de conexões com um número limitado de licenças.
  static class ConnectionPool {

    private final Semaphore semaphore;

    public ConnectionPool(int maxConnections) {
      // O 'true' torna o semáforo "justo" (fair), atendendo as threads na ordem de chegada.
      this.semaphore = new Semaphore(maxConnections, true);
    }

    public void useConnection(int threadId) throws InterruptedException {
      semaphore.acquire(); // Adquire uma permissão. Bloqueia se nenhuma estiver disponível.
      try {
        System.out.println("Thread " + threadId + " obteve uma conexão.");
        Thread.sleep(2000); // Simula o uso da conexão.
        System.out.println("Thread " + threadId + " liberando a conexão.");
      } finally {
        semaphore.release(); // Libera a permissão. ESSENCIAL estar no finally.
      }
    }
  }

  public static void main(String[] args) {
    Ex3SemaphoreResourcePoolExample.ConnectionPool pool = new Ex3SemaphoreResourcePoolExample.ConnectionPool(3); // Apenas 3 conexões simultâneas.
    // Usando threads virtuais para simular muitos clientes tentando acessar o pool.
    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      IntStream.range(0, 10).forEach(i ->
          executor.submit(() -> {
            try {
              pool.useConnection(i);
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
            }
          })
      );
    }
  }
}