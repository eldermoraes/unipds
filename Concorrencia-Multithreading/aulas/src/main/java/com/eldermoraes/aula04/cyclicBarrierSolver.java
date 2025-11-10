package com.eldermoraes.aula04;

import com.eldermoraes.aula04.Ex2CyclicBarrierSolverExample.Worker;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class cyclicBarrierSolver {
  static class Worker implements Runnable {

    private final int id;
    private final CyclicBarrier barrier;

    public Worker(int id, CyclicBarrier barrier) {
      this.id = id;
      this.barrier = barrier;
    }

    @Override
    public void run() {
      try {
        // Fase 1: Processamento de dados
        System.out.println("Worker " + id + " iniciando Fase 1.");
        Thread.sleep(1000 + (id * 500));
        System.out.println("Worker " + id + " concluiu Fase 1, aguardando na barreira.");
        barrier.await();

        // Fase 2: Validação de dados
        System.out.println("Worker " + id + " iniciando Fase 2.");
        Thread.sleep(1000 + (id * 500));
        System.out.println("Worker " + id + " concluiu Fase 2, aguardando na barreira.");
        barrier.await();

        System.out.println("Worker " + id + " concluiu o trabalho.");

      } catch (InterruptedException | BrokenBarrierException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  public static void main(String[] args) {
    int numberOfWorkers = 3;

    // Ação da barreira: executada quando todas as threads chegam.
    Runnable barrierAction = () -> System.out.println("\n--- Barreira quebrada! Todos os workers concluíram a fase. Próxima fase iniciada. ---\n");
    CyclicBarrier barrier = new CyclicBarrier(numberOfWorkers, barrierAction);

    try (ExecutorService executor = Executors.newFixedThreadPool(numberOfWorkers)) {
      for (int i = 0; i < numberOfWorkers; i++) {
        executor.submit(new Ex2CyclicBarrierSolverExample.Worker(i, barrier));
      }
    }
  }
}