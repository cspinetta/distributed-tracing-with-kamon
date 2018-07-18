package kamon.demo.tracing.seller.instrumentation

import kamon.servlet.v3.KamonFilterV3
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

class KamonFilterForSpark(private val nextFilter: Filter) : KamonFilterV3() {

    override fun doFilter(request: ServletRequest, response: ServletResponse,
                          filterChain: FilterChain?) {
        super.doFilter(request, response) { req, res -> nextFilter.doFilter(req, res, filterChain) }
    }
}