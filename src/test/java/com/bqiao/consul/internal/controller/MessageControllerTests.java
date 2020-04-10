package com.bqiao.consul.internal.controller;

import com.bqiao.consul.internal.service.MessageService;
import com.bqiao.consul.internal.domain.Message;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MessageController.class)
public class MessageControllerTests {

    private static final String TEST_Message = "test-message";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MessageService service;

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    public void givenOneMessage_whenGetMessages_thenReturnJsonArray()
            throws Exception {

        Message dev = new Message();
        dev.setMessage(TEST_Message);

        given(service.getMessages()).willReturn(Collections.singletonList(dev));

        mvc.perform(get("/message")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].message", is(TEST_Message)));
    }

    @Test
    public void givenNewMessage_whenCreateMessage_thenReturnCreated()
            throws Exception {

        Message toCreate = new Message();
        toCreate.setMessage(TEST_Message);
        Message created = new Message();
        created.setMessage(TEST_Message);
        given(service.createMessage(toCreate)).willReturn(created);

        mvc.perform(post("/message")
                .content(mapper.writeValueAsString(toCreate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}
