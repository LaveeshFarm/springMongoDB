package ru.mrsu.springmongodb.service;

import ru.mrsu.springmongodb.model.Client;
import ru.mrsu.springmongodb.model.ClientNoId;

import java.util.List;

public interface ClientService {
    public void create(ClientNoId client);
    public ClientNoId findById(String id);
    public List<ClientNoId> findByName(String name);
    public void delete();
    public List<Client> findClients();
}
