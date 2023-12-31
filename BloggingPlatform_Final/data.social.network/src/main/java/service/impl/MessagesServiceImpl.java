package service.impl;

import converters.MessageDtoToMessageConverters;
import converters.MessageToMessageDtoConverter;
import dto.MessageDTO;
import entity.Message;
import entity.User;
import repository.MessageRepository;
import service.MessagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MessagesServiceImpl implements MessagesService {

    private final MessageRepository messageRepository;
    private final MessageToMessageDtoConverter messageToMessageDtoConverter;
    private final MessageDtoToMessageConverter messageDtoToMessageConverter;

    @Override
    @Transactional(readOnly = true)
    public Collection<MessageDTO> findAllRecentMessages(Long id) {
        Iterable<Message> all = messageRepository.findAllRecentMessages(id);
        Map<User, MessageDTO> map = new HashMap<>();
        all.forEach(m -> {
            MessageDTO messageDTO = messageToMessageDtoConverter.convert(m, id);
            User user = m.getSender().getId().equals(id) ? m.getReceiver() : m.getSender();
            map.put(user, messageDTO);
        });
        return map.values();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageDTO> findConversation(Long userId, Long companionId) {
        List<Message> all = messageRepository.findConversation(userId, companionId);
        List<MessageDTO> messages = new LinkedList<>();
        all.forEach(m -> messages.add(messageToMessageDtoConverter.convert(m, userId)));
        return messages;
    }

    @Override
    @Transactional(readOnly = true)
    public MessageDTO getRecentMessage(Long id) {
        Message message = messageRepository.findFirstBySenderIdOrReceiverIdOrderByIdDesc(id, id);
        MessageDTO messageDTO = messageToMessageDtoConverter.convert(message, id);
        return messageDTO;
    }

    @Override
    @Transactional
    public void postMessage(MessageDTO messageDTO) {
        Message message = messageDtoToMessageConverter.convert(messageDTO);
        messageRepository.save(message);
    }


}
