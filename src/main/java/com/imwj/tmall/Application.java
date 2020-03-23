package com.imwj.tmall;

import com.imwj.tmall.util.PortUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author langao_q
 * @create 2019-11-19 10:30
 */
@EnableCaching
@SpringBootApplication
@EnableElasticsearchRepositories(basePackages="com.imwj.tmall.es")
@EnableJpaRepositories(basePackages={"com.imwj.tmall.dao", "com.imwj.tmall.pojo"})
public class Application {

    static {
        PortUtil.checkPort(6379,"Redis 服务端",true);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
