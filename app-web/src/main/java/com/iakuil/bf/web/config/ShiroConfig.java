package com.iakuil.bf.web.config;

import com.iakuil.bf.web.security.CustomCredentialsMatcher;
import com.iakuil.bf.web.security.CustomShiroFilter;
import com.iakuil.bf.web.security.JdbcRealm;
import com.iakuil.bf.web.security.SessionService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.*;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.*;

/**
 * Shiro配置
 *
 * @author Kai
 */
@Configuration
public class ShiroConfig {

    @Value("${spring.redis.host:127.0.0.1}")
    private String host;

    @Value("${spring.redis.port:6379}")
    private String port;

    @Value("${spring.redis.password:#{null}}")
    private String password;

    @Value("${spring.redis.cluster:#{null}}")
    private String[] cluster;

    /**
     * Shiro过滤器配置
     */
    @Bean
    ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager());
        Map<String, String> urlMap = new LinkedHashMap<>();

        // static resources
        urlMap.put("/*.txt", "anon");
        urlMap.put("/favicon.ico", "anon");
        urlMap.put("/js/**", "anon");
        urlMap.put("/css/**", "anon");
        urlMap.put("/fonts/**", "anon");
        urlMap.put("/lib/**", "anon");
        urlMap.put("/images/**", "anon");

        // swagger
        urlMap.put("/swagger**/**", "anon");
        urlMap.put("/webjars/**", "anon");
        urlMap.put("/v2/api-docs", "anon");
        urlMap.put("/doc.html", "anon");

        // actuator
        urlMap.put("/actuator/**", "anon");
        urlMap.put("/web/**", "anon");
        urlMap.put("/health/**", "anon");

        // auth and token
        urlMap.put("/api/auth/**", "anon");
        urlMap.put("/api/reg/**", "anon");
        urlMap.put("/api/token/**", "anon");

        // demo
        urlMap.put("/demo/login", "anon");
        urlMap.put("/demo/index", "anon");

        // other
        urlMap.put("/**", "authc");

        bean.setFilterChainDefinitionMap(urlMap);
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("authc", new CustomShiroFilter());
        bean.setFilters(filterMap);
        return bean;
    }

    /**
     * 安全管理器
     */
    @Bean(name = "securityManager")
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 先配置认证策略
        securityManager.setAuthenticator(authenticator());
        // 再配置Realm
        securityManager.setRealms(Collections.singletonList(jdbcRealm()));
        // 配置Redis缓存管理器
        securityManager.setCacheManager(cacheManager());
        // 配置自定义Session管理器
        securityManager.setSessionManager(sessionManager());
        // 配置“记住我”Session管理器（注意和上面区分）
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    /**
     * 自定义JDBC Realm
     */
    @Bean
    JdbcRealm jdbcRealm() {
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setCredentialsMatcher(customCredentialsMatcher());
        return jdbcRealm;
    }

    /**
     * 自定义密码匹配器
     */
    @Bean("credentialsMatcher")
    public CustomCredentialsMatcher customCredentialsMatcher() {
        return new CustomCredentialsMatcher();
    }

    /**
     * 配置认证策略
     */
    @Bean
    ModularRealmAuthenticator authenticator() {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(authenticationStrategy());
        return authenticator;
    }

    /**
     * 认证策略：第一个Realm认证通过
     */
    @Bean
    FirstSuccessfulStrategy authenticationStrategy() {
        return new FirstSuccessfulStrategy();
    }

    /**
     * 缓存管理器，使用Redis实现
     */
    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheManager cacheManager = new RedisCacheManager();
        cacheManager.setRedisManager(redisManager());
        // 针对不同用户缓存，默认是id
        cacheManager.setPrincipalIdFieldName("id");
        // 用户权限信息缓存时间（秒），默认30分钟
        cacheManager.setExpire(60 * 30);
        return cacheManager;
    }

    /**
     * 用户密码输入错误计数缓存管理器
     */
    @Bean("lockedAccountManager")
    public RedisCacheManager lockedAccountManager() {
        RedisCacheManager cacheManager = new RedisCacheManager();
        cacheManager.setRedisManager(redisManager());
        // 用户密码输入错误计数缓存时间（秒），5分钟
        cacheManager.setExpire(60 * 5);
        return cacheManager;
    }

    /**
     * Redis管理器，使用crazycake实现
     */
    @Bean
    public IRedisManager redisManager() {
        if (cluster == null) {
            RedisManager manager = new RedisManager();
            manager.setHost(host + ":" + port);
            manager.setPassword(password);
            return manager;
        } else {
            RedisClusterManager manager = new RedisClusterManager();
            manager.setHost(StringUtils.join(cluster, ","));
            manager.setPassword(password);
            return manager;
        }
    }

    /**
     * 会话管理器
     */
    @Bean("sessionManager")
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionDAO(sessionDAO());
        sessionManager.setCacheManager(cacheManager());

        // 全局会话超时时间（单位毫秒），默认30分钟
        sessionManager.setGlobalSessionTimeout(1000 * 60 * 30);
        // 是否开启删除无效的session对象，默认为true
        sessionManager.setDeleteInvalidSessions(true);
        // 是否开启定时调度器进行检测过期session，默认为true
        sessionManager.setSessionValidationSchedulerEnabled(true);
        // 设置session失效的扫描时间, 清理用户直接关闭浏览器造成的孤立会话，默认为 1个小时
        // 设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
        sessionManager.setSessionValidationInterval(1000 * 60 * 60);
        // 取消url后面的JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    /**
     * Session DAO，使用crazycake实现
     */
    @Bean
    public SessionDAO sessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        redisSessionDAO.setSessionIdGenerator(sessionIdGenerator());
        // Session在Redis中的保存时间，单位：秒，最好大于Session会话超时时间
        redisSessionDAO.setExpire(60 * 60);
        return redisSessionDAO;
    }

    /**
     * 自定义的Session Service，将SessionDAO又封装了一遍
     */
    @Bean
    public SessionService sessionService() {
        return new SessionService(sessionDAO());
    }

    /**
     * 配置Session ID生成器
     */
    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * 普通Cookie属性设置
     */
    @Bean("sessionIdCookie")
    public SimpleCookie sessionIdCookie() {
        // sessionId默认为JSESSIONID，与SERVLET容器名冲突，重新定义为sid
        SimpleCookie simpleCookie = new SimpleCookie("sid");
        // setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：
        // setcookie()的第七个参数
        // 设为true后，只能通过http访问，javascript无法访问
        // 防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        // maxAge=-1表示浏览器关闭时失效此Cookie
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    /**
     * “记住我”管理器
     */
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        cookieRememberMeManager.setCipherKey(Base64.getDecoder().decode("zSyK5Kp6PZAAjlT+eeNMlg=="));
        return cookieRememberMeManager;
    }

    /**
     * “记住我”Cookie属性设置
     */
    public SimpleCookie rememberMeCookie() {
        SimpleCookie cookie = new SimpleCookie("rememberMe");
//        cookie.setDomain(domain);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        // Cookie生效时间30天，单位秒
        cookie.setMaxAge(30 * 24 * 60 * 60);
        return cookie;
    }

    /**
     * Advistor创建器
     */
    @Bean("defaultAdvisorAutoProxyCreator")
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 指定强制使用cglib为action创建代理对象
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * 生命周期管理器
     */
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启Shiro注解通知器
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }
}