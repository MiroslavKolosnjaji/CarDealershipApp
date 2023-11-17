package cardealershipapp.common.domain;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class User implements Serializable  {

    private Long id;
    private String firstName;
    private String lastName;
    LocalDate DateOfBirth;
    private Gender gender;
    private City residence;

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, String firstName, String lastName, LocalDate DateOfBirth, Gender gender, City residence) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.DateOfBirth = DateOfBirth;
        this.gender = gender;
        this.residence = residence;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(LocalDate DateOfBirth) {
        this.DateOfBirth = DateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public City getResidence() {
        return residence;
    }

    public void setResidence(City residence) {
        this.residence = residence;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User{");
        sb.append("id=").append(id);
        sb.append(", firstName=").append(firstName);
        sb.append(", lastName=").append(lastName);
        sb.append(", DateOfBirth=").append(DateOfBirth);
        sb.append(", gender=").append(gender);
        sb.append(", residence=").append(residence);
        sb.append('}');
        return sb.toString();
    }

}
