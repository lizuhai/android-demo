package com.example.zhli.lottery.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * 工厂类
 * 依据配置文件，加载实例
 * Created by zhli on 2015/2/9.
 */
public class BeanFactory {

    private static Properties properties;
    static {
        properties = new Properties();
        try {
//            InputStream is = new FileInputStream("bean.properties");
//            System.out.println(is);
//            properties.load(is);
            properties.load(BeanFactory.class.getClassLoader().getResourceAsStream("src/bean.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载需要的实现类
     * @param clazz
     * @return
     */
    public static <T> T getImpl(Class<T> clazz) {
        String key = clazz.getSimpleName();
        String className = properties.getProperty(key);
        System.out.println(className);
        try {
            return (T) Class.forName(className).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
