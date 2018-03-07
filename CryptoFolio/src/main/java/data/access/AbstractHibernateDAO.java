package data.access;

import data.DatabaseSessionFactory;
import data.model.Employee;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sylvain on 3/5/18.
 */
public abstract class AbstractHibernateDAO < T extends Serializable>{

    private Class< T > clazz;

    public AbstractHibernateDAO() {

    }

    public void setClazz(Class<T> clazzToSet) {
        this.clazz = clazzToSet;
    }

    public T findById(int id) {
        Session session = DatabaseSessionFactory.getInstance().openSession();
        return (T) session.get(clazz, id);
    }

    public List<T> findAll(){

        Session session = DatabaseSessionFactory.getInstance().openSession();
        Transaction tx = null;
        List<T> entityList = null;
        try {
            tx = session.beginTransaction();
            entityList = session.createQuery( "from " + clazz.getName() ).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return entityList;
    }


    public void save( final T entity ){

        Session session = DatabaseSessionFactory.getInstance().openSession();
        Transaction tx = null;
        Integer entityID = null;

        try {
            tx = session.beginTransaction();
            session.save(entity);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void update(T entity ){

        Session session = DatabaseSessionFactory.getInstance().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.update(entity);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void delete(T entity ){

        Session session = DatabaseSessionFactory.getInstance().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.delete(entity);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    public void deleteById(int id ){
        T entity = this.findById(id);
        this.delete( entity );
    }
}
