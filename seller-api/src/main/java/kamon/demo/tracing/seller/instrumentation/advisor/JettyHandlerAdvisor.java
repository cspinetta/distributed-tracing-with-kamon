package kamon.demo.tracing.seller.instrumentation.advisor;

import javax.servlet.Filter;
import kamon.demo.tracing.seller.instrumentation.KamonFilterForSpark;
import kanela.agent.libs.net.bytebuddy.asm.Advice;

public class JettyHandlerAdvisor {

    @Advice.OnMethodEnter()
    public static void start(@Advice.Argument(value = 0, readOnly = false) Filter filter) {
        Filter oldFilter = filter;
        filter = new KamonFilterForSpark(oldFilter);
    }

}
