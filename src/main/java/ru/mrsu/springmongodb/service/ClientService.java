package ru.mrsu.springmongodb.service;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void create(ClientDTO.ClientNoId client) {
        if(client != null) {
            Client oldClient = clientRepository.findByNameAndNumber(client.getName(), client.getNumber());
            Client newClient = new Client(ObjectId.get(), client.getName(), client.getNumber());
            try {
                if(oldClient == null || !newClient.equals(oldClient)) {
                    clientRepository.save(newClient);
                }

                else {
                    log.error("Client with same name and number exists.");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                log.error("Method createClient() of class {} can't save client in database", ClientController.class);
            }
        }
        else {
            log.error("Client wasn't created because request body is null");
        }
    }

    public ClientService.ClientDTO.ClientNoId findById(String id) {
        if (id != null && !id.equals("")) {
            Client baseClient = clientRepository.findClientById(id);

            if(baseClient != null)
                return new ClientService.ClientDTO.ClientNoId( baseClient.getName(), baseClient.getNumber());
            return null;
        }
        else {
            log.error("Can't find client by ID because ID is null");
            return null;
        }
    }

    public List<ClientDTO.ClientNoId> findByName(String name) {
        if (name != null && !name.equals("")) {
            List<Client> listClients = clientRepository.findClientByName(name);
            if (!listClients.isEmpty()) {
                List<ClientDTO.ClientNoId> clientNoIdList = new ArrayList<>();
                listClients.forEach(client -> {
                    clientNoIdList.add(new ClientDTO.ClientNoId(client.getName(), client.getNumber()));
                });
                return clientNoIdList;
            } else {
                log.error("No clients name {} found", name);
                return null;
            }
        }
        else {
            log.error("Can't find client by ID because ID is null");
            return null;
        }
    }

    public void deleteClients() {
        try {
            clientRepository.deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Client> findClients() {
        List<Client> clients = clientRepository.findAll();
        return clients;
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
