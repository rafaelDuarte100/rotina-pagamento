package br.com.pagamento.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static springfox.documentation.builders.PathSelectors.any;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select()
                .apis(basePackage("br.com.pagamento"))
                .paths(any())
                .build()
                .useDefaultResponseMessages(false);
    }

    protected ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Rotina de Pagamento")
                .description("Api simples para simulação de rotinas de pagamentos de uma processadora de crédito.")
                .version("1.0")
                .build();
    }
}
