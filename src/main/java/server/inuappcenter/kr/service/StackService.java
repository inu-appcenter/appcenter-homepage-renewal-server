package server.inuappcenter.kr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.domain.Stack;
import server.inuappcenter.kr.data.domain.board.Image;
import server.inuappcenter.kr.data.dto.request.StackRequestDto;
import server.inuappcenter.kr.data.dto.response.StackResponseDto;
import server.inuappcenter.kr.data.repository.StackRepository;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StackService {
    private final StackRepository stackRepository;
    private final HttpServletRequest request;

    @Transactional(readOnly = true)
    public StackResponseDto findStack(Long id) {
        Stack stack = stackRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("해당 ID의 스택을 찾을 수 없습니다."));
        return StackResponseDto.entityToDto(stack, request);
    }

    @Transactional(readOnly = true)
    public List<StackResponseDto> findAllStacks() {
        return stackRepository.findAll().stream()
                .map(stack -> StackResponseDto.entityToDto(stack, request))
                .collect(Collectors.toList());
    }

    @Transactional
    public CommonResponseDto saveStack(StackRequestDto requestDto) {
        Image icon = null;
        if (requestDto.getIconImage() != null && !requestDto.getIconImage().isEmpty()) {
            icon = new Image(requestDto.getIconImage());
        }
        Stack stack = new Stack(requestDto.getName(), icon);
        Stack savedStack = stackRepository.save(stack);
        return new CommonResponseDto("Stack ID : " + savedStack.getId() + "이 저장되었습니다.");
    }

    @Transactional
    public CommonResponseDto updateStack(Long id, StackRequestDto requestDto) {
        Stack stack = stackRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("해당 ID의 스택을 찾을 수 없습니다."));

        Image newIcon = null;
        if (requestDto.getIconImage() != null && !requestDto.getIconImage().isEmpty()) {
            newIcon = new Image(requestDto.getIconImage());
        }
        stack.updateStack(requestDto.getName(), newIcon);
        stackRepository.save(stack);
        return new CommonResponseDto("Stack ID : " + id + "이 수정되었습니다.");
    }

    @Transactional
    public CommonResponseDto deleteStack(Long id) {
        stackRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("해당 ID의 스택을 찾을 수 없습니다."));
        stackRepository.deleteById(id);
        return new CommonResponseDto("Stack ID : " + id + "이 삭제되었습니다.");
    }
}