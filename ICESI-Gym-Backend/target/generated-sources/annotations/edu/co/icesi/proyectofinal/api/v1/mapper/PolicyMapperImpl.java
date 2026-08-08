package edu.co.icesi.proyectofinal.api.v1.mapper;

import edu.co.icesi.proyectofinal.api.v1.dto.PolicyRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.PolicyResponse;
import edu.co.icesi.proyectofinal.entity.Policy;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-07T19:02:46-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class PolicyMapperImpl implements PolicyMapper {

    @Override
    public Policy toEntity(PolicyRequest request) {
        if ( request == null ) {
            return null;
        }

        Policy policy = new Policy();

        policy.setAction( request.getAction() );
        policy.setDescription( request.getDescription() );
        policy.setName( request.getName() );
        policy.setResource( request.getResource() );

        return policy;
    }

    @Override
    public PolicyResponse toResponse(Policy policy) {
        if ( policy == null ) {
            return null;
        }

        PolicyResponse policyResponse = new PolicyResponse();

        policyResponse.setAction( policy.getAction() );
        policyResponse.setDescription( policy.getDescription() );
        policyResponse.setId( policy.getId() );
        policyResponse.setName( policy.getName() );
        policyResponse.setResource( policy.getResource() );

        return policyResponse;
    }
}
