package ru.mrsu.springmongodb.controller;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.mrsu.handler.exception.NotFoundApiException;
import ru.mrsu.springmongodb.config.MongoDBContainerBase;
import ru.mrsu.springmongodb.model.Client;
import ru.mrsu.springmongodb.model.ClientDTO;
import ru.mrsu.springmongodb.repository.ClientRepository;
import ru.mrsu.springmongodb.service.ClientService;
import ru.mrsu.springmongodb.service.impl.ClientServiceImpl;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@Import({ClientServiceImpl.class, ClientController.class})
public class ClientControllerTest extends MongoDBContainerBase {
    @Autowired
    ClientService clientService;
    @Autowired
    ClientController clientController;
    @Autowired
    ClientRepository clientRepository;

    @Test
    void getClients_StatusOKAndListEmpty_EmptyDB() {
        clientService.delete();
        ResponseEntity<List<Client>> response = clientController.getClients();
        Assertions.assertTrue(response.getStatusCode().equals(HttpStatus.OK) &&
                (response.getBody() == null || response.getBody().isEmpty()));
    }

    @Test
    void getClients_StatusOKAndListNotEmpty_NotEmptyDB() {
        clientService.create(new ClientDTO.ClientNoId("Misha", "AAA111"));
        ResponseEntity<List<Client>> response = clientController.getClients();
        Assertions.assertTrue(response.getStatusCode().equals(HttpStatus.OK) &&
                response.getBody() != null &&
                !response.getBody().isEmpty());
    }

    @Test
    void dropClients_StatusOK() {
        Assertions.assertTrue(clientController.dropClients().getStatusCode().equals(HttpStatus.OK));
    }
    @Test
    void findByName_ThrowNotFoundApiException_NameIsNull() {
        clientService.delete();
        Assertions.assertThrows(NotFoundApiException.class, () -> clientController.findByName(null));
    }

    @Test
    void findByName_ThrowNotFoundApiException_NameIsEmpty() {
        clientService.delete();
        Assertions.assertThrows(NotFoundApiException.class, () -> clientController.findByName(""));
    }
    @Test
    void findByName_StatusOKAndListEmpty_AnotherName() {
        clientService.delete();
        clientService.create(new ClientDTO.ClientNoId("Katya", "AAA111"));
        ResponseEntity<List<ClientDTO.ClientNoId>> response = clientController.findByName("Misha");

        Assertions.assertTrue(response.getStatusCode().equals(HttpStatus.OK) &&
                (response.getBody() == null || response.getBody().isEmpty()));
    }

    @Test
    void findByName_StatusOKAndListNotEmpty_TrulyName() {
        clientService.delete();
        clientService.create(new ClientDTO.ClientNoId("Katya", "AAA111"));
        ResponseEntity<List<ClientDTO.ClientNoId>> response = clientController.findByName("Katya");

        Assertions.assertTrue(response.getStatusCode().equals(HttpStatus.OK) &&
                !response.getBody().isEmpty());
    }


    @Test
    void findById_ThrowNotFoundApiException_IdIsNull() {
        clientService.delete();
        Assertions.assertThrows(NotFoundApiException.class, () -> clientController. findById(null));
    }

    @Test
    void findById_ThrowNotFoundApiException_IdIsEmpty() {
        clientService.delete();
        Assertions.assertThrows(NotFoundApiException.class, () -> clientController.findById(""));
    }

    @Test
    void findById_StatusOKAndListEmpty_AnotherId() {
        clientService.delete();
        clientRepository.save(new Client(ObjectId.get(), "Misha", "AAA111"));
        ResponseEntity<ClientDTO.ClientNoId> response = clientController.findById(ObjectId.get().toString());

        Assertions.assertTrue(response.getStatusCode().equals(HttpStatus.OK) &&
                response.getBody() == null);
    }

    @Test
    void findById_StatusOKAndListNotEmpty_TrulyId() {
        clientService.delete();
        ObjectId trulyId = ObjectId.get();
        clientRepository.save(new Client(trulyId, "Misha", "AAA111"));
        ResponseEntity<ClientDTO.ClientNoId> response = clientController.findById(trulyId.toString());

        Assertions.assertTrue(response.getStatusCode().equals(HttpStatus.OK) &&
                response.getBody() != null);
    }

    @Test
    void create_NotFoundApiException_ClientIsNull() {
        Assertions.assertThrows(NotFoundApiException.class, () -> clientController.createClient(null));
    }

    @Test
    void create_ClientCreated() {
        clientService.delete();
        List<Client> before = clientService.findClients();
        ResponseEntity<Void> response = clientController.createClient(new ClientDTO.ClientNoId("Misha", "AAA111"));
        List<Client> after = clientService.findClients();
        Assertions.assertTrue(response.getStatusCode().equals(HttpStatus.OK) && !before.equals(after));
    }

    @Test
    void create_ClientNotCreated() {
        clientService.delete();
        clientService.create(new ClientDTO.ClientNoId("Misha", "AAA111"));
        List<Client> before = clientService.findClients();
        ResponseEntity<Void> response = clientController.createClient(new ClientDTO.ClientNoId("Misha", "AAA111"));
        List<Client> after = clientService.findClients();
        Assertions.assertTrue(response.getStatusCode().equals(HttpStatus.OK) && before.equals(after));
    }
}
