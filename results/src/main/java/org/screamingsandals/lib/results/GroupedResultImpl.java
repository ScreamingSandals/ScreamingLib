package org.screamingsandals.lib.results;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

class GroupedResultImpl implements GroupedResult {
    private final BlockingQueue<SimpleResult> results = new LinkedBlockingQueue<>();

    @Override
    public Result.Status getStatus() {
        final var failCount = results.stream()
                .filter(result -> result.getStatus() == Result.Status.FAIL)
                .count();
        final var unknownCount = results.stream()
                .filter(result -> result.getStatus() == Result.Status.UNKNOWN)
                .count();

        if (failCount == 0 && unknownCount == 0) {
            return Result.Status.OK;
        }

        if (failCount > 0) {
            return Result.Status.FAIL;
        }

        return Result.Status.UNKNOWN;
    }

    @Override
    public boolean isOk() {
        return getStatus() == Result.Status.OK;
    }

    @Override
    public boolean isFail() {
        return getStatus() == Result.Status.FAIL;
    }

    @Override
    public List<String> getMessages() {
        return results.stream()
                .map(BaseResult::getMessage)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<SimpleResult> getResults() {
        return List.copyOf(results);
    }

    @Override
    public GroupedResult from(SimpleResult simpleResult) {
        results.offer(simpleResult);
        return this;
    }

    @Override
    public GroupedResult ok() {
        results.offer(SimpleResult.ok());
        return this;
    }

    @Override
    public GroupedResult fail(String message) {
        results.offer(SimpleResult.fail(message));
        return this;
    }

    @Override
    public GroupedResult fail(String message, Throwable throwable) {
        results.offer(SimpleResult.fail(message, throwable));
        return this;
    }

    @Override
    public GroupedResult unknown(String message) {
        results.offer(SimpleResult.unknown(message));
        return this;
    }

    @Override
    public GroupedResult merge(GroupedResult result) {
        results.addAll(result.getResults());
        return this;
    }

    @Override
    public SimpleResult toSimple() {
        if (isOk()) {
            return SimpleResult.ok();
        }

        if (isFail()) {
            final var builder = new StringBuilder();
            results.forEach(result -> {
                if (result.isFail()) {
                    builder.append(result.getMessage());
                    builder.append(System.getProperty("line.separator"));
                }
            });
            return SimpleResult.fail(builder.toString());
        }

        return SimpleResult.unknown();
    }

    @Override
    public <T> DataResult<T> toData() {
        return toSimple().toData();
    }
}

