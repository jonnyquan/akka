package akka.enter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ruancl@xkeshi.com on 16/11/21.
 */
@Configuration
public class BeanInit {

    private AkkaInitFactory akkaInitFactory;

    @Bean
    public AkkaInitFactory akkaInit(){
       this.akkaInitFactory = new AkkaInitFactory();
        return akkaInitFactory;
    }

    @Bean
    public BeanProcess beanManager(){
        return new BeanProcess(akkaInitFactory);
    }
}
