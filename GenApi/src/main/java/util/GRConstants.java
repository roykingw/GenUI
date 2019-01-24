package util;

/**
 * Created by roykingw on 2017/12/14 0014.
 */
public class GRConstants {
    /**
     *   这个版本的dubbo内部的netty端口号，默认为22222，不知道为什么会和tomcat冲突。可以修改下端口号
     *   为了消费者和提供者两边端口一致，写在常量类里。 如果不用这种方式，可以用-D在两边分别修改端口。
     *   见dubbo-2.5.8.jar中com.alibaba.dubbo.qos.common.Constants
     */
    public static String dubboQosPort="20881";
}
