package edu.co.icesi.proyectofinal.api.v1.mapper;

import edu.co.icesi.proyectofinal.api.v1.dto.RegisterRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.RegisterResponse;
import edu.co.icesi.proyectofinal.api.v1.dto.UserRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.UserResponse;
import edu.co.icesi.proyectofinal.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-07T19:02:47-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponse toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.age( user.getAge() );
        userResponse.firstName( user.getFirstName() );
        userResponse.idUser( user.getIdUser() );
        userResponse.institutionalEmail( user.getInstitutionalEmail() );
        userResponse.lastName( user.getLastName() );

        return userResponse.build();
    }

    @Override
    public User toEntity(UserRequest userRequest) {
        if ( userRequest == null ) {
            return null;
        }

        User user = new User();

        user.setAge( userRequest.getAge() );
        user.setFirstName( userRequest.getFirstName() );
        user.setInstitutionalEmail( userRequest.getInstitutionalEmail() );
        user.setLastName( userRequest.getLastName() );
        user.setPassword( userRequest.getPassword() );

        return user;
    }

    @Override
    public User registerRequestToUser(RegisterRequest registerRequest) {
        if ( registerRequest == null ) {
            return null;
        }

        User user = new User();

        user.setAge( registerRequest.getAge() );
        user.setFirstName( registerRequest.getFirstName() );
        user.setInstitutionalEmail( registerRequest.getInstitutionalEmail() );
        user.setLastName( registerRequest.getLastName() );
        user.setPassword( registerRequest.getPassword() );

        return user;
    }

    @Override
    public RegisterResponse toRegisterResponse(User user) {
        if ( user == null ) {
            return null;
        }

        RegisterResponse.RegisterResponseBuilder registerResponse = RegisterResponse.builder();

        registerResponse.firstName( user.getFirstName() );
        registerResponse.idUser( user.getIdUser() );
        registerResponse.institutionalEmail( user.getInstitutionalEmail() );
        registerResponse.lastName( user.getLastName() );

        registerResponse.message( "User registered successfully" );

        return registerResponse.build();
    }
}
