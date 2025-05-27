/*
 *    Copyright 2009-2023 the original author or authors.
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
package org.apache.ibatis.executor.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * @author Clinton Begin
 */
public class PreparedStatementHandler extends BaseStatementHandler {

  public PreparedStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameter,
      RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
    super(executor, mappedStatement, parameter, rowBounds, resultHandler, boundSql);
  }

  @Override
  public int update(Statement statement) throws SQLException {
    //底层能力都是Statement的
    PreparedStatement ps = (PreparedStatement) statement;
    ps.execute();
    int rows = ps.getUpdateCount();
    //取配置 取key,
    Object parameterObject = boundSql.getParameterObject();
    KeyGenerator keyGenerator = mappedStatement.getKeyGenerator();
    keyGenerator.processAfter(executor, mappedStatement, ps, parameterObject);
    return rows;
  }

  @Override
  public void batch(Statement statement) throws SQLException {
    PreparedStatement ps = (PreparedStatement) statement;
    ps.addBatch();
  }

  @Override
  public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
    PreparedStatement ps = (PreparedStatement) statement;
    ps.execute();
    //和Update区别在于有结果集处理
    return resultSetHandler.handleResultSets(ps);
  }

  @Override
  public <E> Cursor<E> queryCursor(Statement statement) throws SQLException {
    PreparedStatement ps = (PreparedStatement) statement;
    ps.execute();
    return resultSetHandler.handleCursorResultSets(ps);
  }

  /**
   * 实例化Statement,此处,实例化的就是 PreparedStatement
   *
   * 这里的主要逻辑就是jdbc/java.sql的逻辑: connection,prepareStatement的使用方式,
   *
   * @param connection
   * @return
   * @throws SQLException
   */
  @Override
  protected Statement instantiateStatement(Connection connection) throws SQLException {
    String sql = boundSql.getSql();
    if (mappedStatement.getKeyGenerator() instanceof Jdbc3KeyGenerator) {
      String[] keyColumnNames = mappedStatement.getKeyColumns();
      if (keyColumnNames == null) {
        // - 插入数据后需要获取数据库自动生成的主键
        // 常用于INSERT操作后获取自增ID
        // MyBatis中通过 Jdbc3KeyGenerator 来处理
        return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      } else {
        //- 需要获取指定列的生成值
        //- 可以精确控制要返回的自动生成值的列
        //- 在复合主键或特定字段自动生成的场景下使用
        return connection.prepareStatement(sql, keyColumnNames);
      }
    }
    if (mappedStatement.getResultSetType() == ResultSetType.DEFAULT) {
      //最基本的预编译SQL语句创建,不需要特殊的结果集类型或自增主键获取,适用于普通的增删改查操作
      return connection.prepareStatement(sql);
    } else {
      //- 需要特殊的结果集类型（如可滚动结果集）
      //- MyBatis中通过 ResultSetType 枚举来控制：
      //- FORWARD_ONLY ：结果集只能向前移动
      //- SCROLL_SENSITIVE ：可滚动且对数据库变化敏感
      //- SCROLL_INSENSITIVE ：可滚动但不敏感
      return connection.prepareStatement(sql, mappedStatement.getResultSetType().getValue(),
          ResultSet.CONCUR_READ_ONLY);
    }
  }

  @Override
  public void parameterize(Statement statement) throws SQLException {
    //设置参数,StatementHandler包含一个parameterHandler
    parameterHandler.setParameters((PreparedStatement) statement);
  }

}
