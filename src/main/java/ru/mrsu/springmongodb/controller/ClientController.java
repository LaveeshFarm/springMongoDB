package ru.mrsu.springmongodb.controller;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mrsu.springmongodb.model.Client;
import ru.mrsu.springmongodb.repository.ClientRepository;

import java.util.List;

@RestController
@Slf4j
public class ClientController {

    @Autowired
    ClientRepository clientRepository;

    @GetMapping("/internal/client/show/all")
    List<Client> getClients() {
        log.trace("Method getClients() of class {} was called", ClientController.class);

        return clientRepository.findAll();
    }

    @GetMapping("/internal/client/delete/all")
    void dropClients() {
        log.trace("Method dropClients() of class {} was called", ClientController.class);
        clientRepository.deleteAll();
    }

    @GetMapping("/internal/client/show/name/{name}")
    List<Client> findByName(@PathVariable("name") String name) {
        log.trace("Method findByName() of class {} was called", ClientController.class);
        return clientRepository.findClientByName(name);
    }

    @GetMapping("/internal/client/show/id/{id}")
    ClientDTO.ClientNoId findById(@PathVariable("id") Integer id) {
        log.trace("Method findById() of class {} was called", ClientController.class);
        Client baseClient = clientRepository.findClientByBaseId(id);
        return new ClientDTO.ClientNoId(baseClient.getBaseId(), baseClient.getName(), baseClient.getNumber());
    }

    @GetMapping("/internal/client/create")
    void createClient(@RequestParam(name = "name") String name, @RequestParam(name = "number") String number) {
        log.trace("Method createClient() of class {} was called", ClientController.class);
        Client lastIdClient = clientRepository.findFirstByOrderByBaseIdDesc();
        Integer id;
        if(lastIdClient.getBaseId() != null) {
            id = clientRepository.findFirstByOrderByBaseIdDesc().getBaseId() + 1;
        } else {
            id = 1;
        }

        Client client = null;
        if(id != null) {
            client = new Client(ObjectId.get(), id, name, number);
        } else {
            client = new Client(ObjectId.get(), name, number);
        }

        try {
            clientRepository.save(client);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Method createClient() of class {} can't save client in database", ClientController.class);
        }
        log.info("Client was added to MongoDB repository");
    }

    public enum ClientDTO{;
        private interface Id {ObjectId getId();}
        private interface BaseId {Integer getBaseId();}
        private interface Name {String getName();}
        private interface Number {String getNumber();}

        @Value
        public static class ClientNoId implements BaseId, Name, Number {
            Integer baseId;
            String name;
            String number;
        }
    }
}
