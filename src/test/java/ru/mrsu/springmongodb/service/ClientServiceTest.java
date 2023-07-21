package ru.mrsu.springmongodb.service;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import ru.mrsu.handler.exception.NotFoundApiException;
import ru.mrsu.springmongodb.config.MongoDBContainerBase;
import ru.mrsu.springmongodb.model.Client;
import ru.mrsu.springmongodb.model.ClientNoId;
import ru.mrsu.springmongodb.repository.ClientRepository;
import ru.mrsu.springmongodb.service.impl.ClientServiceImpl;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@Import(ClientServiceImpl.class)
public class ClientServiceTest extends MongoDBContainerBase {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ClientService clientService;

    @Test
    void createTest_NotFoundApiException_ClientIsNull() {
        Assertions.assertThrows(NotFoundApiException.class, () -> clientService.create(null));
    }
    @Test
    void createTest_createClient() {
        clientRepository.deleteAll();
        List<Client> before = clientRepository.findAll();
        clientService.create(new ClientNoId("Misha", "FFF111"));
        List<Client> after = clientRepository.findAll();
        Assertions.assertNotEquals(before, after);
    }

    @Test
    void createTest_NotCreateClient_ClientIsAlreadyExists() {
        clientRepository.deleteAll();
        clientRepository.save(new Client(ObjectId.get(), "Misha", "FFF111"));
        List<Client> before = clientRepository.findAll();
        clientService.create(new ClientNoId("Misha", "FFF111"));
        List<Client> after = clientRepository.findAll();
        Assertions.assertEquals(before, after);
    }

    @Test
    void findByNameTest_NotFind_EmptyDB() {
        clientRepository.deleteAll();
        List<ClientNoId> clients = clientService.findByName("Misha");
        Assertions.assertTrue(clients == null || clients.isEmpty());
    }

    @Test
    void findByNameTest_NotFind_AnotherName() {
        clientRepository.deleteAll();
        clientRepository.save(new Client(ObjectId.get(), "Katya", "AAA111"));
        List<ClientNoId> clients = clientService.findByName("Misha");
        Assertions.assertTrue(clients == null || clients.isEmpty());
    }

    @Test
    void findByNameTest_FindName() {
        clientRepository.deleteAll();
        clientRepository.save(new Client(ObjectId.get(), "Misha", "AAA111"));
        List<ClientNoId> clients = clientService.findByName("Misha");
        Assertions.assertTrue(clients != null && !clients.isEmpty());
    }

    @Test
    void delete_DeleteAllClients() {
        clientRepository.save(new Client(ObjectId.get(), "Misha", "AAA111"));
        clientService.delete();
        Assertions.assertTrue(clientRepository.findAll().isEmpty());
    }

    @Test
    void findByIdTest_NotFind_EmptyDB() {
        clientRepository.deleteAll();
        Assertions.assertNull(clientService.findById(ObjectId.get().toString()));
    }

    @Test
    void findByIdTest_NotFind_AnotherId() {
        clientRepository.deleteAll();
        ObjectId trulyId = ObjectId.get();
        clientRepository.save(new Client(trulyId, "Katya", "AAA111"));
        Assertions.assertNull(clientService.findById(ObjectId.get().toString()));
    }

    @Test
    void findByIdTest_Find_TrulyId() {
        clientRepository.deleteAll();
        ObjectId trulyId = ObjectId.get();
        clientRepository.save(new Client(trulyId, "Misha", "AAA111"));
        Assertions.assertNotNull(clientService.findById(trulyId.toString()));
    }
}
