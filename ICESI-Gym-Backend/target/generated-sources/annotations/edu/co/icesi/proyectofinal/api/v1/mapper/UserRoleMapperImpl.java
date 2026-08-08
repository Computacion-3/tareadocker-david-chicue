package edu.co.icesi.proyectofinal.api.v1.mapper;

import edu.co.icesi.proyectofinal.api.v1.dto.UserRoleRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.UserRoleResponse;
import edu.co.icesi.proyectofinal.entity.Role;
import edu.co.icesi.proyectofinal.entity.User;
import edu.co.icesi.proyectofinal.entity.UserRole;
import edu.co.icesi.proyectofinal.entity.keys.UserRoleId;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-07T19:02:46-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class UserRoleMapperImpl implements UserRoleMapper {

    @Override
    public UserRole toEntity(UserRoleRequest request) {
        if ( request == null ) {
            return null;
        }

        UserRole userRole = new UserRole();

        userRole.setId( userRoleRequestToUserRoleId( request ) );
        userRole.setUser( userRoleRequestToUser( request ) );
        userRole.setRole( userRoleRequestToRole( request ) );

        return userRole;
    }

    @Override
    public UserRoleResponse toResponse(UserRole entity) {
        if ( entity == null ) {
            return null;
        }

        UserRoleResponse userRoleResponse = new UserRoleResponse();

        userRoleResponse.setUserId( entityIdUserId( entity ) );
        userRoleResponse.setRoleId( entityIdRoleId( entity ) );

        return userRoleResponse;
    }

    protected UserRoleId userRoleRequestToUserRoleId(UserRoleRequest userRoleRequest) {
        if ( userRoleRequest == null ) {
            return null;
        }

        UserRoleId userRoleId = new UserRoleId();

        userRoleId.setUserId( userRoleRequest.getUserId() );
        userRoleId.setRoleId( userRoleRequest.getRoleId() );

        return userRoleId;
    }

    protected User userRoleRequestToUser(UserRoleRequest userRoleRequest) {
        if ( userRoleRequest == null ) {
            return null;
        }

        User user = new User();

        user.setIdUser( userRoleRequest.getUserId() );

        return user;
    }

    protected Role userRoleRequestToRole(UserRoleRequest userRoleRequest) {
        if ( userRoleRequest == null ) {
            return null;
        }

        Role role = new Role();

        role.setId( userRoleRequest.getRoleId() );

        return role;
    }

    private Integer entityIdUserId(UserRole userRole) {
        UserRoleId id = userRole.getId();
        if ( id == null ) {
            return null;
        }
        return id.getUserId();
    }

    private Long entityIdRoleId(UserRole userRole) {
        UserRoleId id = userRole.getId();
        if ( id == null ) {
            return null;
        }
        return id.getRoleId();
    }
}
