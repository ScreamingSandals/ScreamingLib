package org.screamingsandals.lib.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class DefaultConfigBuilder {
	
	private final ConfigAdapter adapter;
	private final String path;
	private final AtomicBoolean modify;
	
	public static DefaultConfigBuilder start(ConfigAdapter adapter) {
		return new DefaultConfigBuilder(adapter, "", new AtomicBoolean());
	}
	
	public DefaultConfigBuilder put(String path, Serializable defaultValue) {
		var fullPath = path;
		if (!this.path.isEmpty()) {
			fullPath = this.path + "." + fullPath;
		}
		adapter.checkOrSet(modify, fullPath, defaultValue);
		return this;
	}
	
	public DefaultConfigBuilder open(String path, Consumer<DefaultConfigBuilder> callback) {
		var fullPath = path;
		if (!this.path.isEmpty()) {
			fullPath = this.path + "." + fullPath;
		}
		callback.accept(new DefaultConfigBuilder(adapter, fullPath, modify));
		return this;
	}
	
	public void end() {
		if (this.path.isEmpty() && modify.get()) {
			adapter.save();
			adapter.load();
		}
	}
}
