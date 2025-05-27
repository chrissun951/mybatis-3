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
package generator.plugin;

import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.BaseExecutor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

@Intercepts({ @Signature(type = BaseExecutor.class, method = "query", args = { MappedStatement.class, Object.class,
    RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class }) })
public class HelloWorldPlugin implements Interceptor {

  private String message;

  /**
   * 拦截器类必须实现该方法。 拦截器拦截到目标方法时，会将操作转接到该 intercept 方法上，其中的参数 Invocation 为拦截到的目标方法
   *
   * @param invocation
   *
   * @return
   *
   * @throws Throwable
   */
  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    System.out.println("Hello World Plugin:" + message);
    Object proceed = invocation.proceed();
    System.out.println(proceed + " Hello World Plugin:" + message);

    return proceed;

  }

  /**
   * 拦截器类可以选择实现该方法。 该方法中可以输出一个对象来替换输入参数传入的目标对象
   *
   * @param target
   *
   * @return
   */
  // @Override
  // public Object plugin(Object target) {
  // return Interceptor.super.plugin(target);
  // }

  /**
   * 拦截器类可以选择实现该方法。 该方法用来为拦截器设置属性，比如这里的 message 属性。
   *
   * @param properties
   */
  @Override
  public void setProperties(Properties properties) {
    message = properties.getProperty("message");
  }
}
