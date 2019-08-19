package br.com.alura.forum.config.swagger;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.alura.forum.model.Usuario;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfigurations {

	
	@Bean
	public Docket forumApi() {
		return new Docket(DocumentationType.SWAGGER_2) //tipo do documento
				.select()
				.apis(RequestHandlerSelectors.basePackage("br.com.alura.forum")) //a partir de qual pacote começas a ler as classes do projeto
				.paths(PathSelectors.ant("/**")) // quais endpoints é pra o springfox realizar analise
				.build()
				.ignoredParameterTypes(Usuario.class) //ignorar a classe usuario para que nao sejam exibidos os dados de usuario como a senha
				.globalOperationParameters(Arrays.asList( //add parametros globais
						new ParameterBuilder()
						.name("Authorization") //nome do parametro
						.description("Header para token JWT") //descricao do parametro
						.modelRef(new ModelRef("string")) // tipo do parametro 
						.parameterType("header") //tipo de parametro
						.required(false)//opcional
						.build()));
	}
	
}
