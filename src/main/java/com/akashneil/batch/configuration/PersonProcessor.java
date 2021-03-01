package com.akashneil.batch.configuration;

import com.akashneil.batch.dao.entity.Person;
import org.springframework.batch.item.ItemProcessor;

public class PersonProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process (final Person person) {
        final String firstName = person.getFirstName();
        final String lastName = person.getLastName();

        final Person processedPerson = new Person();
        processedPerson.setFirstName(firstName);
        processedPerson.setLastName(lastName);
        return processedPerson;
    }

}
