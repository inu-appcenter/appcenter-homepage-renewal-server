package server.inuappcenter.kr.data.dto.response;

import lombok.Builder;
import lombok.Getter;
import server.inuappcenter.kr.data.domain.Stack;

import javax.servlet.http.HttpServletRequest;

@Getter
public class StackResponseDto {
    private final Long id;
    private final String name;
    private final String icon;

    @Builder
    private StackResponseDto(Long id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public static StackResponseDto entityToDto(Stack stack, HttpServletRequest request) {
        String icon = null;
        if (stack.getIcon() != null) {
            icon = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + "/image/photo/" + stack.getIcon().getId();
        }
        return StackResponseDto.builder()
                .id(stack.getId())
                .name(stack.getName())
                .icon(icon)
                .build();
    }
}