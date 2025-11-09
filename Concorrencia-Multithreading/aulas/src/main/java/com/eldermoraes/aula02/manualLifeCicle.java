package com.eldermoraes.aula02;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class manualLifeCicle {
  public static void main(String[] args) {
    // A partir do Java 19, ExecutorService implementa AutoCloseable.
    // O uso de try-with-resources é a forma preferida e mais segura.
    try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
      executor.submit(() -> System.out.println(
          "Tarefa em execução.")); // executor.shutdown() é chamado automaticamente.
    } // executor.shutdown() é chamado automaticamente.
    System.out.println("Executor desligado via try-with-resources.");

    // Padrão de desligamento manual (para Java < 19 ou cenários complexos).
    ExecutorService manualExecutor = Executors.newFixedThreadPool(2);
    try {
      manualExecutor.submit(() -> {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
        System.out.println("Tarefa no executor manual concluída.");
      });
    } finally {
      System.out.println("Iniciando desligamento do executor manual em " + LocalTime.now());
      shutdownAndAwaitTermination(manualExecutor);
    }
    System.out.println("Executor manual desligado.");
  }

  static void shutdownAndAwaitTermination(ExecutorService pool) {
    // Desabilita novas tarefas de serem submetidas.
    pool.shutdown();
    try {
      // Espera um tempo razoável para as tarefas existentes terminarem.
      if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
        // Cancela tarefas em execução.
        pool.shutdownNow();
        // Espera um tempo razoável para as tarefas responderem ao cancelamento.
        if (!pool.awaitTermination(60, TimeUnit.SECONDS))
          System.err.println("O pool não terminou.");
      }
    } catch (InterruptedException ie) {
      // (Re)Cancela se a thread atual for interrompida.
      pool.shutdownNow();
      // Preserva o status de interrupção.
      Thread.currentThread().interrupt();
    }
  }

}
