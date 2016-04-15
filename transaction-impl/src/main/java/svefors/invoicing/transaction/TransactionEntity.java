package svefors.invoicing.transaction;

import akka.Done;
import com.lightbend.lagom.javadsl.immutable.ImmutableStyle;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import org.immutables.value.Value;

import java.util.Optional;

/**
 * Created by mats.svefors on 15/04/16.
 */
public class TransactionEntity extends PersistentEntity<TransactionEntity.Command, TransactionPosted, TransactionState> {

    @Override
    public Behavior initialBehavior(Optional<TransactionState> snapshotState) {
        BehaviorBuilder b = newBehaviorBuilder(
                snapshotState.orElse(
                        TransactionState.builder().transaction(Optional.empty()).build()
                )
        );

        b.setCommandHandler(PostTransaction.class, (cmd, ctx) -> {
            if (state().transaction().isPresent()) {
                ctx.invalidCommand("transaction " + entityId() + " is alread posted");
                return ctx.done();
            }
            Double transactionSum = cmd.transaction().entries()
                    .stream().map(Entry::amount).reduce(0.0, (aDouble, aDouble2) -> aDouble + aDouble2).doubleValue();
            if(transactionSum != 0.0){
                ctx.invalidCommand("transaction " + entityId() + " is unbalanced");
                return ctx.done();
            }
            final TransactionPosted postAdded =
                    TransactionPosted.builder()
                            .transaction(cmd.transaction())
                            .build();
            return ctx.thenPersist(postAdded, evt -> ctx.reply(Done.getInstance()));

        });

        b.setReadOnlyCommandHandler(GetTransaction.class, (cmd, ctx) -> {
            ctx.reply(state().transaction());
        });

        b.setEventHandler(TransactionPosted.class, evt ->
            state().withTransaction(evt.transaction())
        );
        return b.build();
    }

    public static interface Command{}

    @Value.Immutable
    @ImmutableStyle
    public static interface AbstractTransactionState{
        public Optional<Transaction> transaction();
    }

    @Value.Immutable
    @ImmutableStyle
    public static interface AbstractPostTransaction extends PersistentEntity.ReplyType<Done>, Command {
        public Transaction transaction();
    }

    @Value.Immutable
    @ImmutableStyle
    public static interface AbstractTransactionPosted extends Jsonable {
        public Transaction transaction();
    }

    @Value.Immutable
    @ImmutableStyle
    public static interface AbstractGetTransaction extends PersistentEntity.ReplyType<Optional<Transaction>>, Command {

    }


}
