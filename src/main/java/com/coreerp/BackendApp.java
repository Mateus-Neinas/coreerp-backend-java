package com.coreerp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public final class BackendApp {
    private static final ObjectMapper JSON = new ObjectMapper();
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final SummaryService SUMMARIES = new SummaryService();
    private static final String API = System.getenv().getOrDefault("COREERP_API_URL", "http://localhost:3000");

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8082"));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/health", exchange -> respond(exchange, 200, "{\"status\":\"ok\",\"service\":\"coreerp-backend-java\"}"));
        server.createContext("/reports/summary", exchange -> {
            try {
                int customers = count("/customers");
                int products = count("/products");
                int orders = count("/orders");
                respond(exchange, 200, JSON.writeValueAsString(SUMMARIES.summarize(customers, products, orders)));
            } catch (Exception exception) {
                respond(exchange, 502, "{\"error\":\"core_api_unavailable\"}");
            }
        });
        server.start();
        System.out.println("CoreERP Java backend listening on :" + port);
    }

    private static int count(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(API + path)).GET().build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) throw new IOException("Upstream failed");
        JsonNode root = JSON.readTree(response.body());
        JsonNode data = root.path("data");
        if (!data.isArray()) throw new IOException("Unexpected API response");
        return data.size();
    }

    private static void respond(com.sun.net.httpserver.HttpExchange exchange, int status, String body) throws IOException {
        byte[] payload = body.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, payload.length);
        try (var output = exchange.getResponseBody()) { output.write(payload); }
    }
}
