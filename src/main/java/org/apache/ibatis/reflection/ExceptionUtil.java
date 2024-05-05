/*
 *    Copyright 2009-2024 the original author or authors.
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
package org.apache.ibatis.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * @author Clinton Begin
 */
public class ExceptionUtil {

  private ExceptionUtil() {
    // Prevent Instantiation
  }

  /**
   * 拆包 指定的几个异常类型 为什么要拆包呢? 1.在jdk反射中,当调用目标方法异常时,会抛出InvocationTargetException, 这个异常太宽泛了,所以拆包获取真正的异常: 调用目标方法时的真正异常
   * ----另外一点, InvocationTargetException 是反射操作必定会抛出的异常,拆包,可以更容易管理,,, ----但会导致异常不可控, 2.
   * UndeclaredThrowableException,用于处理在继承或实现场景,子类想要抛出父类或接口未声明过的非运行时异常, 由于语法限制,在操作上会被包装为UndeclaredThrowableException.
   * 这个异常也需要拆包,获取真正的异常.
   *
   * @param wrapped
   *
   * @return
   */

  public static Throwable unwrapThrowable(Throwable wrapped) {
    Throwable unwrapped = wrapped;
    while (true) {
      if (unwrapped instanceof InvocationTargetException) {
        unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
      } else if (unwrapped instanceof UndeclaredThrowableException) {
        unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
      } else {
        return unwrapped;
      }
    }
  }

}
