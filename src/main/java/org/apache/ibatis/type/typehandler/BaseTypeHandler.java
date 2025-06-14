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
package org.apache.ibatis.type.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.executor.result.ResultMapException;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeReference;

/**
 * 类型处理器基类 The base {@link TypeHandler} for references a generic type.
 * <p>
 * Important: Since 3.5.0, This class never call the {@link ResultSet#wasNull()} and {@link CallableStatement#wasNull()}
 * method for handling the SQL {@code NULL} value. In other words, {@code null} value handling should be performed on
 * subclass.
 * </p>
 *
 * @author Clinton Begin
 * @author Simone Tripodi
 * @author Kzuki Shimizu
 */
public abstract class BaseTypeHandler<T> extends TypeReference<T> implements TypeHandler<T> {

  /**
   * @deprecated Since 3.5.0 - See https://github.com/mybatis/mybatis-3/issues/1203. This field will remove future.
   *             <p>
   *             说明设计上不再需要该字段，因为在3.5.0版本中，BaseTypeHandler类已经不再使用Configuration对象，而是直接使用TypeReference类来实现泛型参数的类型擦除。
   *             因此，BaseTypeHandler类中的configuration字段已经没有实际意义，可以直接删除。
   */
  @Deprecated
  protected Configuration configuration;

  /**
   * Sets the configuration.
   *
   * @param c
   *          the new configuration
   *
   * @deprecated Since 3.5.0 - See https://github.com/mybatis/mybatis-3/issues/1203. This property will remove future.
   */
  @Deprecated
  public void setConfiguration(Configuration c) {
    this.configuration = c;
  }

  @Override
  public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
    if (parameter == null) {

      if (jdbcType == null) {
        throw new TypeException("JDBC requires that the JdbcType must be specified for all nullable " + "parameters.");
      }
      try {
        // type code,jdk sql中定义的类型码,对应一个具体的jdbc类型,

        // PreparedStatement.setNull由数据库jdbc驱动实现,具体实现可能不同
        ps.setNull(i, jdbcType.TYPE_CODE);
      } catch (SQLException e) {
        throw new TypeException("Error setting null for parameter #" + i + " with JdbcType " + jdbcType + " . "
            + "Try setting a different JdbcType for this parameter or a different jdbcTypeForNull "
            + "configuration property. " + "Cause: " + e, e);
      }
    } else {
      try {
        // 非空时,由子类实现具体的类型设置,模板设计
        setNonNullParameter(ps, i, parameter, jdbcType);
      } catch (Exception e) {
        throw new TypeException("Error setting non null for parameter #" + i + " with JdbcType " + jdbcType + " . "
            + "Try setting a different JdbcType for this parameter or a different configuration property" + "." + " "
            + "Cause: " + e, e);
      }
    }
  }

  @Override
  public T getResult(ResultSet rs, String columnName) throws SQLException {
    try {
      return getNullableResult(rs, columnName);
    } catch (Exception e) {
      throw new ResultMapException(
          "Error attempting to get column '" + columnName + "' from result set.  " + "Cause: " + e, e);
    }
  }

  @Override
  public T getResult(ResultSet rs, int columnIndex) throws SQLException {
    try {
      return getNullableResult(rs, columnIndex);
    } catch (Exception e) {
      throw new ResultMapException(
          "Error attempting to get column #" + columnIndex + " from result set.  " + "Cause: " + e, e);
    }
  }

  @Override
  public T getResult(CallableStatement cs, int columnIndex) throws SQLException {
    try {
      return getNullableResult(cs, columnIndex);
    } catch (Exception e) {
      throw new ResultMapException(
          "Error attempting to get column #" + columnIndex + " from callable statement.  Cause: " + e, e);
    }
  }

  /**
   * 向PreparedStatement指定变量位置设置非空参数
   *
   * @param ps
   * @param i
   * @param parameter
   * @param jdbcType
   *
   * @throws SQLException
   */
  public abstract void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType)
      throws SQLException;

  /**
   * 从ResultSet中按照字段名称获取非空结果 Gets the nullable result.
   *
   * @param rs
   *          the rs
   * @param columnName
   *          Column name, when configuration <code>useColumnLabel</code> is <code>false</code>
   *
   * @return the nullable result
   *
   * @throws SQLException
   *           the SQL exception
   */
  public abstract T getNullableResult(ResultSet rs, String columnName) throws SQLException;

  /**
   * 从ResultSet中按照字段索引获取非空结果
   *
   * @param rs
   * @param columnIndex
   *
   * @return
   *
   * @throws SQLException
   */
  public abstract T getNullableResult(ResultSet rs, int columnIndex) throws SQLException;

  /**
   * 从CallableStatement中按照字段索引获取非空结果
   *
   * @param cs
   * @param columnIndex
   *
   * @return
   *
   * @throws SQLException
   */
  public abstract T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException;

}
