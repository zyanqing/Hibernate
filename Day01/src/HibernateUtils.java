import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {

    public static final Configuration cfg;
    public static final SessionFactory factiory;

    static {
        cfg = new Configuration().configure();
        factiory = cfg.buildSessionFactory();
    }

    public static Session openSession(){
        return factiory.openSession();
    }
}
