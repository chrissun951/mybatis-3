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
package org.apache.ibatis.type.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.ByteArrayUtils;
import org.apache.ibatis.type.JdbcType;

/**
 * @author Clinton Begin
 */
public class ByteObjectArrayTypeHandler extends BaseTypeHandler<Byte[]> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, Byte[] parameter, JdbcType jdbcType)
      throws SQLException {
    ps.setBytes(i, ByteArrayUtils.convertToPrimitiveArray(parameter));
  }

  @Override
  public Byte[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
    byte[] bytes = rs.getBytes(columnName);
    return getBytes(bytes);
  }

  @Override
  public Byte[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    byte[] bytes = rs.getBytes(columnIndex);
    return getBytes(bytes);
  }

  @Override
  public Byte[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    byte[] bytes = cs.getBytes(columnIndex);
    return getBytes(bytes);
  }

  private Byte[] getBytes(byte[] bytes) {
    Byte[] returnValue = null;
    if (bytes != null) {
      returnValue = ByteArrayUtils.convertToObjectArray(bytes);
    }
    return returnValue;
  }

}
