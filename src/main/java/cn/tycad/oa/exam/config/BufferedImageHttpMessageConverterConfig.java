package cn.tycad.oa.exam.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;

/**
 * @Author: YY
 * @Date: 2019/3/25
 * @Description:
 */
@SpringBootConfiguration
public class BufferedImageHttpMessageConverterConfig {
    @Bean
    public BufferedImageHttpMessageConverter bufferedImageHttpMessageConverter(){
        return new BufferedImageHttpMessageConverter();
    }
}
