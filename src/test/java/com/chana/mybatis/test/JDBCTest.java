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

  public static void main(String[] args) throws SQLException {
    // jdbc连接数据库
    // Class.forName("com.mysql.cj.jdbc.Driver");
    // 创建数据库连接对象
    String url = "jdbc:mysql://localhost:33106/db_account?useUnicode=true" + "&characterEncoding=utf-8";
    String root = "root";
    String number = "123456";

    // 处理结果集

    try (Connection conection = DriverManager.getConnection(url, root, number)) {
      // 创建sql语句
      PreparedStatement statement = conection.prepareStatement("select * from account_tbl where id = ?");
      statement.setInt(1, 2);
      conection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
      // 执行sql语句
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        Object object1 = resultSet.getObject(1);
        Object object2 = resultSet.getObject(2);
        Object object3 = resultSet.getObject(3);
        // 拼接打印三列数据
        System.out.println(object1 + ", " + object2 + ", " + object3);
        // System.out.println(resultSet.getString("id"));
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
