package edu.co.icesi.proyectofinal.api.v1.mapper;

import edu.co.icesi.proyectofinal.api.v1.dto.RolePolicyRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.RolePolicyResponse;
import edu.co.icesi.proyectofinal.entity.Policy;
import edu.co.icesi.proyectofinal.entity.Role;
import edu.co.icesi.proyectofinal.entity.RolePolicy;
import edu.co.icesi.proyectofinal.entity.keys.RolePoliciesId;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-07T19:02:47-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class RolePolicyMapperImpl implements RolePolicyMapper {

    @Override
    public RolePolicy toEntity(RolePolicyRequest request) {
        if ( request == null ) {
            return null;
        }

        RolePolicy rolePolicy = new RolePolicy();

        rolePolicy.setId( rolePolicyRequestToRolePoliciesId( request ) );
        rolePolicy.setRole( rolePolicyRequestToRole( request ) );
        rolePolicy.setPolicy( rolePolicyRequestToPolicy( request ) );

        return rolePolicy;
    }

    @Override
    public RolePolicyResponse toResponse(RolePolicy entity) {
        if ( entity == null ) {
            return null;
        }

        RolePolicyResponse rolePolicyResponse = new RolePolicyResponse();

        rolePolicyResponse.setRoleId( entityIdRoleId( entity ) );
        rolePolicyResponse.setPolicyId( entityIdPolicyId( entity ) );

        return rolePolicyResponse;
    }

    protected RolePoliciesId rolePolicyRequestToRolePoliciesId(RolePolicyRequest rolePolicyRequest) {
        if ( rolePolicyRequest == null ) {
            return null;
        }

        RolePoliciesId rolePoliciesId = new RolePoliciesId();

        rolePoliciesId.setRoleId( rolePolicyRequest.getRoleId() );
        rolePoliciesId.setPolicyId( rolePolicyRequest.getPolicyId() );

        return rolePoliciesId;
    }

    protected Role rolePolicyRequestToRole(RolePolicyRequest rolePolicyRequest) {
        if ( rolePolicyRequest == null ) {
            return null;
        }

        Role role = new Role();

        role.setId( rolePolicyRequest.getRoleId() );

        return role;
    }

    protected Policy rolePolicyRequestToPolicy(RolePolicyRequest rolePolicyRequest) {
        if ( rolePolicyRequest == null ) {
            return null;
        }

        Policy policy = new Policy();

        policy.setId( rolePolicyRequest.getPolicyId() );

        return policy;
    }

    private Long entityIdRoleId(RolePolicy rolePolicy) {
        RolePoliciesId id = rolePolicy.getId();
        if ( id == null ) {
            return null;
        }
        return id.getRoleId();
    }

    private Long entityIdPolicyId(RolePolicy rolePolicy) {
        RolePoliciesId id = rolePolicy.getId();
        if ( id == null ) {
            return null;
        }
        return id.getPolicyId();
    }
}
