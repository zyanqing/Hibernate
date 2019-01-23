import org.hibernate.Session;
import org.hibernate.Transaction;

public class Demo {

    Session session = HibernateUtils.getCurrentSession();
    Transaction t = session.beginTransaction();

}
