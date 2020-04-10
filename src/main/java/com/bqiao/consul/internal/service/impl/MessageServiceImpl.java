package com.bqiao.consul.internal.service.impl;

import com.bqiao.consul.internal.domain.Message;
import com.bqiao.consul.internal.service.MessageService;
import com.bqiao.consul.internal.repo.jpa.RepositoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

	private final RepositoryService repoService;

	public MessageServiceImpl(
			RepositoryService repoService) {
		this.repoService = repoService;
	}

	@Override
	public List<Message> getMessages() {
		return this.repoService.getMessageRepo().findAll();
	}

	@Override
	public Message getMessage(String id) {
		Optional<Message> found = findMessage(id);
		return found.get();
	}

	@Override
	public void deleteMessage(String id) {
		Optional<Message> found = findMessage(id);
		this.repoService.getMessageRepo().delete(found.get());
	}

	@Override
	public Message updateMessage(String id, Message toUpdate) {
		Optional<Message> found = findMessage(id);
		found.get().setMessage(toUpdate.getMessage());
		return this.repoService.getMessageRepo().save(found.get());
	}

	@Override
	public Message createMessage(Message toCreate) {
		if (toCreate == null || StringUtils.isEmpty(toCreate.getMessage())) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, "Invalid object");
		}
		return this.repoService.getMessageRepo().save(toCreate);
	}

	private Optional<Message> findMessage(String id) {
		Optional<Message> found = this.repoService.getMessageRepo().findById(id);
		if (found.isEmpty()) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "Object not found");
		}
		return found;
	}
}
