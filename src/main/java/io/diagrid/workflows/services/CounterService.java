package io.diagrid.workflows.services;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CounterService {
  private AtomicInteger counter = new AtomicInteger(0);
  public void incrementCounter() {
    counter.incrementAndGet();
  }
  public int getCounter() {
    return counter.get();
  }
}
