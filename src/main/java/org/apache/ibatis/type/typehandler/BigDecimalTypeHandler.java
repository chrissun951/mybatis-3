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

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;

/**
 * 类型处理器的具体实现,其实都在驱动包中, 类型处理器,只是做了分类,分组处理,更内聚,更清晰,更易于维护,
 *
 * @author Clinton Begin
 */
public class BigDecimalTypeHandler extends BaseTypeHandler<BigDecimal> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, BigDecimal parameter, JdbcType jdbcType)
      throws SQLException {
    ps.setBigDecimal(i, parameter);
  }

  @Override
  public BigDecimal getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return rs.getBigDecimal(columnName);
  }

  @Override
  public BigDecimal getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return rs.getBigDecimal(columnIndex);
  }

  @Override
  public BigDecimal getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return cs.getBigDecimal(columnIndex);
  }
}
