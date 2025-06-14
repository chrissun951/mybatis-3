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
package org.apache.ibatis.type;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 有时我们想使用自己的处理器来处理某些 JDBC 类型，只需创建 BaseTypeHandler 的子类，然后在上面加上该注解，声明它要处理的JDBC类型即可;
 */

/**
 * The annotation that specify jdbc types to map {@link TypeHandler}.
 * <p>
 * <b>How to use:</b>
 *
 * <pre>
 * &#064;MappedJdbcTypes({ JdbcType.CHAR, JdbcType.VARCHAR })
 * public class StringTrimmingTypeHandler implements TypeHandler&lt;String&gt; {
 *   // ...
 * }
 * </pre>
 *
 * @author Eduardo Macarron
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MappedJdbcTypes {
  /**
   * Returns jdbc types to map {@link TypeHandler}.
   *
   * @return jdbc types
   */
  JdbcType[] value();

  /**
   * Returns whether map to jdbc null type.
   *
   * @return {@code true} if map, {@code false} if otherwise
   */
  boolean includeNullJdbcType() default false;
}
