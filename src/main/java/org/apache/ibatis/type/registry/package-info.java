package org.apache.ibatis.type.registry;


/*

定义了大量的类型处理器之后，
MyBatis 还需要在遇到某种类型的数据时，
快速找到与数据的类型对应的类型处理器。这个过程就需要各种类型注册表的帮助
 */