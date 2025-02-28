demo
---debug过程看的差不多了

1. 初始化阶段: 加载资源文件,解析配置文件,映射文件 XxxBuilder,把所有配置都存储到 Configuration中,最终返回一个SqlSessionFactory
2. 数据库读写阶段: 通过SqlSession执行调用映射接口,实现SQL执行,从Configuration中取出配置,通过Executor执行SQL

整体流程如此,
然后是每个点的横向扩展,

