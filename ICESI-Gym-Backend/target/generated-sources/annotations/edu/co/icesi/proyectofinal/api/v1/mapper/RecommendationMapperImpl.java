package edu.co.icesi.proyectofinal.api.v1.mapper;

import edu.co.icesi.proyectofinal.api.v1.dto.RecommendationRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.RecommendationResponse;
import edu.co.icesi.proyectofinal.entity.Recommendation;
import edu.co.icesi.proyectofinal.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-07T19:02:47-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class RecommendationMapperImpl implements RecommendationMapper {

    @Override
    public Recommendation toEntity(RecommendationRequest request) {
        if ( request == null ) {
            return null;
        }

        Recommendation recommendation = new Recommendation();

        recommendation.setTrainer( recommendationRequestToUser( request ) );
        recommendation.setUser( recommendationRequestToUser1( request ) );
        recommendation.setDateCreated( request.getDateCreated() );
        recommendation.setDescription( request.getDescription() );

        return recommendation;
    }

    @Override
    public RecommendationResponse toResponse(Recommendation recommendation) {
        if ( recommendation == null ) {
            return null;
        }

        RecommendationResponse recommendationResponse = new RecommendationResponse();

        recommendationResponse.setTrainerId( recommendationTrainerIdUser( recommendation ) );
        recommendationResponse.setTrainerFirstName( recommendationTrainerFirstName( recommendation ) );
        recommendationResponse.setTrainerLastName( recommendationTrainerLastName( recommendation ) );
        recommendationResponse.setUserId( recommendationUserIdUser( recommendation ) );
        recommendationResponse.setUserFirstName( recommendationUserFirstName( recommendation ) );
        recommendationResponse.setUserLastName( recommendationUserLastName( recommendation ) );
        recommendationResponse.setDateCreated( recommendation.getDateCreated() );
        recommendationResponse.setDescription( recommendation.getDescription() );
        recommendationResponse.setIdRecommendation( recommendation.getIdRecommendation() );

        return recommendationResponse;
    }

    protected User recommendationRequestToUser(RecommendationRequest recommendationRequest) {
        if ( recommendationRequest == null ) {
            return null;
        }

        User user = new User();

        user.setIdUser( recommendationRequest.getTrainerId() );

        return user;
    }

    protected User recommendationRequestToUser1(RecommendationRequest recommendationRequest) {
        if ( recommendationRequest == null ) {
            return null;
        }

        User user = new User();

        user.setIdUser( recommendationRequest.getUserId() );

        return user;
    }

    private Integer recommendationTrainerIdUser(Recommendation recommendation) {
        User trainer = recommendation.getTrainer();
        if ( trainer == null ) {
            return null;
        }
        return trainer.getIdUser();
    }

    private String recommendationTrainerFirstName(Recommendation recommendation) {
        User trainer = recommendation.getTrainer();
        if ( trainer == null ) {
            return null;
        }
        return trainer.getFirstName();
    }

    private String recommendationTrainerLastName(Recommendation recommendation) {
        User trainer = recommendation.getTrainer();
        if ( trainer == null ) {
            return null;
        }
        return trainer.getLastName();
    }

    private Integer recommendationUserIdUser(Recommendation recommendation) {
        User user = recommendation.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getIdUser();
    }

    private String recommendationUserFirstName(Recommendation recommendation) {
        User user = recommendation.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getFirstName();
    }

    private String recommendationUserLastName(Recommendation recommendation) {
        User user = recommendation.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getLastName();
    }
}
