package com.manji.elastic.api.controller.apidoc;
/**
 * swagger Ui 接口文档   上线时这个类 整个类注释掉
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

@ApiIgnore
@Controller
@Configuration
@EnableSwagger
public class ApiCon {
    
	@RequestMapping(value = "/apidoc")
	public String api() {
		String env = System.getProperty("spring.profiles.active");
		if(!"online".equals(env)){
			return "api";
		}else{
			return "sdsa";
		}
	}
    private SpringSwaggerConfig springSwaggerConfig;
    @Autowired
    public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig){
        this.springSwaggerConfig = springSwaggerConfig;
    }
    @Bean
    public SwaggerSpringMvcPlugin customImplementation(){
        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig).apiInfo(apiInfo());
    }
    private ApiInfo apiInfo(){
        ApiInfo apiInfo = new ApiInfo(
                "接口管理文档", 
                "开发过程中API随时都有可能变化，请密切关注本文档"
                + "<br/>SYSTEM_ERROR  系统异常，后端代码抛异常了，直接联系后端开发人员即可",
                null, 
                null,
                null,
                null);
        return apiInfo;
    }
}