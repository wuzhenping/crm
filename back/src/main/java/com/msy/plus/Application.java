package com.msy.plus;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

import static com.msy.plus.core.constant.ProjectConstant.FILTER_PACKAGE;
import static com.msy.plus.core.constant.ProjectConstant.MAPPER_PACKAGE;

/**
 * 主程序
 *
 * @author MoShuying
 * @date 2018/05/27
 */
@EnableCaching
@SpringBootApplication
@EnableEncryptableProperties
@EnableTransactionManagement
@MapperScan(basePackages = MAPPER_PACKAGE)
@ServletComponentScan(basePackages = FILTER_PACKAGE)
public class Application extends SpringBootServletInitializer {

  public static void main(final String[] args) {
    SpringApplication.run(Application.class, args);
  }

  /** 容器启动配置 */
  @Override
  protected SpringApplicationBuilder configure(final SpringApplicationBuilder builder) {
    return builder.sources(Application.class);
  }
}
