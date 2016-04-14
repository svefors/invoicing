package svefors.invoicing.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;

import javax.annotation.concurrent.Immutable;
import java.math.BigDecimal;
import java.util.List;

@Immutable
@JsonDeserialize
public class Transaction {

    private final String transactionId;

    private final List<Entry> entries;

    public Transaction(String transactionId, List<Entry> entries) {
        this.transactionId = Preconditions.checkNotNull(transactionId, "transactionId");
        this.entries = entries;
    }

    @Immutable
    @JsonDeserialize
    public class Entry {
        private final BigDecimal amount;
        private final String accountId;

        @JsonCreator
        public Entry(BigDecimal amount, String accountId) {
            this.amount = Preconditions.checkNotNull(amount, "accountId");
            this.accountId = Preconditions.checkNotNull(accountId, "accountId");
        }
    }


}
