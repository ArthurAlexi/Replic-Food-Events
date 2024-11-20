package br.com.replicfoodevents.configurations

import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import java.io.IOException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.jvm.Throws

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
class CorsFilter: Filter {
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(req: ServletRequest?, res: ServletResponse?, chain: FilterChain?) {
        val response = res as HttpServletResponse
        response.setHeader("Acess-Control-Allow-Origin", "*")
        response.setHeader("Acess-Control-Allow-Credentials", "*")
        response.setHeader("Acess-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE")
        response.setHeader("Acess-Control-Max-Age", "3600")
        response.setHeader("Acess-Control-Max-Headers", "X-Requested-With, Authorization, Origin, Content-Type, Version")
        response.setHeader("Acess-Control-Expose-Headers", "X-Requested-With, Authorization, Origin, Content-Type")

        val request = req as HttpServletRequest
        if(request.method != "OPTIONS"){
            chain?.doFilter(req, res)
        }else{

        }
    }

    override fun destroy() {
        super.destroy()
    }
    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig?) {
        super.init(filterConfig)
    }


}