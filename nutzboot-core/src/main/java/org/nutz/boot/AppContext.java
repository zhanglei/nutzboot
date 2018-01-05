package org.nutz.boot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.nutz.boot.config.ConfigureLoader;
import org.nutz.boot.env.EnvHolder;
import org.nutz.boot.resource.ResourceLoader;
import org.nutz.boot.starter.ServerFace;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.combo.ComboIocLoader;
import org.nutz.lang.util.LifeCycle;

/**
 * 全局上下文
 * 
 * @author wendal(wendal1985@gmail.com)
 *
 */
public class AppContext implements LifeCycle {

    protected static AppContext _default = new AppContext();

    /**
     * Ioc容器
     */
    protected Ioc ioc;
    /**
     * ClassLoader
     */
    protected ClassLoader classLoader;
    /**
     * 配置加载器
     */
    protected ConfigureLoader configureLoader;
    /**
     * 资源加载器
     */
    protected ResourceLoader resourceLoader;
    /**
     * 环境信息加载器
     */
    protected EnvHolder envHolder;
    /**
     * Ioc容器的IocLoader引用
     */
    protected ComboIocLoader comboIocLoader;
    /**
     * 主类
     */
    protected Class<?> mainClass;

    /**
     * 主扫描路径
     */
    protected String mainPackage;

    /**
     * 一个Starter列表
     */
    protected List<Object> starters = new ArrayList<>();

    /**
     * 获取Ioc容器
     * 
     * @return Ioc容器
     */
    public Ioc ioc() {
        return ioc;
    }

    /**
     * 获取Ioc容器
     * 
     * @return Ioc容器
     */
    public Ioc getIoc() {
        return ioc;
    }

    /**
     * 设置Ioc容器
     * 
     * @param ioc
     *            Ioc容器
     */
    public void setIoc(Ioc ioc) {
        this.ioc = ioc;
    }

    /**
     * 设置ClassLoader
     * 
     * @param classLoader
     *            待设置的classLoader,不可以是null
     */
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * 获取ClassLoader
     * 
     * @return 当前ClassLoader
     */
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * 获取配置加载器
     * 
     * @return
     */
    public ConfigureLoader getConfigureLoader() {
        return configureLoader;
    }

    /**
     * 获取Nutz中最常用的PropertiesProxy对象
     * 
     * @return 配置信息对象
     */
    public PropertiesProxy getConf() {
        return configureLoader.get();
    }

    /**
     * 获取资源加载器
     * 
     * @return 资源加载器
     */
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    /**
     * 获取环境加载器
     * 
     * @return 环境加载器
     */
    public EnvHolder getEnvHolder() {
        return envHolder;
    }

    /**
     * 设置环境加载器
     * 
     * @param envHolder
     *            环境加载器
     */
    public void setEnvHolder(EnvHolder envHolder) {
        this.envHolder = envHolder;
    }

    /**
     * 设置资源加载器
     * 
     * @param resourceLoader
     *            资源加载器
     */
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * 设置配置信息加载器
     * 
     * @param configureLoader
     */
    public void setConfigureLoader(ConfigureLoader configureLoader) {
        this.configureLoader = configureLoader;
    }

    /**
     * 获取缺省的AppContext实例
     * 
     * @return 缺省的AppContext实例
     */
    public static AppContext getDefault() {
        return _default;
    }

    /**
     * 设置缺省的AppContext实例
     * 
     * @param ctx
     *            缺省的AppContext实例
     */
    public static void setDefault(AppContext ctx) {
        _default = ctx;
    }

    /**
     * 设置Ioc容器的ComboIocLoader
     * 
     * @param comboIocLoader
     *            Ioc容器的ComboIocLoader
     */
    public void setComboIocLoader(ComboIocLoader comboIocLoader) {
        this.comboIocLoader = comboIocLoader;
    }

    /**
     * 获取Ioc容器的ComboIocLoader
     * 
     * @return Ioc容器的ComboIocLoader
     */
    public ComboIocLoader getComboIocLoader() {
        return comboIocLoader;
    }

    /**
     * 获取Starter列表
     * 
     * @return Starter列表
     */
    public List<Object> getStarters() {
        return starters;
    }

    /**
     * 添加一个Starter
     */
    public void addStarter(Object obj) {
        starters.add(obj);
    }

    /**
     * 获取主类,很多配置信息都在这个类上
     * 
     * @return 主类
     */
    public Class<?> getMainClass() {
        return mainClass;
    }

    /**
     * 设置主类
     * 
     * @param mainClass
     *            主类不可以是null
     */
    public void setMainClass(Class<?> mainClass) {
        this.mainClass = mainClass;
    }

    /**
     * 设置主package,这是扫描路径的基础
     * 
     * @param mainPackage
     *            主package
     */
    public void setMainPackage(String mainPackage) {
        this.mainPackage = mainPackage;
    }

    /**
     * 获取主package
     * 
     * @return 主package
     */
    public String getPackage() {
        return mainPackage == null ? this.mainClass.getPackage().getName() : mainPackage;
    }

    /**
     * 循环调用starter的init方法
     */
    public void init() throws Exception {
        for (Object object : starters) {
            if (object instanceof LifeCycle)
                ((LifeCycle) object).init();
        }
    }

    public void fetch() throws Exception {}

    /**
     * 循环调用starter的depose方法,如果有的话,然后销毁ioc容器
     */
    public void depose() throws Exception {
        for (Object object : starters) {
            if (object instanceof LifeCycle)
                ((LifeCycle) object).depose();
        }
        if (ioc != null)
            ioc.depose();
    }

    /**
     * 循环调用ServerFace的start方法,通常是一个starter
     */
    public void startServers() throws Exception {
        for (ServerFace face : getBeans(ServerFace.class))
            face.start();
    }

    /**
     * 循环调用ServerFace的start方法,通常是一个starter
     */
    public void stopServers() throws Exception {
        for (ServerFace face : getBeans(ServerFace.class))
            face.stop();
    }

    /**
     * 根据Class获取指定的Ioc Bean列表
     * 
     * @param klass
     *            需要指定的Class类
     * @return 符合Class类的Ioc Bean列表
     */
    public <T> List<T> getBeans(Class<T> klass) {
        List<T> list = new ArrayList<>();
        for (String name : getIoc().getNamesByType(klass)) {
            if (name == null)
                continue;
            list.add(getIoc().get(klass, name));
        }
        return list;
    }
    
    public int getServerPort(String legacyKey) {
        if (legacyKey != null && getConf().has(legacyKey)) {
            return getConf().getInt(legacyKey);
        }
        int port = getConf().getInt("server.port", 8080);
        if (port == 0) {
            port = new Random(System.currentTimeMillis()).nextInt(1000) + 8000;
            getConf().set("server.port", ""+port);
        }
        return port;
    }
}
