package io.rayd.backend

import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.io.IOException

class ClientAppPathResourceResolver : org.springframework.web.servlet.resource.PathResourceResolver() {

    @Throws(IOException::class)
    override fun getResource(resourcePath: String, location: Resource): Resource? {
        val requestedResource = location.createRelative(resourcePath)
        return if (requestedResource.exists() && requestedResource.isReadable) {
            requestedResource
        } else if (location is ClassPathResource && location.path.equals("static/", ignoreCase = true)) {
            ClassPathResource("/static/index.html")
        } else {
            null
        }
    }
}

@Configuration
class ResourceHandlerConfigurer : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {

        registry.addResourceHandler("/**/*")
            .addResourceLocations("classpath:/META-INF/resources/")
            .addResourceLocations("classpath:/static/")
            .resourceChain(true)
            .addResolver(ClientAppPathResourceResolver())
    }
}
