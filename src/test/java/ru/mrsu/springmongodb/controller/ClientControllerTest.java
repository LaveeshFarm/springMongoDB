package ru.mrsu.springmongodb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.mrsu.springmongodb.config.MongoDBContainerBase;
import ru.mrsu.springmongodb.model.Client;
import ru.mrsu.springmongodb.model.ClientNoId;
import ru.mrsu.springmongodb.repository.ClientRepository;
import ru.mrsu.springmongodb.service.ClientService;
import ru.mrsu.springmongodb.service.impl.ClientServiceImpl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({ClientServiceImpl.class, ClientController.class})
//@AutoConfigureMockMvc
@WebMvcTest(ClientController.class)

@AutoConfigureMockMvc
@AutoConfigureDataMongo
@ContextConfiguration
public class ClientControllerTest extends MongoDBContainerBase {
    @Autowired
    ClientService clientService;
    @Autowired
    ClientController clientController;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    private MockMvc mvc;

    @Test
    void getClients_StatusOKAndListEmpty_EmptyDB() throws Exception {
        clientService.delete();
        mvc.perform(get("/internal/client/show/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"));
    }

    @Test
    void getClients_StatusOKAndListNotEmpty_NotEmptyDB() throws Exception {
        clientService.create(new ClientNoId("Misha", "AAA111"));
        mvc.perform(get("/internal/client/show/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Misha"));
    }

    @Test
    void dropClients_StatusOK() throws Exception {
        mvc.perform(get("/internal/client/delete/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void findByName_ThrowNotFoundApiException_NameIsNull() throws Exception {
        clientService.delete();
        mvc.perform(get("/internal/client/show/name/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

//    @Test
//    void findByName_ThrowNotFoundApiException_NameIsEmpty() {
//        clientService.delete();
//        Assertions.assertThrows(NotFoundApiException.class, () -> clientController.findByName(""));
//    }
    @Test
    void findByName_StatusOKAndListEmpty_AnotherName() throws Exception {
        clientService.delete();
        clientService.create(new ClientNoId("Katya", "AAA111"));
//        ResponseEntity<List<ClientNoId()>> response = clientController.findByName("Misha");
//        Assertions.assertTrue(response.getStatusCode().equals(HttpStatus.OK) &&
//                (response.getBody() == null || response.getBody().isEmpty()));
        mvc.perform(get("/internal/client/show/name/Misha")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void findByName_StatusOKAndListNotEmpty_TrulyName() throws Exception {
        clientService.delete();
        clientService.create(new ClientNoId("Katya", "AAA111"));
        mvc.perform(get("/internal/client/show/name/Katya")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Katya"));
    }


    @Test
    void findById_ThrowNotFoundApiException_IdIsNull() throws Exception {
        clientService.delete();
//        Assertions.assertThrows(NotFoundApiException.class, () -> clientController. findById(null));
        mvc.perform(get("/internal/client/show/id/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

//    @Test
//    void findById_ThrowNotFoundApiException_IdIsEmpty() {
//        clientService.delete();
//        Assertions.assertThrows(NotFoundApiException.class, () -> clientController.findById(""));
//    }

    @Test
    void findById_StatusOKAndListEmpty_AnotherId() throws Exception {
        clientService.delete();
        clientRepository.save(new Client(ObjectId.get(), "Misha", "AAA111"));
        mvc.perform(get("/internal/client/show/id/" + ObjectId.get())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empty").value("true"));
    }

    @Test
    void findById_StatusOKAndListNotEmpty_TrulyId() throws Exception {
        clientService.delete();
        ObjectId trulyId = ObjectId.get();
        clientRepository.save(new Client(trulyId, "Misha", "AAA111"));
        mvc.perform(get("/internal/client/show/id/" + trulyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Misha"));
    }

    @Test
    void create_NotFoundApiException_ClientIsNull() throws Exception {
        mvc.perform(post("/internal/client/create")
                        .content(asJsonString(new ClientNoId(null, null)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
//                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof NotFoundApiException))
//                .andExpect(result -> Assertions.assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), result.getResolvedException().getMessage()));
//                .andExpect(result -> Assertions.assertEquals("Client wasn't created because request body is null", result.getResolvedException().getMessage()));
    }

    private static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void create_ClientCreated() throws Exception {
        clientService.delete();
        mvc.perform(post("/internal/client/create")
                        .content(asJsonString(new ClientNoId("Misha", "AAA111")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    void create_AlreadyExistsException_ClientAlreadyExists() throws Exception {
        clientService.delete();
        clientService.create(new ClientNoId("Misha", "AAA111"));
        mvc.perform(post("/internal/client/create")
                        .content(asJsonString(new ClientNoId("Misha", "AAA111")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }
}
