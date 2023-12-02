package com.ddang.ddang.user.application;

import com.ddang.ddang.image.domain.StoreImageProcessor;
import com.ddang.ddang.image.domain.dto.StoreImageDto;
import com.ddang.ddang.user.application.dto.ReadUserDto;
import com.ddang.ddang.user.application.dto.UpdateUserDto;
import com.ddang.ddang.user.application.exception.AlreadyExistsNameException;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StoreImageProcessor imageProcessor;

    public ReadUserDto readById(final Long userId) {
        final User user = userRepository.findByIdWithProfileImage(userId)
                                        .orElseThrow(() -> new UserNotFoundException("사용자 정보를 사용할 수 없습니다."));

        return ReadUserDto.from(user);
    }

    @Transactional
    public ReadUserDto updateById(final Long userId, final UpdateUserDto userDto) {
        final User user = userRepository.findByIdWithProfileImage(userId)
                                        .orElseThrow(() -> new UserNotFoundException("사용자 정보를 사용할 수 없습니다."));

        updateUserByRequest(userDto, user);

        return ReadUserDto.from(user);
    }

    private void updateUserByRequest(final UpdateUserDto userDto, final User user) {
        if (userDto.profileImage() != null) {
            final StoreImageDto storeImageDto = imageProcessor.storeImageFile(userDto.profileImage());
            user.updateProfileImage(storeImageDto.toProfileImageEntity());
        }
        if (userDto.name() != null) {
            validateAvailableName(userDto.name());
            user.updateName(userDto.name());
        }
    }

    private void validateAvailableName(final String name) {
        if (userRepository.existsByName(name)) {
            throw new AlreadyExistsNameException("이미 존재하는 닉네임입니다.");
        }
    }
}
