import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import java.util.List;

public class HibernateDemo {

    @Test
    public void demo1() {

        //Session代表的是Hibernate与数据库的链接对象。不是线程安全的。与数据库交互桥梁。
        Session session = HibernateUtils.openSession();

        //开启事务
        Transaction transaction = session.beginTransaction();

        Customer customer = new Customer();
        customer.setCust_id(1l);
        customer.setCust_name("张三");
        customer.setCust_level("66");
        session.update(customer);

        //提交事务
        transaction.commit();

        //释放资源
        session.close();

    }

    @Test // 查询
    public void demo2() {

        Session session = HibernateUtils.openSession();
        Transaction transaction = session.beginTransaction();

        Customer cs = null;

        /* get
           采用的是立即加载，执行到这里的时候，就会马上发送SQL语句去查询。
           查询后返回的是真实对象本身。
           查询一个找不到的对象的时候，返回null
         */
//        cs = session.get(Customer.class, 6l);


        /* load
           采用的是延迟加载（lazy懒加载），执行到这行代码的时候，不会发送SQL语句，当真正使用这个对象的时候才会发送SQL语句。
           查询后返回的是代理对象，javassist-3.18.1-GA.jar 利用javassist技术产生的代理
           查询一个找不到的对象的时候，返回ObjectNotFoundException
         */
        cs = session.load(Customer.class, 1l);

        System.out.println(cs);
        transaction.commit();
        session.close();
    }

    @Test //修改操作
    public void demo3() {

        Session session = HibernateUtils.openSession();
        Transaction transaction = session.beginTransaction();

        // 直接创建对象，进行修改。弊端：可能会覆盖或者修改你不希望更新的值
       /*
        Customer customer = new Customer();
        customer.setCust_id(1l);
        customer.setCust_name("李四");
        session.update(customer);
        */

        // 先查询，再修改
        Customer customer = session.get(Customer.class, 1l);
        customer.setCust_phone("66666666666");
        session.update(customer);

        transaction.commit();
        session.close();

    }

    @Test //删除操作
    public void demo4() {

        Session session = HibernateUtils.openSession();
        Transaction transaction = session.beginTransaction();

        // 直接创建对象删除，不能级联删除
        /*
        Customer customer = new Customer();
        customer.setCust_id(3l);
        session.delete(customer);
        */

        // 先查询再删除，可以级联删除
        Customer customer = session.get(Customer.class,2l);
        session.delete(customer);

        transaction.commit();
        session.close();
    }

    @Test //保存或更新
    public void demo5(){

        Session session = HibernateUtils.openSession();
        Transaction transaction = session.beginTransaction();

        /*
        Customer customer = new Customer();
        customer.setCust_name("如花");
        session.saveOrUpdate(customer);
        */

        Customer customer = new Customer();
        customer.setCust_id(4l);
        customer.setCust_name("li如");
        session.saveOrUpdate(customer);

        transaction.commit();
        session.close();

    }

    @Test //查询所有
    public void demo6(){

        Session session = HibernateUtils.openSession();
        Transaction transaction = session.beginTransaction();

        // 接收HQL：Hibernate Query Language 面向对象的查询语言
        /*
        Query query = session.createQuery("from Customer");
        List<Customer> list = query.list();
        for (Customer customer : list){
             System.out.println(customer);
        }
        */

        // 接收SQL
        SQLQuery query = session.createSQLQuery("select * from cst_customer");
        List<Object[]> list = query.list();
        for (Object[] obj:list) {
             System.out.println(obj);
        }

        transaction.commit();
        session.close();

    }

}
