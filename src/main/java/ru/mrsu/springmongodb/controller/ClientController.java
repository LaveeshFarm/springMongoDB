package ru.mrsu.springmongodb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    ResponseEntity<?> getClients() {
        return clientService.findClients();
    }

    @GetMapping("/internal/client/delete/all")
    ResponseEntity<?> dropClients() {
        return clientService.deleteClients();
    }

    @GetMapping("/internal/client/show/name/{name}")
    ResponseEntity<?> findByName(@PathVariable("name") String name) {
        return clientService.findByName(name);
    }

    @GetMapping("/internal/client/show/id/{id}")
    ResponseEntity<?> findById(@PathVariable("id") String id) {
        return clientService.findById(id);
    }

    @PostMapping("/internal/client/create")
    ResponseEntity<?> createClient(@RequestBody ClientService.ClientDTO.ClientNoId client) {
        return clientService.create(client);
    }


}
