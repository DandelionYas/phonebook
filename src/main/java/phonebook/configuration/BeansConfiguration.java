package phonebook.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import phonebook.exception.Contact;
import phonebook.model.ContactsRepository;

import java.util.ArrayList;

@Configuration
public class BeansConfiguration {

    @Bean
    public ContactsRepository contactsRepository(){
        ArrayList<Contact> contactsArrayList = new ArrayList<>();
        return new ContactsRepository(null);
    }

    @Bean
    public Contact createPersonBean() {
        return  new Contact();
    }

}
