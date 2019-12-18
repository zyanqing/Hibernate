package oneToMany;

import Utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

public class Demo1 {

    @Test // 保存2个客户和三个联系人并且建立好关系
    public void demo1() {
        Session session = HibernateUtils.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Customer customer1 = new Customer();
        customer1.setCust_name("王东");
        Customer customer2 = new Customer();
        customer2.setCust_name("赵洪");

        LinkMan linkMan1 = new LinkMan();
        linkMan1.setLkm_name("凤姐");
        LinkMan linkMan2 = new LinkMan();
        linkMan2.setLkm_name("如花");
        LinkMan linkMan3 = new LinkMan();
        linkMan3.setLkm_name("旺财");


        /*
        关联关系没有相互对应，将会导致数据库重复创建相同的数据(主键不同)

        linkMan1.setCustomer(customer1);
        linkMan2.setCustomer(customer1);
        linkMan3.setCustomer(customer2);
        customer1.getLinkMans().add(linkMan1);
        customer1.getLinkMans().add(linkMan2);
        customer2.getLinkMans().add(linkMan3);
        customer2.getLinkMans().add(linkMan1);

        // 需要全部保存
        session.save(linkMan1);
        session.save(linkMan2);
        session.save(linkMan3);
        session.save(customer1);
        session.save(customer2);
         */

        linkMan1.setCustomer(customer1);
        linkMan2.setCustomer(customer1);
        linkMan3.setCustomer(customer2);
        customer1.getLinkMans().add(linkMan1);
        customer1.getLinkMans().add(linkMan2);
        customer2.getLinkMans().add(linkMan3);

        // 需要全部保存
        session.save(linkMan1);
        session.save(linkMan2);
        session.save(linkMan3);
        session.save(customer1);
        session.save(customer2);

        transaction.commit();
    }

    @Test
    // 一对多关系只保存一边是否可以
    public void demo2() {

        Session session = HibernateUtils.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Customer customer = new Customer();
        customer.setCust_name("赵洪");

        LinkMan linkMan = new LinkMan();
        linkMan.setLkm_name("如花");

        customer.getLinkMans().add(linkMan);
        linkMan.setCustomer(customer);

        // 不设置级联的情况下，只保存一边不可以，会报一个瞬时态对象异常：持久态对象关联了一个瞬时态对象
        session.save(customer);

        transaction.commit();
    }


    @Test
    /**
     *  级联保存或更新操作：
     *  * 保存客户级联联系人，操作的主体是客户对象，需要在Customer.hbm.xml中进行配置
     *  * <set name="linkMans" cascade="save-update">
     */
    public void demo3() {
        Session session = HibernateUtils.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Customer customer = new Customer();
        customer.setCust_name("赵洪");

        LinkMan linkMan = new LinkMan();
        linkMan.setLkm_name("如花");

        customer.getLinkMans().add(linkMan);
        linkMan.setCustomer(customer);

        // 报瞬时态错误
        //session.save(linkMan);

        session.save(customer);

        transaction.commit();
    }

    @Test
    /**
     *  级联保存或更新操作：
     *  * 保存联系人级联客户，操作的主体是客户对象，需要在LinkMan.hbm.xml中进行配置
     *  * <set name="customer" cascade="save-update">
     */
    public void demo4() {

        Session session = HibernateUtils.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Customer customer = new Customer();
        customer.setCust_name("赵洪");

        LinkMan linkMan = new LinkMan();
        linkMan.setLkm_name("如花");

        customer.getLinkMans().add(linkMan);
        linkMan.setCustomer(customer);


        session.save(linkMan);

        // 报瞬时态错误
        //session.save(customer);

        transaction.commit();

    }


    @Test
    public void demo5(){

        Session session = HibernateUtils.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Customer customer = new Customer();
        customer.setCust_name("李兵");

        LinkMan linkMan1 = new LinkMan();
        linkMan1.setLkm_name("凤姐");
        LinkMan linkMan2 = new LinkMan();
        linkMan2.setLkm_name("如花");
        LinkMan linkMan3 = new LinkMan();
        linkMan3.setLkm_name("芙蓉");

        linkMan1.setCustomer(customer);
        customer.getLinkMans().add(linkMan2);
        customer.getLinkMans().add(linkMan3);

        // 双方都设置了cascade
        //session.save(linkMan1) //发送4条insert语句
        //session.save(customer) //发送3条insert语句
        session.save(linkMan2); //发送1条insert语句

        transaction.commit();

    }

    @Test
    /**
     * 级联删除：
     * * 删除客户级联删除联系人，删除的主体是客户，需要在Customer.hbm.xml中配置
     * * <set name="linkMans" cascade="delete">
     */
    public void demo6(){

        Session session = HibernateUtils.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        // 没有设置级联删除，默认情况：修改了联系人的外键，删除客户
        /*Customer customer = session.get(Customer.class,1l);
        session.delete(customer);*/

        // 删除客户，同时删除联系人
        Customer customer = session.get(Customer.class,1l);
        session.delete(customer);

        transaction.commit();
    }

    @Test // 几乎不用
    /**
     * 级联删除：
     * * 删除联系人级联删除客户，删除的主体是联系人，需要在LinkMan.hbm.xml中配置
     * * <many-to-one name="customer" cascade="delete">
     */
    public void demo7(){

        Session session = HibernateUtils.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        // 删除联系人，同时删除客户
        LinkMan linkMan = session.get(LinkMan.class,1l);
        session.delete(linkMan);

        transaction.commit();

    }

    @Test
    /**
     * 将2号联系人原来归1号客户，现在改为2号客户
     */
    public void demo8(){
        Session session = HibernateUtils.getCurrentSession();
        Transaction tx = session.beginTransaction();

        // 查询2号联系人
        LinkMan linkMan = session.get(LinkMan.class, 2l);
        // 查询2号客户
        Customer customer = session.get(Customer.class, 2l);
        // 双向的关联
        linkMan.setCustomer(customer);
        customer.getLinkMans().add(linkMan);

        tx.commit();
    }


    @Test
    /**
     * 区分cascade和inverse的区别
     */
    public void demo9(){
        Session session = HibernateUtils.getCurrentSession();
        Transaction tx = session.beginTransaction();

        Customer customer = new Customer();
        customer.setCust_name("李兵");

        LinkMan linkMan = new LinkMan();
        linkMan.setLkm_name("凤姐");

        customer.getLinkMans().add(linkMan);

        // 条件在Customer.hbm.xml上的set中配置了cascade="save-update" inverse="true"
        session.save(customer); // 客户会插入到数据库，联系人也会插入到数据库，但是外键为null

        tx.commit();
    }

}




























