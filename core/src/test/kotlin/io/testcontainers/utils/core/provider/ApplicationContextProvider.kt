package io.testcontainers.utils.core.provider

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
class ApplicationContextProvider : ApplicationContextAware {
    
    companion object {
        private var applicationContext: ApplicationContext? = null
        
        fun getApplicationContext(): ApplicationContext {
            return applicationContext ?: throw IllegalStateException("ApplicationContext is not initialized")
        }
    }
    
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        Companion.applicationContext = applicationContext
    }
}
