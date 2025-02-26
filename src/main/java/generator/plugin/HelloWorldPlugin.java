package generator.plugin;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.BaseExecutor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

@Intercepts({
        @Signature(type = BaseExecutor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class,
                CacheKey.class, BoundSql.class})
}
)
public class HelloWorldPlugin implements Interceptor {

    private String message;

    /**
     * 拦截器类必须实现该方法。
     * 拦截器拦截到目标方法时，会将操作转接到该 intercept 方法上，其中的参数 Invocation 为拦截到的目标方法
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("Hello World Plugin:" + message);
        Object proceed = invocation.proceed();
        System.out.println(proceed+" Hello World Plugin:" + message);

        return proceed;

    }

    /**
     * 拦截器类可以选择实现该方法。
     * 该方法中可以输出一个对象来替换输入参数传入的目标对象
     * @param target
     * @return
     */
    //@Override
    //public Object plugin(Object target) {
    //    return Interceptor.super.plugin(target);
    //}

    /**
     *   拦截器类可以选择实现该方法。
     *     该方法用来为拦截器设置属性，比如这里的 message 属性。
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
        message = properties.getProperty("message");
    }
}
