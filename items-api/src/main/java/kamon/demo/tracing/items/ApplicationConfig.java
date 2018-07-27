package kamon.demo.tracing.items;

import static com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"kamon.demo.tracing.items"})
public class ApplicationConfig {

    public class TypesafeConfigPropertySource extends PropertySource<Config> {
        public TypesafeConfigPropertySource(String name, Config source) {
            super(name, source);
        }

        @Override
        public Object getProperty(String path) {
            if (path.contains("["))
                return null;
            else if (path.contains(":"))
                return null;
            else if (source.hasPath(path))
                return source.getAnyRef(path);
            else return null;
        }
    }

    @Bean("typeSafeConfig")
    public Config provideTypeSafeConfig() {
        return ConfigFactory.load().resolve();
    }

    @Bean
    @DependsOn("typeSafeConfig")
    public TypesafeConfigPropertySource provideTypesafeConfigPropertySource(Config typeSafeConfig, ConfigurableEnvironment env) {
        TypesafeConfigPropertySource source = new TypesafeConfigPropertySource("typeSafe", typeSafeConfig);
        MutablePropertySources sources = env.getPropertySources();
        sources.addFirst(source); // Choose if you want it first or last
        return source;

    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder()
            .modules(new Jdk8Module(), new JavaTimeModule())
            .propertyNamingStrategy(SNAKE_CASE)
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}
