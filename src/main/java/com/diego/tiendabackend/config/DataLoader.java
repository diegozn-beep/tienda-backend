package com.diego.tiendabackend.config;

import com.diego.tiendabackend.model.Product;
import com.diego.tiendabackend.model.User;
import com.diego.tiendabackend.repository.ProductRepository;
import com.diego.tiendabackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setName("Administrador");
            admin.setEmail("admin@tecnodiego.pe");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
        }

        if (productRepository.count() == 0) {
            createProduct("Teclado mecánico RGB", "Switches rojos, retroiluminado", 89.9, 25, "Teclados");
            createProduct("Teclado inalámbrico", "Compacto, batería de 3 meses", 59.9, 14, "Teclados");
            createProduct("Mouse gamer", "12000 DPI, 6 botones", 69.5, 3, "Mouse");
            createProduct("Mouse ergonómico", "Vertical, para largas jornadas", 79, 9, "Mouse");
            createProduct("Audífonos con micrófono", "Sonido envolvente 7.1", 119, 12, "Audio");
            createProduct("Parlantes de escritorio", "Bluetooth, 10 W", 95, 7, "Audio");
        }
    }

    private void createProduct(String name, String description, double price, int stock, String category) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategory(category);
        product.setImageUrl("https://placehold.co/300x200");
        productRepository.save(product);
    }
}