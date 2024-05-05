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
package com.chana.mybatis.test;

import java.sql.*;

public class JDBCTest {

  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    // jdbc连接数据库
    // Class.forName("com.mysql.cj.jdbc.Driver");
    // 创建数据库连接对象
    Connection conection = DriverManager.getConnection(
        "jdbc:mysql://localhost:33106/db_account?useUnicode=true" + "&characterEncoding=utf-8", "root", "123456");
    // 创建sql语句
    PreparedStatement statement = conection.prepareStatement("select * from account_tbl");
    conection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    // 执行sql语句
    ResultSet resultSet = statement.executeQuery();
    // 处理结果集
    while (resultSet.next()) {
      System.out.println(resultSet.getString("id"));
    }

    conection.close();

  }
}
