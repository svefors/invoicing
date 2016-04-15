package svefors.invoicing.transaction;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.*;
import com.lightbend.lagom.javadsl.api.transport.Method;

import static com.lightbend.lagom.javadsl.api.Service.*;

public interface TransactionService extends Service{

    ServiceCall<NotUsed, Transaction, NotUsed> postTransaction();

    ServiceCall<String, NotUsed, Transaction> getTransaction();


    default Descriptor descriptor() {
        return named("transaction").with(
                restCall(Method.POST, "/transaction/", postTransaction()),
                restCall(Method.GET, "/transaction/:transactionId", getTransaction())
        );
    }
}
