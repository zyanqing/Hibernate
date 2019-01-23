import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

public class Demo {

    @Test
    public void demo1(){
        Session session = HibernateUtils.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Customer customer = new Customer();
        customer.setCust_name("zhangshan");
        session.save(customer);

        transaction.commit();
    }


    
}
