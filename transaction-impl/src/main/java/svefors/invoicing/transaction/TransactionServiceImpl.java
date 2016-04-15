package svefors.invoicing.transaction;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.NotFound;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;

import javax.inject.Inject;
import java.util.Optional;

public class TransactionServiceImpl implements TransactionService {

    private final PersistentEntityRegistry persistentEntities;

    @Inject
    public TransactionServiceImpl(PersistentEntityRegistry persistentEntities) {
        this.persistentEntities = persistentEntities;
        persistentEntities.register(TransactionEntity.class);
    }

    @Override
    public ServiceCall<NotUsed, Transaction, NotUsed> postTransaction() {

        return (notUsed, transaction) ->
                ref(transaction.transactionId()).ask(PostTransaction.builder().transaction(transaction).build())
                        .thenApply(ack -> NotUsed.getInstance());

    }

    private PersistentEntityRef<TransactionEntity.Command> ref(String transactionId) {
        return persistentEntities.refFor(
                TransactionEntity.class,
                transactionId
        );
    }

    @Override
    public ServiceCall<String, NotUsed, Transaction> getTransaction() {
        return (transactionId, notUsed) ->
                ref(transactionId).<Optional<Transaction>, GetTransaction>ask(
                        GetTransaction.builder().build()
                ).thenApply(
                        transaction -> {
                            if (transaction.isPresent())
                                return transaction.get();
                            else
                                throw new NotFound(String.format("Transaction id: %s not found", transactionId));

                        })
                ;
    }

}
