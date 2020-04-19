package org.screamingsandals.lib.update_checker;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.function.Consumer;

import org.yaml.snakeyaml.Yaml;

public class CheckUpdate {
	private static final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

	public static void checkUpdate(int resourceId, Consumer<Result> consumer) {
		try {
			var request = HttpRequest
				.newBuilder(URI.create(String.format("https://api.spiget.org/v2/resources/%s/versions/latest?ut=%s",
					resourceId, System.currentTimeMillis())))

				.GET().setHeader("User-Agent", "SpigetResourceUpdater").build();

			httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(response -> {
				var body = response.body();
				var yml = new Yaml();
				Map<String, Object> data = yml.load(body);
				consumer.accept(new Result(data.get("name").toString()));
			});
		} catch (Exception ignored) {
		}
	}
}
