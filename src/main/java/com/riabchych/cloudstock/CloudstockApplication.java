package com.riabchych.cloudstock;

import com.riabchych.cloudstock.security.CrossOriginConfiguration;
import com.riabchych.cloudstock.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CloudstockApplication implements CommandLineRunner {

    @Autowired
    public CloudstockApplication(UserServiceImpl userService) {
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(CloudstockApplication.class, args);
    }

    @Bean
    public CrossOriginConfiguration corsConfiguration() {
        return CrossOriginConfiguration.builder()
                .allowedHeader("X-MyCustomHeader")
                .exposedHeader("X-MyCustomHeader")
                .build();
    }

    private final UserServiceImpl userService;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {

        /*User admin = new User();
        admin.setName("Yevhenii Riabchych");
        HashSet<Role> roles = new HashSet<>();
        roles.add(new Role(EnumRole.ROLE_USER));
        admin.setRoles(roles);
        admin.setUsername("admin");
        admin.setPassword("123456789");
        userService.addUser(admin);*/
    }
}
