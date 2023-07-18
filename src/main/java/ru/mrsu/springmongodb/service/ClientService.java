package ru.mrsu.springmongodb.service;

import ru.mrsu.springmongodb.model.Client;
import ru.mrsu.springmongodb.model.ClientDTO;
import ru.mrsu.springmongodb.service.impl.ClientServiceImpl;

import java.util.List;

public interface ClientService {
    public void create(ClientDTO.ClientNoId client);
    public ClientDTO.ClientNoId findById(String id);
    public List<ClientDTO.ClientNoId> findByName(String name);
    public void delete();
    public List<Client> findClients();
}
