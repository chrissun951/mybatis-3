/*
 *    Copyright 2009-2025 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.chana.mybatis.test;

import static org.junit.Assert.fail;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class MyHandlerTest {

  @Test
  public void testConcurrentHandling() throws InterruptedException {

    MyHandler.Key testKey = new MyHandler.Key("test");
    int threadCount = 3;
    int iterations = 1000; // 增加测试迭代次数
    AtomicInteger concurrentExecutions = new AtomicInteger(0);
    AtomicInteger maxConcurrentExecutions = new AtomicInteger(0);

    // 模拟工作执行器

    SomeWorkExecutor executor = key -> {
      int current = concurrentExecutions.incrementAndGet();
      maxConcurrentExecutions.set(Math.max(current, maxConcurrentExecutions.get()));
      Thread.yield(); // 增加线程切换的机会
      try {
        Thread.sleep(1); // 使用更短的睡眠时间
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
      concurrentExecutions.decrementAndGet();
    };

    MyHandler handler = new MyHandler(executor);

    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
    CountDownLatch completionLatch = new CountDownLatch(threadCount * iterations);

    // 多次重复测试
    for (int i = 0; i < iterations; i++) {
      for (int j = 0; j < threadCount; j++) {
        executorService.submit(() -> {
          try {
            handler.handle(testKey);
          } finally {
            completionLatch.countDown();
          }
        });
      }
    }

    completionLatch.await();
    executorService.shutdown();

    if (maxConcurrentExecutions.get() > 1) {
      fail("检测到并发执行！最大并发数: " + maxConcurrentExecutions.get());
    }
  }
}
