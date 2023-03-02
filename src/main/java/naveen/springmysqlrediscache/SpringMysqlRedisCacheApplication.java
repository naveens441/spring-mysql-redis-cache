package naveen.springmysqlrediscache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringMysqlRedisCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMysqlRedisCacheApplication.class, args);
	}

}
