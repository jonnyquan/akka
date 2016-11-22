package akka.enter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ruancl@xkeshi.com on 16/11/21.
 */
@Configuration
public class BeanInit {

    @Bean
    public AkkaInitFactory akkaInit(){
        return new AkkaInitFactory();
    }
}
