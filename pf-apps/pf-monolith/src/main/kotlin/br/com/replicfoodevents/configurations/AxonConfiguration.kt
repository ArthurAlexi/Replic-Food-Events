package br.com.replicfoodevents.configurations

import org.axonframework.commandhandling.CommandBus
import org.axonframework.messaging.interceptors.BeanValidationInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.axonframework.spring.eventsourcing.SpringAggregateSnapshotterFactoryBean

@Configuration
class AxonConfiguration {
    @Autowired
    fun registerInterceptors(commandBus: CommandBus){
        commandBus.registerDispatchInterceptor(BeanValidationInterceptor())
    }
    @Bean
    open fun snapshotterFactoryBean() = SpringAggregateSnapshotterFactoryBean()
}