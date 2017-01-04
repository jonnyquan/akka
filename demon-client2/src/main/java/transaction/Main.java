package transaction;

import com.google.common.base.Optional;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringStack;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Objects;

/**
 * Created by ruancl@xkeshi.com on 2016/12/29.
 */
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        TransactionService transactionService = context.getBean(TransactionService.class);
        transactionService.needDisTransaction();
    }
}
