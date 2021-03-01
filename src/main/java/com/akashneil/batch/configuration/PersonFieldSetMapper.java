package com.akashneil.batch.configuration;

import com.akashneil.batch.dao.entity.Person;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;

@Component
public class PersonFieldSetMapper implements FieldSetMapper<Person> {

    @Override
    public Person mapFieldSet(FieldSet fieldSet) {
        final Person person = new Person();

        person.setFirstName(fieldSet.readString("firstName"));
        person.setLastName(fieldSet.readString("lastName"));
        return person;

    }

}
