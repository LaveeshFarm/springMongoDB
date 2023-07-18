package ru.mrsu.springmongodb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mrsu.springmongodb.model.Client;
import ru.mrsu.springmongodb.service.ClientService;

import java.util.List;

@RestController
public class ClientController {

    @Autowired
    ClientService clientService;
    @GetMapping("/internal/client/show/all")
    ResponseEntity<List<Client>> getClients() {
        return ResponseEntity.ok(clientService.findClients());
    }

    @GetMapping("/internal/client/delete/all")
    void dropClients() {
        clientService.deleteClients();
    }

    @GetMapping("/internal/client/show/name/{name}")
    ResponseEntity<List<ClientService.ClientDTO.ClientNoId>> findByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(clientService.findByName(name));
    }

    @GetMapping("/internal/client/show/id/{id}")
    ResponseEntity<ClientService.ClientDTO.ClientNoId> findById(@PathVariable("id") String id) {
        return ResponseEntity.ok(clientService.findById(id));
    }

    @PostMapping("/internal/client/create")
    void createClient(@RequestBody ClientService.ClientDTO.ClientNoId client) {
        clientService.create(client);
    }


}
