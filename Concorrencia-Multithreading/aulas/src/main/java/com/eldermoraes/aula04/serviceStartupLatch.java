package com.eldermoraes.aula04;

import com.eldermoraes.aula04.Ex1ServiceStartupLatchExample.ServiceInitializer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class serviceStartupLatch {

  static class ServiceInitializer implements Runnable {
    private final String serviceName;
    private final CountDownLatch latch;
    private final int startupTime;

    public ServiceInitializer(String serviceName, int startupTime, CountDownLatch latch) {
      this.serviceName = serviceName;
      this.startupTime = startupTime;
      this.latch = latch;
    }

    @Override
    public void run() {
      try {
        System.out.println("Inicializando " + serviceName + "...");
        Thread.sleep(startupTime);
        System.out.println(serviceName + " inicializado.");
        latch.countDown(); // Decrementa o contador do latch.
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }


  public static void main(String[] args) throws InterruptedException {
    int numberOfServices = 3;
    CountDownLatch startupLatch = new CountDownLatch(numberOfServices);
    try (ExecutorService executor = Executors.newFixedThreadPool(numberOfServices)) {
      executor.submit(new Ex1ServiceStartupLatchExample.ServiceInitializer("DatabaseService", 3000, startupLatch));
      executor.submit(new Ex1ServiceStartupLatchExample.ServiceInitializer("CacheService", 5000, startupLatch));
      executor.submit(new Ex1ServiceStartupLatchExample.ServiceInitializer("MessagingService", 7000, startupLatch));

      System.out.println("Thread principal aguardando a inicialização dos serviços...");
      startupLatch.await(); // Bloqueia até o contador chegar a zero.

      System.out.println("Todos os serviços foram inicializados. Aplicação pronta!");
    }
  }
}