package phonebook;


import phonebook.data.access.StatementFactory;
import phonebook.data.model.Contact;
import phonebook.data.access.ContactsRepository;
import phonebook.util.SequenceGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

// TODO: 7/17/18 Cleaning Or Refactor
// TODO: 7/17/18 Study Log4j concept
// TODO: 7/17/18 LOG
// TODO: 7/17/18 add packages
// TODO: 7/17/18 Use JDBC for save data to database
// TODO: 7/18/18 Use properties file
// TODO: 7/18/18 Manage statement
// TODO: 7/18/18 changed the name of table

public class MainApp {
    private static StatementFactory STATEMENT_FACTORY;
    private static ContactsRepository CONTACTS_REPOSITORY;
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args){
        initialize();
        run();
    }

    private static void initialize() {
        STATEMENT_FACTORY = new StatementFactory();
        ClassLoader classLoader = MainApp.class.getClassLoader();
        File file = new File(classLoader.getResource("config/database.properties").getFile());
        try {
            Scanner scanner = new Scanner(file);
            STATEMENT_FACTORY.setUsername(scanner.nextLine().substring(14));
            STATEMENT_FACTORY.setPassword(scanner.nextLine().substring(14));
            STATEMENT_FACTORY.setUrl(scanner.nextLine().substring(9));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        CONTACTS_REPOSITORY = new ContactsRepository(STATEMENT_FACTORY);
    }

    private static void run() {
        while(true) {
            int operationNumber = showMenu();
            switch (operationNumber) {
                case 1 :
                    System.out.println("You have chosen Search contact operation:");
                    findContact();
                    break;
                case 2 :
                    System.out.println("You have chosen Saving new contact operation:");
                    saveNewContact();
                    break;
                case 3 :
                    System.out.println("You have chosen Deleting contact operation:");
                    deleteContact();
                    break;
                case 4 :
                    System.out.println("You have chosen Editing contact information:");
                    editContactInfo();
                    break;
                case 5 :
                    System.out.println("You have chosen Printing contacts information:");
                    printAllContacts();
                    break;
                case 6:
                    System.out.println("Application is shutting down...");
                    System.exit(1);
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
            }catch (Exception e){
                System.out.println("Sorry! You can only press the number keys.");
            }
            return operationNumber;

        }
    }

    private static void printAllContacts() {
        List<Contact> contacts;
        try {
            contacts = CONTACTS_REPOSITORY.getAll();
            for(Contact contact:contacts) {
                System.out.println(
                        "Contact ID= " + contact.getId() +
                        " firstName= " + contact.getFirstName() +
                        " lastName= " + contact.getLastName() +
                        " phoneNumber= " + contact.getPhoneNum() +
                        " mobileNumber= " + contact.getMobileNum() +
                        " address= " + contact.getAddress());
            }
        }catch(SQLException e){
            e.printStackTrace();
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
        if(emptyFlagForInputString(contact.getFirstName())){
            contact.setFirstName("");
        }
        System.out.println("new family:");
        contact.setLastName(getStringFromInput());
        if(emptyFlagForInputString(contact.getLastName())){
            contact.setLastName("");
        }
        System.out.println("new mobile number:");
        contact.setMobileNum(getStringFromInput());
        if(emptyFlagForInputString(contact.getMobileNum())){
            contact.setMobileNum("");
        }
        System.out.println("new phone number:");
        contact.setPhoneNum(getStringFromInput());
        if(emptyFlagForInputString(contact.getPhoneNum())){
            contact.setPhoneNum("");
        }
        System.out.println("new address:");
        contact.setAddress(getStringFromInput());
        if(emptyFlagForInputString(contact.getAddress())){
            contact.setAddress("");
        }
        try {
            CONTACTS_REPOSITORY.updateContact(contact);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static int getLongFromInput() {
        return SCANNER.nextInt();
    }

    private static String getStringFromInput() {
        return SCANNER.next();
    }

    private static boolean emptyFlagForInputString(String field) {
        if(field.equals("0")){
            return true;
        }else{
            return false;
        }
    }


    private static void deleteContact() {
        System.out.println("Please Enter the ID of Contact:");
        int ID = getLongFromInput();
        try{
            CONTACTS_REPOSITORY.deleteContactByID(ID);
            System.out.println("Contact with ID="+ID+" successfully deleted.\n");
        } catch (Exception e){
            System.out.println("There was a problem in deleting contact.\n");

        }
    }

    private static void findContact() {
        System.out.println("Please enter first name:");
        String name = getStringFromInput();
        System.out.println("Please enter last name:");
        String family = getStringFromInput();
        Contact contact = null;
        try {
            contact = CONTACTS_REPOSITORY.findContactByNameAndFamily(name, family);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        if(contact == null){
            System.out.println("Contact not found.");
        } else {
            System.out.println("Contact successfully found:");
            System.out.println(contact.toString());
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

        try{
            CONTACTS_REPOSITORY.saveContact(newContact);
            System.out.println("Contact successfully saved.");
        }catch (Exception e){
            System.out.println("There was a problem in contact saving process.");
            e.printStackTrace();
        }
    }
}
