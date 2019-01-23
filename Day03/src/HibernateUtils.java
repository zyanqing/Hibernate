import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {

    public static SessionFactory factory;
    public static Configuration configuration;

    static {
        configuration = new Configuration().configure();
        factory = configuration.buildSessionFactory();
    }

    public static Session openSession(){
        return factory.openSession();
    }

    public static Session getCurrentSession(){
        return factory.getCurrentSession();
    }

}
