package ru.mrsu.springmongodb.service;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.mrsu.springmongodb.controller.ClientController;
import ru.mrsu.springmongodb.model.Client;
import ru.mrsu.springmongodb.repository.ClientRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    public ResponseEntity<?> create(ClientDTO.ClientNoId client) {
        if(client != null) {
            Client oldClient = clientRepository.findByNameAndNumber(client.getName(), client.getNumber());
            Client newClient = new Client(ObjectId.get(), client.getName(), client.getNumber());
            try {
                if(oldClient == null || !newClient.equals(oldClient)) {
                    clientRepository.save(newClient);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                }

                else {
                    log.error("Client with same name and number exists.");
                    return new ResponseEntity<>(HttpStatus.FOUND);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                log.error("Method createClient() of class {} can't save client in database", ClientController.class);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        else {
            log.error("Client wasn't created because request body is null");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    public ResponseEntity<?> findById(String id) {
        if (id != null && !id.equals("")) {
            Client baseClient = clientRepository.findClientById(id);

            if(baseClient != null)
                return new ResponseEntity<>(new ClientService.ClientDTO.ClientNoId( baseClient.getName(), baseClient.getNumber()), HttpStatus.OK);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            log.error("Can't find client by ID because ID is null");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    public ResponseEntity<?> findByName(String name) {
        if (name != null && !name.equals("")) {
            List<Client> listClients = clientRepository.findClientByName(name);
            if (!listClients.isEmpty()) {
                List<ClientDTO.ClientNoId> clientNoIdList = new ArrayList<>();
                listClients.forEach(client -> {
                    clientNoIdList.add(new ClientDTO.ClientNoId(client.getName(), client.getNumber()));
                });
                return new ResponseEntity<>(clientNoIdList, HttpStatus.OK);
            } else {
                log.error("No clients name {} found", name);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        else {
            log.error("Can't find client by ID because ID is null");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    public ResponseEntity<?> deleteClients() {
        try {
            clientRepository.deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> findClients() {
        List<Client> clients = clientRepository.findAll();
        if(clients == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    public enum ClientDTO{;
        private interface Id {ObjectId getId();}
        private interface Name {String getName();}
        private interface Number {String getNumber();}

        @Value
        public static class ClientNoId implements Name, Number {
            String name;
            String number;
        }
    }
}
