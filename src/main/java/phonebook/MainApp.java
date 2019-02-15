package phonebook;


import phonebook.data.model.Contact;
import phonebook.data.access.ContactsRepository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

// TODO: 7/17/18 Cleaning Or Refactor
// TODO: 7/17/18 Study Log4j concept
// TODO: 7/17/18 LOG

public class MainApp {
    private static ContactsRepository CONTACTS_REPOSITORY;
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        initialize();
        run();
    }

    private static void initialize() {

        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Contact.class);
        Properties properties = new Properties();
        ClassLoader classLoader = MainApp.class.getClassLoader();
        File file = new File(classLoader.getResource("config/configuration.properties").getFile());
        try {
            Scanner scanner = new Scanner(file);
            properties.put("hibernate.connection.username", scanner.nextLine().substring(9));
            properties.put("hibernate.connection.password", scanner.nextLine().substring(9));
            properties.put("hibernate.connection.url", scanner.nextLine().substring(4));
            properties.put("hibernate.connection.driver_class", scanner.nextLine().substring(13));
            properties.put("hibernate.dialect", scanner.nextLine().substring(8));
            properties.put("hibernate.hbm2ddl.auto", scanner.nextLine().substring(13));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        configuration.setProperties(properties);
        SessionFactory sessionFactory = configuration.buildSessionFactory(
                new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build());

        CONTACTS_REPOSITORY = new ContactsRepository(sessionFactory);

    }

    private static void run() {
        while (true) {
            int operationNumber = showMenu();
            switch (operationNumber) {
                case 1:
                    System.out.println("You have chosen Search contact operation:");
                    findContact();
                    break;
                case 2:
                    System.out.println("You have chosen Saving new contact operation:");
                    saveNewContact();
                    break;
                case 3:
                    System.out.println("You have chosen Deleting contact operation:");
                    deleteContact();
                    break;
                case 4:
                    System.out.println("You have chosen Editing contact information:");
                    editContactInfo();
                    break;
                case 5:
                    System.out.println("You have chosen Printing contacts information:");
                    printAllContacts();
                    break;
                case 6:
                    System.out.println("Application is shutting down...");
                    System.exit(1);
                    break;
                default:
                    System.out.println("Sorry! Please select a number between 1 and 6.\n");
            }
        }
    }

    private static int showMenu() {
        while (true) {
            System.out.print(
                    "\n***************** Welcome to the phone book main menu *****************\n" +
                            "Please press the key button corresponding to one of the following items:\n" +
                            "1- Search contact.\n" +
                            "2- Save a new contact.\n" +
                            "3- Delete contact.\n" +
                            "4- Update the information of the contact.\n" +
                            "5- Print list of all contacts.\n" +
                            "6- Exit from application.\n"
            );
            int operationNumber = 0;
            try {
                operationNumber = SCANNER.nextInt();
            } catch (Exception e) {
                System.out.println("Sorry! You can only press the number keys.");
            }
            return operationNumber;
        }
    }

    private static void findContact() {
        Contact inputContact = new Contact();
        System.out.println("Please enter first name:");
        inputContact.setFirstName(getStringFromInput());
        System.out.println("Please enter last name:");
        inputContact.setLastName(getStringFromInput());

        List<Contact> foundContacts = new ArrayList<>();
        try {
            foundContacts = CONTACTS_REPOSITORY.findByNameAndFamily(inputContact);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (foundContacts.size() == 0) {
            System.out.println("Contact not found.");
        } else {
            System.out.println(String.format("%d Contact(s) successfully found:", foundContacts.size()));
            for (Contact contact : foundContacts) {
                System.out.println(contact.toString());
            }
        }
    }

    private static void saveNewContact() {

        Contact newContact = new Contact();
        System.out.println("Please enter first name:");
        newContact.setFirstName(getStringFromInput());
        System.out.println("Please enter last name:");
        newContact.setLastName(getStringFromInput());
        System.out.println("Please Enter phone Number:");
        newContact.setPhoneNum(getStringFromInput());
        System.out.println("Please Enter mobile Number:");
        newContact.setMobileNum(getStringFromInput());
        System.out.println("Please Enter Address:");
        newContact.setAddress(getStringFromInput());

        try {
            CONTACTS_REPOSITORY.saveContact(newContact);
            System.out.println("Contact successfully saved.");
        } catch (Exception e) {
            System.out.println("There was a problem in contact saving process.");
            e.printStackTrace();
        }
    }

    private static void deleteContact() {
        System.out.println("Please Enter the ID of Contact:");
        int ID = getLongFromInput();
        try {
            CONTACTS_REPOSITORY.deleteByID(ID);
            System.out.println("Contact with ID=" + ID + " successfully deleted.\n");
        } catch (Exception e) {
            System.out.println("There was a problem in deleting contact.\n");

        }
    }

    private static void editContactInfo() {

        Contact contact = new Contact();
        System.out.println("Please Enter the ID of the contact:");
        contact.setId(getLongFromInput());
        System.out.println("Enter the new information contact" +
                "Enter 0 in case of not changing the corresponding field.");
        System.out.println("new name:");
        contact.setFirstName(getStringFromInput());
        if (getEmptyFlagForInputString(contact.getFirstName())) {
            contact.setFirstName("");
        }
        System.out.println("new family:");
        contact.setLastName(getStringFromInput());
        if (getEmptyFlagForInputString(contact.getLastName())) {
            contact.setLastName("");
        }
        System.out.println("new mobile number:");
        contact.setMobileNum(getStringFromInput());
        if (getEmptyFlagForInputString(contact.getMobileNum())) {
            contact.setMobileNum("");
        }
        System.out.println("new phone number:");
        contact.setPhoneNum(getStringFromInput());
        if (getEmptyFlagForInputString(contact.getPhoneNum())) {
            contact.setPhoneNum("");
        }
        System.out.println("new address:");
        contact.setAddress(getStringFromInput());
        if (getEmptyFlagForInputString(contact.getAddress())) {
            contact.setAddress("");
        }
        try {
            CONTACTS_REPOSITORY.updateContact(contact);
        } catch (Exception e) {
            System.out.println("There is a problem in updating information...");
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Contact information successfully updated.");

    }

    private static void printAllContacts() {
        List<Contact> contacts;
        contacts = CONTACTS_REPOSITORY.getAll();
        for (Contact contact : contacts) {
            System.out.println(
                    "Contact ID= " + contact.getId() +
                            " firstName= " + contact.getFirstName() +
                            " lastName= " + contact.getLastName() +
                            " phoneNumber= " + contact.getPhoneNum() +
                            " mobileNumber= " + contact.getMobileNum() +
                            " address= " + contact.getAddress());
        }

    }

    private static int getLongFromInput() {
        return SCANNER.nextInt();
    }

    private static String getStringFromInput() {
        return SCANNER.next();
    }

    private static boolean getEmptyFlagForInputString(String field) {
        if (field.equals("0")) {
            return true;
        } else {
            return false;
        }
    }
}