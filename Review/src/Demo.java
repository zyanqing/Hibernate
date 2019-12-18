import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

public class Demo {

    @Test
    public void demo1(){


        Session session = HibernateUtils.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Customer customer = new Customer();
        customer.setCust_name("凤姐");
        session.save(customer);

        transaction.commit();
    }

    @Test
    public void demo2(){

        Session session = HibernateUtils.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Customer customer = new Customer();
        customer.setCust_name("大爷");

        LinkMan linkMan = new LinkMan();
        linkMan.setLkm_name("小弟");

        customer.getLinkMans().add(linkMan);
        linkMan.setCustomer(customer);

        session.save(customer);

        transaction.commit();
    }

    @Test
    public void demo3(){

        Session session = HibernateUtils.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        User user = new User();
        user.setUser_name("张三");

        Role role = new Role();
        role.setRole_name("小丑");

        user.getRoles().add(role);
        role.getUsers().add(user);

        session.save(user);

        transaction.commit();
    }

}







