package phonebook.data.access;

import phonebook.data.model.Contact;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class ContactsRepository {
    private StatementFactory statementFactory;

    public ContactsRepository(StatementFactory statementFactory) {
        this.statementFactory = statementFactory;
    }

    public boolean saveContact(Contact contact) throws SQLException {
        String insertQuery = "INSERT INTO contact( firstName, lastName, phoneNum, mobileNum, address)" +
                " VALUES ('" +
                contact.getFirstName() + "','" +
                contact.getLastName() + "','" +
                contact.getPhoneNum() + "','" +
                contact.getMobileNum() + "','" +
                contact.getAddress() +
                "')";

        Statement mysqlStatement = statementFactory.get();
        mysqlStatement.executeUpdate(insertQuery);
        mysqlStatement.close();
        return true;
    }

    public void updateContact(Contact contact) throws Exception {
        String updateQuery = "UPDATE contact SET ";
        StringBuilder updateFields = new StringBuilder();
        String whereString = format(" WHERE ID = %s", contact.getId());

        if (!contact.getFirstName().isEmpty()) {
            updateFields.append(format(" firstName = '%s',", contact.getFirstName()));
        }

        if (!contact.getLastName().isEmpty()) {
            updateFields.append(format(" lastName = '%s',", contact.getLastName()));
        }

        if (!contact.getMobileNum().isEmpty()) {
            updateFields.append(format(" mobileNum = '%s',", contact.getMobileNum()));
        }

        if (!contact.getPhoneNum().isEmpty()) {
            updateFields.append(format(" phoneNum = '%s',", contact.getPhoneNum()));
        }

        if (!contact.getAddress().isEmpty()) {
            updateFields.append(format(" address = '%s',", contact.getAddress()));
        }

        if (updateFields.toString().isEmpty()) {
            System.out.println("There was no information to be updated.");
            return;
        }

        Statement statement = statementFactory.get();
        statement.executeUpdate(updateQuery + updateFields.substring(0, updateFields.length() - 1) + whereString);
        statement.close();
        System.out.println("Contact updated successfully.");
    }

    public Contact findContactByNameAndFamily(String name, String family) throws SQLException {

        String selectQuery = "SELECT * FROM contact WHERE firstName = '" + name +
                "' and lastName = '" + family + "'";
        Statement mysqlStatement = statementFactory.get();
        ResultSet resultSet = mysqlStatement.executeQuery(selectQuery);
        Contact foundContact = null;
        if (resultSet.next()) {
            foundContact = createContactFromResultSet(resultSet);
        }
        mysqlStatement.close();
        return foundContact;
    }


    public boolean deleteContactByID(int ID) throws SQLException {

        String selectQuery = "DELETE FROM contact WHERE id ='" + ID + "'";
        Statement dataBaseStatement = statementFactory.get();
        dataBaseStatement.executeUpdate(selectQuery);
        dataBaseStatement.close();
        return true;
    }

    public Contact findContactByID(Long ID) throws SQLException {
        String selectQuery = "SELECT * FROM contact WHERE id = '" + ID + "'";
        Statement mysqlStatement = statementFactory.get();
        ResultSet resultSet = mysqlStatement.executeQuery(selectQuery);
        if (!resultSet.next()) {
            return null;
        }
        return createContactFromResultSet(resultSet);
    }

    private Contact createContactFromResultSet(ResultSet resultSet) throws SQLException {

        Contact foundContact = new Contact();
        foundContact.setId(resultSet.getInt("id"));
        foundContact.setFirstName(resultSet.getString("firstName"));
        foundContact.setLastName(resultSet.getString("lastName"));
        foundContact.setPhoneNum(resultSet.getString("phoneNum"));
        foundContact.setMobileNum(resultSet.getString("mobileNum"));
        foundContact.setAddress(resultSet.getString("address"));

        return foundContact;

    }

    public List<Contact> getAll() throws SQLException {
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
