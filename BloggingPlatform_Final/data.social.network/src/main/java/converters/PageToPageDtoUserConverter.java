package converters;

import dto.PageDTO;
import dto.UserDTO;
import entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PageToPageDtoUserConverter implements Converter<Page<User>, PageDTO<UserDTO>> {

    private final UserToUserDtoConverter userToUserDtoConverter;

    @Override
    public PageDTO<UserDTO> convert(Page<User> page) {
        List<UserDTO> list = page.getContent().stream()
                .map(e -> userToUserDtoConverter.convert(e))
                .collect(Collectors.toList());

        return PageDTO.<UserDTO>builder()
                .content(list)
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }

}
