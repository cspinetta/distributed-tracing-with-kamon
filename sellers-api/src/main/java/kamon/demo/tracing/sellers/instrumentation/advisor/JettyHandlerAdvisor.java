package kamon.demo.tracing.sellers.instrumentation.advisor;

import javax.servlet.Filter;
import kamon.demo.tracing.sellers.instrumentation.KamonFilterForSpark;
import kanela.agent.libs.net.bytebuddy.asm.Advice;

public class JettyHandlerAdvisor {

    @Advice.OnMethodEnter()
    public static void start(@Advice.Argument(value = 0, readOnly = false) Filter filter) {
        Filter oldFilter = filter;
        filter = new KamonFilterForSpark(oldFilter);
    }

}
