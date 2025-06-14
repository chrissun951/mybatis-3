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

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Java 类型T <-> JDBC Type
 *
 *
 *将 Java 对象的值设置到 PreparedStatement 对象中,或者
 * 从 ResultSet 对象中获取指定列(通过列名,或者列索引定位)的结果。
 */

/**
 * @author Clinton Begin
 */
public interface TypeHandler<T> {

  /**
   * 用于将 Java 对象的值设置到 PreparedStatement 对象中
   *
   * @param ps
   * @param i
   * @param parameter
   * @param jdbcType
   *
   * @throws SQLException
   */
  void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;

  /**
   * 从 ResultSet 中获取指定列的结果，需要传入列名作为参数 Gets the result.
   *
   * @param rs
   *          the rs
   * @param columnName
   *          Column name, when configuration <code>useColumnLabel</code> is <code>false</code>
   *
   * @return the result
   *
   * @throws SQLException
   *           the SQL exception
   */
  T getResult(ResultSet rs, String columnName) throws SQLException;

  /**
   * 从 ResultSet 中获取指定索引位置的结果，需要传入列的索引作为参数
   *
   * @param rs
   * @param columnIndex
   *
   * @return
   *
   * @throws SQLException
   */
  T getResult(ResultSet rs, int columnIndex) throws SQLException;

  T getResult(CallableStatement cs, int columnIndex) throws SQLException;

}
