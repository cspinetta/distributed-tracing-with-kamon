package kamon.demo.tracing.sellers.instrumentation

import kamon.demo.tracing.sellers.instrumentation.advisor.JettyHandlerAdvisor
import kanela.agent.kotlin.KanelaInstrumentation


class JettyInstrumentation : KanelaInstrumentation() {
    init {
        forTargetType({ "spark.embeddedserver.jetty.JettyHandler" }, { builder ->
            builder
                    .withAdvisorFor(isConstructor) { JettyHandlerAdvisor::class.java }
                    .build()
        })
    }
}

//class JettyHandlerAdvisor2 {
//    companion object Obj {
//
//        @Advice.OnMethodEnter()
//        @JvmStatic
//        fun start(@Advice.AllArguments(readOnly = false) args: Array<Any>) = run {
//            val filter = args[0] as Filter
//            args[0] = KamonFilterForSpark(filter)
//            println("...................................................")
////            val filterHolder = FilterHolder(KamonFilterV3::class.java)
////            val servlet = servletHandler as ServletHandler
////            servlet.addFilterWithMapping(filterHolder,"/*", EnumSet.of(DispatcherType.REQUEST))
//        }
//    }
//}