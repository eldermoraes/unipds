package com.eldermoraes.aula02;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class fixedTreadPool {
  public static void main(String[] args) {
    //pega nucleos do pocessador
    int coreCount = Runtime.getRuntime().availableProcessors(); // 16
    System.out.println("Número de núcleos de CPU: " + coreCount);

    // try-with-resources garante que o executor seja desligado.
    try (ExecutorService executor = Executors.newFixedThreadPool(coreCount)) {
      // Submete N tarefas CPU-bound.
      for (int i = 0; i < coreCount * 2; i++) {
        final int taskNumber = i;
        executor.submit(() -> {
          System.out.println(
              "Iniciando tarefa CPU-bound " + taskNumber + " em " + Thread.currentThread());
          long result = performIntensiveCalculation();
          System.out.println("Tarefa " + taskNumber + " concluída com resultado " + result + " em "
              + Thread.currentThread());
        });
      }
    }
  }

  private static long performIntensiveCalculation() {
    // Simulação de uma tarefa que mantém a CPU ocupada.
    long sum = 0;
    for (int i = 0; i < 1_000_000_000; i++) {
      sum += i;
    }
    return sum;
  }

}
