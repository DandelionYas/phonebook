package phonebook.data.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "contact")
@Access(value = AccessType.PROPERTY)
public class Contact implements Serializable {

    private int id;
    private String firstName;
    private String lastName;
    private String mobileNum;
    private String phoneNum;
    private String address;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "firstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "lastName")
    public String getLastName() { return lastName; }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "mobileNum")
    public String getMobileNum() {        return mobileNum;    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    @Column(name = "phoneNum")
    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (!firstName.equals(contact.firstName)) return false;
        if (lastName != null ? !lastName.equals(contact.lastName) : contact.lastName != null) return false;
        if (!mobileNum.equals(contact.mobileNum)) return false;
        if (phoneNum != null ? !phoneNum.equals(contact.phoneNum) : contact.phoneNum != null) return false;
        return address != null ? address.equals(contact.address) : contact.address == null;
    }

    @Override
    public String toString() {
        return "Contact:{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobileNum='" + mobileNum + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + mobileNum.hashCode();
        result = 31 * result + (phoneNum != null ? phoneNum.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }
}