package phonebook.model;

import phonebook.da.StatementFactory;
import phonebook.exception.Contact;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContactsRepository {
    private StatementFactory statementFactory;

    public ContactsRepository(StatementFactory statementFactory) {
        this.statementFactory = statementFactory;
    }

    public boolean saveContact(Contact contact)throws SQLException{
        String inserQuery = "INSERT INTO contact( first_name, last_name, phone_number, mobile_number, address)"+
                "VALUES('" +
                contact.getFirstName() + "','" +
                contact.getLastName() + "','" +
                contact.getPhoneNum() + "','" +
                contact.getMobileNum() + "','" +
                contact.getAddress() +
                "')" ;

        Statement mysqlStatement = statementFactory.get();
        mysqlStatement.executeUpdate(inserQuery);
        mysqlStatement.close();
        return true;
    }

    public void updateContact(Contact contact) throws Exception {
        String queryString = "UPDATE contact SET ";
        String setString = "";
        String whereString = String.format(" WHERE ID = %s", contact.getId());

        if (!contact.getFirstName().isEmpty()) {
            setString = setString.concat(String.format(" first_name = '%s',", contact.getFirstName()));
        }

        if (!contact.getLastName().isEmpty()) {
            setString = setString.concat(String.format(" last_name = '%s',", contact.getLastName()));
        }

        if (setString.isEmpty()) {
            return;
        } else {
            setString = setString.substring(0, setString.length() - 1);
        }

        System.out.println(queryString + setString + whereString);
//        statementFactory.get().executeUpdate();


//        String updateQuery = "UPDATE contact SET " +
//                queryGenerator("first_name = '",contact.getFirstName(), false) +
//                queryGenerator("last_name = '", contact.getLastName(), false) +
//                queryGenerator("phone_number = '", contact.getPhoneNum(), false) +
//                queryGenerator("mobile_number = '", contact.getMobileNum(),false) +
//                queryGenerator("address = '", contact.getAddress(),true) +
//                "WHERE id = " + contact.getId();
//        System.out.println(updateQuery);
//        Statement statement = statementFactory.get();
//        statement.executeUpdate(updateQuery);
//        statement.close();
//        return ;
    }

    private String queryGenerator(String columnName, String inputField, boolean lastColumn){
        String query = "";
        if(!inputField.isEmpty()){
            if (lastColumn) {
                query = columnName + inputField + "' ";
            } else {
                query = columnName + inputField + "', ";
            }
        }
        return query;

    }

    public Contact findContactByNameAndFamily(String name, String family) throws SQLException{

        String selectQuery = "SELECT * FROM contact WHERE first_name = '" + name +
                "' and last_name = '" + family + "'";
        Statement mysqlStatement = statementFactory.get();
        ResultSet resultSet = mysqlStatement.executeQuery(selectQuery);
        Contact foundContact = createContactFromResultSet(resultSet);

        mysqlStatement.close();
        return foundContact;
    }


    public boolean deleteContactByID(Long ID) throws SQLException{

        String selectQuery = "DELETE FROM contact WHERE id ='" + ID +"'";
        Statement dataBaseStatement = statementFactory.get();
        dataBaseStatement.executeUpdate(selectQuery);
        dataBaseStatement.close();
        return true;

    }

    public Contact findContactByID(Long ID) throws SQLException{
        String selectQuery = "SELECT * FROM contact WHERE id = '" + ID +"'";
        Statement mysqlStatement = statementFactory.get();
        ResultSet resultSet = mysqlStatement.executeQuery(selectQuery);
        return createContactFromResultSet(resultSet);
    }

    private Contact createContactFromResultSet(ResultSet resultSet) throws SQLException {

        Contact foundContact = new Contact();
        foundContact.setId(resultSet.getLong("id"));
        foundContact.setFirstName(resultSet.getString("first_name"));
        foundContact.setLastName(resultSet.getString("last_name"));
        foundContact.setPhoneNum(resultSet.getString("phone_number"));
        foundContact.setMobileNum(resultSet.getString("mobile_number"));
        foundContact.setAddress(resultSet.getString("address"));
        return foundContact;

    }

    public List<Contact> getAll() throws SQLException{
        List<Contact> contacts = new ArrayList<>();
        String selectQuery = "SELECT * FROM contact";
        Statement mysqlStatement = statementFactory.get();
        ResultSet resultSet = mysqlStatement.executeQuery(selectQuery);
        while (resultSet.next()) {
            contacts.add(createContactFromResultSet(resultSet));
        }

        return contacts;
    }
}
