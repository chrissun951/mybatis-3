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
package org.apache.ibatis.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * 是一个参数名解析器,用来按顺序列出方法中的虚参,并对实参进行名称标注。
 * <p>
 * 该解析器会从方法参数中查找 {@link Param} 注解,如果存在,则使用注解中的名称作为参数名,否则使用参数索引作为名称。
 * </p>
 * <p>
 * 如果方法参数中有 {@link RowBounds} 或 {@link ResultHandler} 类型参数,则这些参数不会被标注。
 * </p>
 * <p>
 * 如果方法参数中有 {@link Collection} 或数组类型参数,则这些参数会被包装成 {@link ParamMap} 对象,并使用 {@code collection} 或 {@code array} 作为名称。
 * </p>
 *
 * @author <NAME>
 */
public class ParamNameResolver {

  public static final String GENERIC_NAME_PREFIX = "param";

  public static final String[] GENERIC_NAME_CACHE = new String[10];

  static {
    for (int i = 0; i < 10; i++) {
      GENERIC_NAME_CACHE[i] = GENERIC_NAME_PREFIX + (i + 1);
    }
  }

  private final boolean useActualParamName;

  /**
   * <p>
   * The key is the index and the value is the name of the parameter.<br />
   * The name is obtained from {@link Param} if specified. When {@link Param} is not specified, the parameter index is
   * used. Note that this index could be different from the actual index when the method has special parameters (i.e.
   * {@link RowBounds} or {@link ResultHandler}).
   * </p>
   * <ul>
   * <li>aMethod(@Param("M") int a, @Param("N") int b) -&gt; {{0, "M"}, {1, "N"}}</li>
   * <li>aMethod(int a, int b) -&gt; {{0, "0"}, {1, "1"}}</li>
   * <li>aMethod(int a, RowBounds rb, int b) -&gt; {{0, "0"}, {2, "1"}}</li>
   * </ul>
   */
  /**
   * key是索引,value,如果用了Param,就用Param的值为value,否则使用索引的字符串形式,
   *
   * 时机:在调用映射接口方法的时候确定,
   */
  private final SortedMap<Integer, String> names;

  private boolean hasParamAnnotation;

  /**
   * 使用 Configuration 和method构造ParamNameResolver对象。
   *
   * @param config
   * @param method
   */
  public ParamNameResolver(Configuration config, Method method) {
    this.useActualParamName = config.isUseActualParamName();
    final Class<?>[] paramTypes = method.getParameterTypes();
    final Annotation[][] paramAnnotations = method.getParameterAnnotations();
    final SortedMap<Integer, String> map = new TreeMap<>();
    int paramCount = paramAnnotations.length;
    // get names from @Param annotations
    for (int paramIndex = 0; paramIndex < paramCount; paramIndex++) {
      if (isSpecialParameter(paramTypes[paramIndex])) {
        // skip special parameters
        continue;
      }
      String name = null;
      //一种方式是Param注解
      for (Annotation annotation : paramAnnotations[paramIndex]) {
        if (annotation instanceof Param) {
          hasParamAnnotation = true;
          name = ((Param) annotation).value();
          break;
        }
      }
      if (name == null) {
        // @Param was not specified.
        if (useActualParamName) {
          //配置项,如果配置了,按照配置取参数,
          name = getActualParamName(method, paramIndex);
        }
        if (name == null) {
          // use the parameter index as the name ("0", "1", ...)
          // gcode issue #71
//          使用索引
          name = String.valueOf(map.size());
        }
      }
      map.put(paramIndex, name);
    }
    names = Collections.unmodifiableSortedMap(map);
  }

  private static boolean isSpecialParameter(Class<?> clazz) {
    return RowBounds.class.isAssignableFrom(clazz) || ResultHandler.class.isAssignableFrom(clazz);
  }

  /**
   * Wrap to a {@link ParamMap} if object is {@link Collection} or array.
   *
   * @param object
   *          a parameter object
   * @param actualParamName
   *          an actual parameter name (If specify a name, set an object to {@link ParamMap} with specified name)
   *
   * @return a {@link ParamMap}
   *
   * @since 3.5.5
   */
  public static Object wrapToMapIfCollection(Object object, String actualParamName) {
    if (object instanceof Collection) {
      ParamMap<Object> map = new ParamMap<>();
      map.put("collection", object);
      if (object instanceof List) {
        map.put("list", object);
      }
      Optional.ofNullable(actualParamName).ifPresent(name -> map.put(name, object));
      return map;
    }
    if (object != null && object.getClass().isArray()) {
      ParamMap<Object> map = new ParamMap<>();
      map.put("array", object);
      Optional.ofNullable(actualParamName).ifPresent(name -> map.put(name, object));
      return map;
    }
    return object;
  }

  private String getActualParamName(Method method, int paramIndex) {
    return ParamNameUtil.getParamNames(method).get(paramIndex);
  }

  /**
   * Returns parameter names referenced by SQL providers.
   *
   * @return the names
   */
  public String[] getNames() {
    return names.values().toArray(new String[0]);
  }

  /**
   * 能够在映射文件中引用参数的名称。
   * <p>
   * A single non-special parameter is returned without a name. Multiple parameters are named using the naming rule. In
   * addition to the default names, this method also adds the generic names (param1, param2, ...).
   * </p>
   *
   * @param args
   *          the args
   *
   * @return the named params
   */
  public Object getNamedParams(Object[] args) {
    final int paramCount = names.size();
    if (args == null || paramCount == 0) {
      return null;
    }
    if (!hasParamAnnotation && paramCount == 1) {
      Object value = args[names.firstKey()];
      return wrapToMapIfCollection(value, useActualParamName ? names.get(names.firstKey()) : null);
    } else {
      final Map<String, Object> param = new ParamMap<>();
      int i = 0;
      for (Map.Entry<Integer, String> entry : names.entrySet()) {
        param.put(entry.getValue(), args[entry.getKey()]);
        // add generic param names (param1, param2, ...)
        final String genericParamName = i < 10 ? GENERIC_NAME_CACHE[i] : GENERIC_NAME_PREFIX + (i + 1);
        // ensure not to overwrite parameter named with @Param
        if (!names.containsValue(genericParamName)) {
          param.put(genericParamName, args[entry.getKey()]);
        }
        i++;
      }
      return param;
    }
  }

}
