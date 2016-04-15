package svefors.invoicing.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.immutable.ImmutableStyle;
import org.immutables.value.Value;


import java.math.BigDecimal;
import java.util.List;

@Value.Immutable
@ImmutableStyle
@JsonDeserialize
public interface AbstractTransaction {

    public String transactionId();

    public List<Entry> entries();

    @Value.Immutable
    @JsonDeserialize
    @ImmutableStyle
    public interface AbstractEntry {
        public Double amount();
        public String accountId();

    }


}
