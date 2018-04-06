package data.access;

import com.google.common.base.Preconditions;
import data.DatabaseSessionFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

/**
 * Abstract DAO implementation for CRUD operations for Hibernate.
 */
public abstract class AHibernateDAO <T extends Serializable> {

    /**
     * The logger.
     */
    private static Logger logger = LoggerFactory.getLogger(AHibernateDAO.class);

    /**
     * The DAO class.
     */
    private Class<T> clazz;

    /**
     * Constructor.
     * @param clazz The DAO class
     */
    public AHibernateDAO(Class<T> clazz) {

        // Validate preconditions
        Preconditions.checkNotNull(clazz);

        // Set member
        this.clazz = clazz;
    }

    /**
     * Finds an entity by its ID.
     * @param id The ID to lookup
     * @return The entity if found, null otherwise.
     */
    public T findById(int id) {

        // Open the session
        Session session = DatabaseSessionFactory.getInstance().openSession();
        Transaction tx = null;
        T entity = null;

        // Try to retrieve the entity
        try {
            tx = session.beginTransaction();
            entity = session.get(clazz, id);
            tx.commit();

        // Catch the hibernate exception and try to rollback the transaction
        } catch (HibernateException e) {

            // Log the exception
            logger.warn("Exception occurred while finding an entity by its ID", e);

            // Rollback the transaction if possible
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return entity;
    }

    /**
     * Finds all entities.
     * @return All the entities or an empty array if no entity is found.
     */
    public List<T> findAll(){

        // Open the session
        Session session = DatabaseSessionFactory.getInstance().openSession();
        Transaction tx = null;
        List<T> entityList = null;

        // Try to retrieve the entities
        try {
            tx = session.beginTransaction();
            entityList = session.createQuery( "from " + clazz.getName() ).list();
            tx.commit();

        // Catch the hibernate exception and try to rollback the transaction
        } catch (HibernateException e) {

            // Log the exception
            logger.warn("Exception occurred while finding all entities", e);

            // Rollback the transaction if possible
            if (tx!=null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return entityList;
    }

    /**
     * Finds one entity with a match on a property.
     * @param propertyName The name of the property to match
     * @param value The value of the property desired
     * @return The entity if found, null otherwise
     */
    public T findOneByProperty(String propertyName, Object value) {

        // Open the session
        Session session = DatabaseSessionFactory.getInstance().openSession();
        Transaction tx = null;
        T entity = null;

        // Try to retrieve the entity
        try {
            tx = session.beginTransaction();

            // Get Criteria Builder
            CriteriaBuilder criteriaBuilder =  session.getCriteriaBuilder();

            // Create the Query
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.clazz);
            Root<T> rootT = criteriaQuery.from(this.clazz);
            criteriaQuery.select(rootT);
            criteriaQuery.where(criteriaBuilder.equal(rootT.get(propertyName), value));

            // Retrieve the entity
            entity = session.createQuery(criteriaQuery).uniqueResult();
            tx.commit();

        // Catch the hibernate exception and try to rollback the transaction
        } catch (HibernateException e) {

            // Log the exception
            logger.warn("Exception occurred while finding one entity by property", e);

            // Rollback the transaction if possible
            if (tx!=null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return entity;
    }

    /**
     * Creates an entity.
     * @param entity The entity to create
     */
    public void create(T entity ) {

        // Open the session
        Session session = DatabaseSessionFactory.getInstance().openSession();
        Transaction tx = null;
        Integer entityID = null;

        // Try to create the entity
        try {
            tx = session.beginTransaction();
            session.save(entity);
            tx.commit();

        // Catch the hibernate exception and try to rollback the transaction
        } catch (HibernateException e) {

            // Log the exception
            logger.warn("Exception occurred while creating the entity", e);

            // Rollback the transaction if possible
            if (tx!=null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    /**
     * Updates en entity.
     * @param entity The entity to update
     */
    public void update(T entity) {

        // Open the session
        Session session = DatabaseSessionFactory.getInstance().openSession();
        Transaction tx = null;

        // Try to update the entity
        try {
            tx = session.beginTransaction();
            session.update(entity);
            tx.commit();

        // Catch the hibernate exception and try to rollback the transaction
        } catch (HibernateException e) {

            // Log the exception
            logger.warn("Exception occurred while updating the entity", e);

            // Rollback the transaction if possible
            if (tx!=null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    /**
     * Deletes the entity.
     * @param entity The entity to delete
     */
    public void delete(T entity) {

        // Open the session
        Session session = DatabaseSessionFactory.getInstance().openSession();
        Transaction tx = null;

        // Try to delete the entity
        try {
            tx = session.beginTransaction();
            session.delete(entity);
            tx.commit();

        // Catch the hibernate exception and try to rollback the transaction
        } catch (HibernateException e) {

            // Log the exception
            logger.warn("Exception occurred while deleting the entity", e);

            // Rollback the transaction if possible
            if (tx!=null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    /**
     * Deletes an entity by its ID.
     * @param id The id of the entity to delete
     */
    public void deleteById(int id) {

        // Retrieve the entity
        T entity = this.findById(id);
        Preconditions.checkNotNull(entity);

        // Delete the entity
        this.delete(entity);
    }

    /**
     * Counts the number of rows in the database for the entity.
     * @return The number of rows in the database.
     */
    public Long countRowInDatabase() {

        // Open the session
        Session session = DatabaseSessionFactory.getInstance().openSession();
        Transaction tx = null;
        Long result = Long.valueOf(-1);

        // Try to retrieve the entities
        try {
            tx = session.beginTransaction();

            // Get Criteria Builder
            CriteriaBuilder criteriaBuilder =  session.getCriteriaBuilder();

            // Create the Query
            CriteriaQuery<Long> criteriaQueryCount = criteriaBuilder.createQuery(Long.class);
            Root<T> entityRoot = criteriaQueryCount.from(this.clazz);
            criteriaQueryCount.select(criteriaBuilder.count(entityRoot));
            result = session.createQuery(criteriaQueryCount).uniqueResult();
            tx.commit();

            // Catch the hibernate exception and try to rollback the transaction
        } catch (HibernateException e) {

            // Log the exception
            logger.warn("Exception occurred while counting all entities", e);

            // Rollback the transaction if possible
            if (tx!=null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return result;
    }
}