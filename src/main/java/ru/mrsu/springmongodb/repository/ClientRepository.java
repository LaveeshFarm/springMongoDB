package ru.mrsu.springmongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.mrsu.springmongodb.model.Client;

import java.util.List;

public interface ClientRepository extends MongoRepository<Client, String> {
    Client findClientByBaseId(Integer id);
    List<Client> findClientByName(String name);
    Client findFirstByOrderByBaseIdDesc();
}
