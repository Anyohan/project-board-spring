package com.spring.projectboardspring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class jpaConfig{
    // jpa auditing 을 이용하여 속성들을 자동생성 할때 권한자를 할당해주시위함
    @Bean
    public AuditorAware<String> auditorAware(){
        return() -> Optional.of("yohan"); //TODO: 스프링 시큐리티로 인증 기능을 붙이게 될 때 , 수정하자
    }
}
