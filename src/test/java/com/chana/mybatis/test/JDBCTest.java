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
package com.chana.mybatis.test;

import java.sql.*;

import org.junit.Test;

public class JDBCTest {
  // jdbc连接数据库
  // Class.forName("com.mysql.cj.jdbc.Driver");
  // 创建数据库连接对象
  String url = "jdbc:mysql://localhost:33106/db_account?useUnicode=true" + "&characterEncoding=utf-8";
  String root = "root";
  String number = "123456";

  @Test
  public void testJDBC2() {
  }


  @Test
  public void testJDBC1() {


    // 处理结果集

    try (Connection connection = DriverManager.getConnection(url, root, number)) {
      // 创建sql语句
      PreparedStatement statement = connection.prepareStatement("select * from account_tbl where id = ?");
      statement.setInt(1, 2);
      connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
      // 执行sql语句
//      ResultSet resultSet = statement.executeQuery();
      boolean execute = statement.execute();
      System.out.println("execute = " + execute);
      ResultSet resultSet = statement.getResultSet();
      while (resultSet.next()) {
        int object1 = resultSet.getInt("id");
        String object2 = resultSet.getString("user_id");
        int object3 = resultSet.getInt("money");
        // 拼接打印三列数据
        System.out.println(object1 + ", " + object2 + ", " + object3);
        // System.out.println(resultSet.getString("id"));
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
