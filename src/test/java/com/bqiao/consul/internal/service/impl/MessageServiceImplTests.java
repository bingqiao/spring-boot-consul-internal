package com.bqiao.consul.internal.service.impl;

import com.bqiao.consul.internal.domain.Message;
import com.bqiao.consul.internal.repo.jpa.MessageRepo;
import com.bqiao.consul.internal.repo.jpa.RepositoryService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class MessageServiceImplTests {

    private static final String TEST_MESSAGE = "test-message";
    private static final String TEST_MESSAGE_ID = "test-message-id";

    @MockBean
    private MessageRepo messageRepo;

    @MockBean
    private RepositoryService repoService;

    private MessageServiceImpl underTest;

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Before
    public void beforeEach() {
        underTest = new MessageServiceImpl(repoService);
        given(repoService.getMessageRepo()).willReturn(messageRepo);
    }

    @Test
    public void givenOneMessage_whenGetMessages_thenReturnOneMessage() {
        List<Message> messages = new ArrayList<>();
        Message message = new Message();
        messages.add(message);
        given(messageRepo.findAll()).willReturn(messages);
        List<Message> retrieved = underTest.getMessages();
    }

    @Test
    public void givenNewMessage_whenCreateMessage_thenReturnCreatedMessage() {
        Message toCreate = new Message();

        toCreate.setMessage(TEST_MESSAGE);

         given(messageRepo.save(any())).willAnswer(invocation -> invocation.getArgument(0));

        Message created = underTest.createMessage(toCreate);
        then(messageRepo).should().save(created);
    }

    @Test
    public void givenNullMessage_whenCreateMessage_thenThrowException() {
        thrown.expect(ResponseStatusException.class);
        thrown.expectMessage("400 BAD_REQUEST \"Invalid object\"");
        underTest.createMessage(null);
    }

    @Test
    public void givenMessageWithInvalidMessage_whenCreateMessage_thenThrowException() {
        thrown.expect(ResponseStatusException.class);
        thrown.expectMessage("400 BAD_REQUEST \"Invalid object\"");
        Message message = new Message();
        underTest.createMessage(message);
    }

    @Test
    public void givenMessageAbsent_whenDeleteMessage_thenThrowException() {
        given(messageRepo.findById(TEST_MESSAGE_ID)).willReturn(Optional.empty());
        thrown.expect(ResponseStatusException.class);
        thrown.expectMessage("404 NOT_FOUND \"Object not found\"");
        underTest.deleteMessage(TEST_MESSAGE_ID);
    }

    @Test
    public void givenMessage_whenDeleteMessage_thenExpectSuccess() {
        Optional<Message> message = Optional.of(new Message());
        given(messageRepo.findById(TEST_MESSAGE_ID)).willReturn(message);
        underTest.deleteMessage(TEST_MESSAGE_ID);
        then(messageRepo).should().findById(TEST_MESSAGE_ID);
        then(messageRepo).should().delete(message.get());
    }
}
