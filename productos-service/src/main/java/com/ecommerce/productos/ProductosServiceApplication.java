package com.ecommerce.productos;

import com.ecommerce.productos.model.Producto;
import com.ecommerce.productos.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class ProductosServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductosServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ProductoRepository repository) {
		return args -> {
			// Solo inserta si la tabla está vacía
			if (repository.count() == 0) {
				List<Producto> productos = List.of(
					Producto.builder()
						.nombre("Manzana Fuji")
						.descripcion("Manzana dulce y crujiente, perfecta para snacks.")
						.precio(new BigDecimal("1500"))
						.stock(50)
						.categoria("Frutas")
						.imagenUrl("/img/manzanaFuji.png")
						.activo(true)
						.build(),
					Producto.builder()
						.nombre("Lechuga Hidropónica")
						.descripcion("Lechuga fresca cultivada sin tierra.")
						.precio(new BigDecimal("990"))
						.stock(30)
						.categoria("Verduras")
						.imagenUrl("/img/lechuga.png")
						.activo(true)
						.build(),
					Producto.builder()
						.nombre("Miel de Ulmo")
						.descripcion("Miel 100% natural del sur de Chile.")
						.precio(new BigDecimal("4500"))
						.stock(20)
						.categoria("Despensa")
						.imagenUrl("/img/miel.png")
						.activo(true)
						.build(),
					Producto.builder()
						.nombre("Huevos de Campo")
						.descripcion("Bandeja de 12 huevos de gallina feliz.")
						.precio(new BigDecimal("3200"))
						.stock(40)
						.categoria("Despensa")
						.imagenUrl("/img/huevos.png")
						.activo(true)
						.build(),
					Producto.builder()
						.nombre("Zanahorias")
						.descripcion("Zanahorias frescas recién sacadas de la tierra.")
						.precio(new BigDecimal("1200"))
						.stock(60)
						.categoria("Verduras")
						.imagenUrl("/img/zanahorias.png")
						.activo(true)
						.build(),
					Producto.builder()
						.nombre("Plátano")
						.descripcion("Plátano importado de alta calidad.")
						.precio(new BigDecimal("1100"))
						.stock(45)
						.categoria("Frutas")
						.imagenUrl("/img/platano_caverdish.png")
						.activo(true)
						.build()
				);
				repository.saveAll(productos);
				System.out.println("✅ PRODUCTOS CARGADOS AUTOMÁTICAMENTE");
			}
		};
	}
}