package me.lozm.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.lozm.domain.user.entity.User;
import me.lozm.global.code.UseYn;
import me.lozm.global.code.UsersType;
import me.lozm.global.object.dto.BaseUserDto;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;

public class UserDto {

    @Getter
    @Builder
    public static class ResponseListInfo {
        private Long id;
        private String name;
        private String identifier;
        private UsersType type;
        private UseYn use;

        public static ResponseListInfo from(User user) {
            return ResponseListInfo.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .identifier(user.getIdentifier())
                    .type(user.getType())
                    .use(user.getUse())
                    .build();
        }
    }

    @Getter
    public static class ResponseList {
        Page<ResponseListInfo> userList;

        public static ResponseList createUserList(Page<User> userList) {
            ResponseList list = new ResponseList();
            list.userList = userList.map(ResponseListInfo::from);
            return list;
        }
    }

    @Getter
    @Builder
    public static class ResponseOne {
        private Long id;
        private String name;
        private String identifier;
        private UsersType type;
        private UseYn use;


        public static ResponseOne from(User user) {
            return ResponseOne.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .identifier(user.getIdentifier())
                    .type(user.getType())
                    .use(user.getUse())
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    public static class AddRequest extends BaseUserDto {
        @NotNull
        private String name;

        @NotNull
        private String identifier;

        @NotNull
        private String password;

        @NotNull
        private UsersType type;
    }

    @Getter
    @SuperBuilder
    public static class EditRequest extends BaseUserDto {
        @NotNull
        private Long id;

        private String name;
        private String password;
        private UsersType type;
    }

    @Getter
    @NoArgsConstructor
    public static class RemoveRequest extends BaseUserDto {
        @NotNull
        private Long id;
    }

}
