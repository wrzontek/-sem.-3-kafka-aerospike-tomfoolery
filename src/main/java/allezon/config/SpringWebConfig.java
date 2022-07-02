package allezon.config;

import allezon.avro2json.AvroJsonHttpMessageConverter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class SpringWebConfig implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
        converters.add(0, new AvroJsonHttpMessageConverter());
        converters.add(1, new MappingJackson2HttpMessageConverter());
    }

    @Bean
    public RestTemplate restTemplate(final RestTemplateBuilder builder) {
        final RestTemplate restTemplate = builder.build();
        restTemplate.getMessageConverters().add(0, new AvroJsonHttpMessageConverter());
        restTemplate.getMessageConverters().add(1, new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }

}

