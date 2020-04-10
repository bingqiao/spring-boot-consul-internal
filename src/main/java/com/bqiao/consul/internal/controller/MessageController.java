package com.bqiao.consul.internal.controller;

import com.bqiao.consul.internal.domain.Message;
import com.bqiao.consul.internal.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

/**
 * Operations for Managing Messages
 */
@CrossOrigin
@RestController
@RequestMapping("message")
public class MessageController {
    private final MessageService service;

    public MessageController(
            MessageService service) {
        this.service = service;
    }

    @GetMapping(path = "")
    public ResponseEntity<List<Message>> getMessages() {
        List<Message> devs = service.getMessages();
        return new ResponseEntity<>(devs, OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable String id) {
        Message message = service.getMessage(id);
        return new ResponseEntity<>(message, OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Message> deleteMessage(@PathVariable String id) {
        service.deleteMessage(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable String id, @RequestBody Message toUpdate) {
        Message message = service.updateMessage(id, toUpdate);
        return new ResponseEntity<>(message, OK);
    }

    @PostMapping(path = "")
    public ResponseEntity<Message> createMessage(@RequestBody Message toCreate) {
        Message message = service.createMessage(toCreate);
        return new ResponseEntity<>(message, CREATED);
    }

    @GetMapping(path = "/ping")
    public String ping() {
        return "pong - internal";
    }
}
