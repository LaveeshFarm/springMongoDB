package ru.mrsu.springmongodb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mrsu.springmongodb.model.Client;
import ru.mrsu.springmongodb.service.impl.ClientServiceImpl;
import ru.mrsu.springmongodb.model.ClientDTO;

import java.util.List;

@RestController
public class ClientController {

    @Autowired
    ClientServiceImpl clientService;
    @GetMapping("/internal/client/show/all")
    ResponseEntity<List<Client>> getClients() {
        return ResponseEntity.ok(clientService.findClients());
    }

    @GetMapping("/internal/client/delete/all")
    ResponseEntity<Void> dropClients() {
        clientService.delete();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/internal/client/show/name/{name}")
    ResponseEntity<List<ClientDTO.ClientNoId>> findByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(clientService.findByName(name));
    }

    @GetMapping("/internal/client/show/id/{id}")
    ResponseEntity<ClientDTO.ClientNoId> findById(@PathVariable("id") String id) {
        return ResponseEntity.ok(clientService.findById(id));
    }

    @PostMapping("/internal/client/create")
    ResponseEntity<Void> createClient(@RequestBody ClientDTO.ClientNoId client) {
        clientService.create(client);
        return ResponseEntity.ok().build();
    }
}
