package phonebook.data.access;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import phonebook.data.model.Contact;

import java.util.List;

public class ContactsRepository {
    private SessionFactory sessionFactory;

    public ContactsRepository(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public boolean saveContact(Contact inputContact) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(inputContact);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    public void updateContact(Contact inputContact) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Contact persistantContact = session.get(Contact.class, inputContact.getId());

        if(!inputContact.getFirstName().isEmpty()){
            persistantContact.setFirstName(inputContact.getFirstName());
        }
        if(!inputContact.getLastName().isEmpty()){
            persistantContact.setLastName(inputContact.getLastName());
        }
        if(!inputContact.getPhoneNum().isEmpty()){
            persistantContact.setPhoneNum(inputContact.getPhoneNum());
        }
        if(!inputContact.getMobileNum().isEmpty()){
            persistantContact.setMobileNum(inputContact.getMobileNum());
        }
        if(!inputContact.getAddress().isEmpty()){
            persistantContact.setAddress(inputContact.getAddress());
        }

        session.getTransaction().commit();
        session.close();
    }

    // TODO: 7/22/18 Use DataTransferObject (DTO) pattern
    public List<Contact> findByNameAndFamily(Contact inputContact) {

        List<Contact> foundContacts;
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        /*String hql = "FROM Contact C WHERE C.firstName = :first_name AND C.lastName = :last_name";
        Query query = session.createQuery(hql);
        query.setParameter("first_name", name);
        query.setParameter("last_name", family);
        foundContacts = query.list();*/

        Criteria criteria = session.createCriteria(Contact.class);
        LogicalExpression logicalExpression =
                Restrictions.and(
                        Restrictions.eq("firstName", inputContact.getFirstName()),
                        Restrictions.eq("lastName",inputContact.getLastName())  );
        criteria.add(logicalExpression);
        foundContacts = criteria.list();


        return foundContacts;
    }

    public boolean deleteByID(int id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Contact persistantContact = session.get(Contact.class,id);
        session.delete(persistantContact);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    public List<Contact> getAll() {
        List<Contact> contacts;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "FROM Contact";
        Query query = session.createQuery(hql);
        contacts = query.list();
        session.getTransaction().commit();
        session.close();
        return contacts;
    }
}