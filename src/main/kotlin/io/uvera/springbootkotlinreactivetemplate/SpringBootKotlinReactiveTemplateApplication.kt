package io.uvera.springbootkotlinreactivetemplate

import io.uvera.springbootkotlinreactivetemplate.common.properties.ConfigurationPropertiesMarker
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableWebFlux
@EnableReactiveMongoRepositories
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackageClasses = [ConfigurationPropertiesMarker::class])
class SpringBootKotlinReactiveTemplateApplication

fun main(args: Array<String>) {
    runApplication<SpringBootKotlinReactiveTemplateApplication>(*args)
}
