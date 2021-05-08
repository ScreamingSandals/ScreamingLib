package org.screamingsandals.lib.results;

import java.util.List;

public interface GroupedResult {

    static GroupedResult get() {
        return new GroupedResultImpl();
    }

    Result.Status getStatus();

    boolean isOk();

    boolean isFail();

    List<String> getMessages();

    List<SimpleResult> getResults();

    GroupedResult from(SimpleResult simpleResult);

    GroupedResult ok();

    GroupedResult fail(String message);

    GroupedResult fail(String message, Throwable throwable);

    GroupedResult unknown(String message);

    GroupedResult merge(GroupedResult result);

    SimpleResult toSimple();

    <T> DataResult<T> toData();
}

