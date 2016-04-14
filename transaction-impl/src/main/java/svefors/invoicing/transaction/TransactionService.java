package svefors.invoicing.transaction;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.*;

import static com.lightbend.lagom.javadsl.api.Service.*;

public interface TransactionService extends Service{

    ServiceCall<NotUsed, Transaction, NotUsed> postTransaction();

    default Descriptor descriptor() {
        return named("transaction").with(
                call(postTransaction())
        );
    }
}
