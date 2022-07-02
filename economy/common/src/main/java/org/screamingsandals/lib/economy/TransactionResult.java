package org.screamingsandals.lib.economy;

import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
public final class TransactionResult {
    private final double amount;
    private final double balance;
    private final boolean successful;
    @Nullable
    private final String errorMessage;
}
