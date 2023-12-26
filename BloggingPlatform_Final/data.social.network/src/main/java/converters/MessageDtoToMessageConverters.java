package converters;

import dto.MessageDTO;
import entity.Message;
import org.springframework.core.convert.converters.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageDtoToMessageConverters implements Converters<MessageDTO, Message> {

    private final UserDtoToUserConverter userDtoToUserConverter;

    @Override
    public Message convert(MessageDTO messageDTO) {
        if(messageDTO == null) {
            return null;
        }

        return Message.builder()
                .time(messageDTO.getTime())
                .message(messageDTO.getMessage())
                .sender(userDtoToUserConverter.convert(messageDTO.getSender()))
                .receiver(userDtoToUserConverter.convert(messageDTO.getReceiver()))
                .build();
    }
}
