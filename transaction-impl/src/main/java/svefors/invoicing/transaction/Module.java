package svefors.invoicing.transaction;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

/**
 * Created by mats.svefors on 15/04/16.
 */
public class Module extends AbstractModule implements ServiceGuiceSupport {

    public Module() {
    }

    @Override
    protected void configure() {
        bindServices(serviceBinding(TransactionService.class, TransactionServiceImpl.class));
    }
}
