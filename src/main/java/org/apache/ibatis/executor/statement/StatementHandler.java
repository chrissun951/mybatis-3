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
package org.apache.ibatis.executor.statement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.ResultHandler;

/**
 * StatementHandler有个基础的抽象实现类 BaseStatementHandler,BaseStatementHandler有三个具体实现类,
 * SimpleStatementHandler,PreparedStatementHandler,CallableStatementHandler.
 *
 * 但是对外,使用RoutingStatementHandler(实现了StatementHandler接口),
 * RoutingStatementHandler通过构造器入参,使用工厂方法设计模式+委托模式,在内部保存了具体的对象引用,
 * 然后将所有请求交给委托的对象,
 *
 * 代理,委托?
 *
 *
 * StatementHandler的作用是
 *
 *
 * Statement类能够执行静态 SQL语句并返回结果(java.sql)
 *
 */

/**
 * @author Clinton Begin
 */
public interface StatementHandler {

  Statement prepare(Connection connection, Integer transactionTimeout) throws SQLException;

  void parameterize(Statement statement) throws SQLException;

  void batch(Statement statement) throws SQLException;

  int update(Statement statement) throws SQLException;

  <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException;

  <E> Cursor<E> queryCursor(Statement statement) throws SQLException;

  BoundSql getBoundSql();

  ParameterHandler getParameterHandler();

}
