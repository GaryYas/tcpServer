package server.tcp;

import com.mongodb.MongoClientURI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TcpApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(TcpApplication.class, args);
		Server  server = context.getBean(Server.class);
		server.startServer();
	}
}
