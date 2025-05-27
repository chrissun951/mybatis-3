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

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import generator.domain.AccountTbl;
import generator.mapper.AccountTblMapper;

public class MybatisLauncher {

  public static void javaConfig() throws IOException {

    Configuration configuration = new Configuration();

    // Creating an UnpooledDataSource for simplicity
    UnpooledDataSource dataSource = new UnpooledDataSource();
    dataSource.setDriver("com.mysql.cj.jdbc.Driver");
    dataSource
      .setUrl("jdbc:mysql://localhost:33106/db_account?useUnicode=true&characterEncoding=utf-8&useSSL" + "=false");
    dataSource.setUsername("root");
    dataSource.setPassword("123456");

    Environment development = new Environment("development", new JdbcTransactionFactory(), dataSource);

    configuration.setEnvironment(development);
    configuration.addMapper(AccountTblMapper.class);
    // String resource = ""; // 你的 XML 映射器文件路径
    // InputStream inputStream = Resources.getResourceAsStream(resource);
    // XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(inputStream);
    // xmlConfigBuilder.parse();
    // configuration = xmlConfigBuilder.getConfiguration();
    // 步骤 4：指定 XML 映射器文件位置并加载
    String resource = "classpath:mapper/AccountTblMapper.xml"; // 你的 XML 映射器文件路径
    try (InputStream inputStream = MybatisLauncher.class.getResourceAsStream("/" + resource)) {
      XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(inputStream, configuration, resource,
        configuration.getSqlFragments());
      xmlMapperBuilder.parse();
    } catch (Exception e) {
      e.printStackTrace();
    }

    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      AccountTblMapper mapper = sqlSession.getMapper(AccountTblMapper.class);
      AccountTbl accountTbl = new AccountTbl();
      accountTbl.setId(22);
      accountTbl.setUserId("33");
      accountTbl.setMoney(44);
      int insert = mapper.insert(accountTbl);

      System.out.println("insert = " + insert);
    }
  }

  public static void xmlConfig() throws IOException {
    String resource = "mapper/mybatis-config.xml";

    InputStream inputStream = null;
    //这里例子通过Resources加载配置文件,有其它方式吗,
    //这部分的功能jdk自己能实现吗,mybatis为什么额外实现,
    //comment by sjh: io包
    inputStream = Resources.getResourceAsStream(resource);
    //解析过程,是通过build实现
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);


    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      AccountTblMapper mapper = sqlSession.getMapper(AccountTblMapper.class);
      AccountTbl o = mapper.selectByPrimaryKey(88L);
      System.out.println("o = " + o);
    }
  }

  public static void main(String[] args) {
    try {
      xmlConfig();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


}
