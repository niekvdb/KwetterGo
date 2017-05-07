import javax.faces.bean.ManagedBean;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;


@ManagedBean(name = "KwetterGO")
public class MessageService {

    private String username = "";
    private String kweetText = "";

    private static final Logger LOG = Logger.getLogger(MessageService.class.getName());

    private static final String JNDI_CONNECTION_FACTORY = "jms/__defaultConnectionFactory";

    private static final String JNDI_QUEUE = "jms/KwetterGo";

    private static <T> T lookup(Class<T> retvalClass, String jndi) {
        try {
            return retvalClass.cast(InitialContext.doLookup(jndi));
        } catch (NamingException ex) {
            throw new IllegalArgumentException("failed to lookup instance of " + retvalClass + " at " + jndi, ex);
        }
    }

    public void sendMessage() {
        final ConnectionFactory connectionFactory = lookup(ConnectionFactory.class, JNDI_CONNECTION_FACTORY);
        final Queue queue = lookup(Queue.class, JNDI_QUEUE);

        JMSContext jmsContext = connectionFactory.createContext();
        final JMSProducer producer = jmsContext.createProducer();

        StringBuilder sb = new StringBuilder();

        sb.append(username);
        sb.append(" : ");
        sb.append(kweetText);

        producer.send(queue, sb.toString());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKweetText() {
        return kweetText;
    }

    public void setKweetText(String kweetText) {
        this.kweetText = kweetText;
    }
}
