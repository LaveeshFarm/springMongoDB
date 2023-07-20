package ru.mrsu.springmongodb.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mrsu.handler.exception.NotFoundApiException;
import ru.mrsu.springmongodb.model.Client;
import ru.mrsu.springmongodb.repository.ClientRepository;
import ru.mrsu.springmongodb.service.ClientService;
import ru.mrsu.springmongodb.model.ClientDTO;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    public void create(ClientDTO.ClientNoId client) {
        if(client != null) {
            Client oldClient = clientRepository.findByNameAndNumber(client.getName(), client.getNumber());
            Client newClient = new Client(ObjectId.get(), client.getName(), client.getNumber());

            if(oldClient == null ||
                    !(oldClient.getName().equals(newClient.getName()) &&
                            oldClient.getNumber().equals(newClient.getNumber()))) {
                clientRepository.save(newClient);
            }
            else {
                log.error("Client with same name and number exists.");
            }
        }
        else {
            log.error("Client wasn't created because request body is null");
            throw NotFoundApiException.Builder.notFoundApiException().build();
        }
    }

    public ClientDTO.ClientNoId findById(String id) {
        if (id != null && !id.equals("")) {
            Client baseClient = clientRepository.findClientById(id);

            if(baseClient != null) {
                return new ClientDTO.ClientNoId(baseClient.getName(), baseClient.getNumber());
            } else {
                return null;
            }
        }
        log.error("Can't find client by ID because ID is null");
        throw NotFoundApiException.Builder.notFoundApiException().build();
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
            }
            log.error("No clients name {} found", name);
            return null;
        }
        log.error("Can't find client by ID because ID is null");
        throw NotFoundApiException.Builder.notFoundApiException().build();
    }

    public void delete() {
        try {
            clientRepository.deleteAll();
        } catch (Exception e) {
            throw NotFoundApiException.Builder.notFoundApiException().message(e.toString()).build();
        }
    }

    public List<Client> findClients() {
        try {
            List<Client> clients = clientRepository.findAll();
            return clients;
        }
        catch (Exception e) {
            throw NotFoundApiException.Builder.notFoundApiException().message(e.toString()).build();
        }
    }
}
