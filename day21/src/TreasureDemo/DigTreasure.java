package TreasureDemo;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DigTreasure {
    public static void main(String[] args) throws Exception {
        byte[] bytes = Files.readAllBytes(Paths.get("C:\\Users\\WIN10\\Desktop\\java\\homework\\day21\\Treasure.class"));

        ClassLoader classLoader = new ClassLoader(){
            protected Class<?> findClass(String name){
                return defineClass(name,bytes,0,bytes.length);
            }
        };
        //加载class文件
        Class<?> aClass = classLoader.loadClass("Herosdemo.Treasure");
        //获取构造方法对象
        Constructor<?> declaredConstructor = aClass.getDeclaredConstructor();
        //设置可以访问私有方法
        declaredConstructor.setAccessible(true);
        //创建该类对象
        Object o = declaredConstructor.newInstance();
        //获取方法对象数组
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            //寻找有注解的方法并调用
            if(declaredMethod.getAnnotations().length>0){
                declaredMethod.invoke(o);
            }
        }
    }
}
