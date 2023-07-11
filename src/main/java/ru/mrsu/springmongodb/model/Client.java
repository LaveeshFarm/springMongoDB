package ru.mrsu.springmongodb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("client")
public class Client {

    @Id
    private ObjectId id;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer baseId;

    private String name;

    private String number;

    public Client(ObjectId id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }
}
