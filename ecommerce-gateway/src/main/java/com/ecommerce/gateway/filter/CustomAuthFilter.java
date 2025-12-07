package com.ecommerce.gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class CustomAuthFilter extends AbstractGatewayFilterFactory<CustomAuthFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    @Value("${auth-service.url}")
    private String authServiceUrl;

    public CustomAuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    public static class Config {
        // Configuración si es necesaria
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // Rutas públicas que no requieren autenticación (ej: /auth/login, /auth/register, /productos GET)
            List<String> publicPaths = List.of("/auth/login", "/auth/register", "/productos");
            String path = request.getURI().getPath().replace("/api", "");

            boolean isPublic = publicPaths.stream().anyMatch(path::contains);
            boolean isGetProducts = request.getMethod().matches("GET") && path.matches("/productos(/.*)?");

            if (isPublic || isGetProducts) {
                return chain.filter(exchange);
            }

            // 1. Verificar si el token está presente
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return this.onError(exchange, "Token de autorización no encontrado", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String token = authHeader.replace("Bearer ", "");

            // 2. Llamar al Auth Service para validar el token
            return webClientBuilder.build()
                    .get()
                    .uri(authServiceUrl + "?token=" + token)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .flatMap(response -> {
                        if (response.get("isValid").equals(true)) {
                            // 3. Inyectar headers con información del usuario
                            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                                    .header("X-User-ID", response.get("userId").toString())
                                    .header("X-User-Email", response.get("email").toString())
                                    .header("X-User-Role", response.get("rol").toString())
                                    .build();

                            // 4. Verificar autorización (ejemplo para rutas /admin)
                            String rol = response.get("rol").toString();
                            if (path.contains("/admin") && !rol.equals("ADMIN")) {
                                return this.onError(exchange, "Acceso denegado. Rol insuficiente.", HttpStatus.FORBIDDEN);
                            }

                            return chain.filter(exchange.mutate().request(modifiedRequest).build());
                        }
                        return this.onError(exchange, "Token inválido o expirado", HttpStatus.UNAUTHORIZED);
                    })
                    .onErrorResume(e -> this.onError(exchange, "Error de comunicación con Auth Service: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}
