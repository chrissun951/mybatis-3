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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyHandler {
  private final SomeWorkExecutor someWorkExecutor;
  private final ConcurrentHashMap<Key, Lock> lockMap = new ConcurrentHashMap<>();

  public MyHandler(SomeWorkExecutor someWorkExecutor) {
    this.someWorkExecutor = someWorkExecutor;
  }

  public void handle(Key key) {
    // This can lead to OOM as it creates locks without removing them
    Lock keyLock = lockMap.computeIfAbsent(key, (k) -> new ReentrantLock());
    keyLock.lock();
    try {
      someWorkExecutor.process(key);
    } finally {
      lockMap.remove(key);
      keyLock.unlock();
    }

  }

  // 为测试添加的简单Key类
  public static class Key {
    private final String value;

    public Key(String value) {
      this.value = value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;
      Key key = (Key) o;
      return value.equals(key.value);
    }

    @Override
    public int hashCode() {
      return value.hashCode();
    }
  }
}
