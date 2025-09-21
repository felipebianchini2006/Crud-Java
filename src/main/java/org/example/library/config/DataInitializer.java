package org.example.library.config;

import org.example.library.user.User;
import org.example.library.user.UserRepository;
import org.example.library.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create default admin user if not exists
        if (!userRepository.existsByEmail("admin@biblioteca.com")) {
            User admin = new User();
            admin.setName("Administrador");
            admin.setEmail("admin@biblioteca.com");
            admin.setPassword(passwordEncoder.encode("password123"));
            admin.setRole(UserRole.ADMIN);
            admin.setBio("Administrador do sistema da biblioteca online.");
            userRepository.save(admin);
        }

        // Create sample author if not exists
        if (!userRepository.existsByEmail("joao.silva@email.com")) {
            User author = new User();
            author.setName("João Silva");
            author.setEmail("joao.silva@email.com");
            author.setPassword(passwordEncoder.encode("password123"));
            author.setRole(UserRole.AUTHOR);
            author.setBio("Escritor brasileiro especializado em ficção científica e fantasia.");
            userRepository.save(author);
        }

        // Create sample reader if not exists
        if (!userRepository.existsByEmail("maria.santos@email.com")) {
            User reader = new User();
            reader.setName("Maria Santos");
            reader.setEmail("maria.santos@email.com");
            reader.setPassword(passwordEncoder.encode("password123"));
            reader.setRole(UserRole.READER);
            reader.setBio("Apaixonada por leitura, especialmente romances e biografias.");
            userRepository.save(reader);
        }
    }
}
