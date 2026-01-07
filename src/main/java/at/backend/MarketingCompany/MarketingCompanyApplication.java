package at.backend.MarketingCompany;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class MarketingCompanyApplication {

  public static void main(String[] args) {
    SpringApplication.run(MarketingCompanyApplication.class, args);
  }

}
