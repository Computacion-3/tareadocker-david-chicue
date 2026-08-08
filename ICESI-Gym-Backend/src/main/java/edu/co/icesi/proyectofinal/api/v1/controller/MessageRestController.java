package edu.co.icesi.proyectofinal.api.v1.controller;

import edu.co.icesi.proyectofinal.api.v1.dto.MessageRequest;
import edu.co.icesi.proyectofinal.api.v1.dto.MessageResponse;
import edu.co.icesi.proyectofinal.api.v1.mapper.MessageMapper;
import edu.co.icesi.proyectofinal.entity.Message;
import edu.co.icesi.proyectofinal.services.MessageService;
import edu.co.icesi.proyectofinal.services.UserService;
import edu.co.icesi.proyectofinal.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/messages")
@Tag(name = "Messages", description = "Endpoints for managing user messages")
public class MessageRestController {

    private final MessageService messageService;
    private final MessageMapper messageMapper;
    private final UserService userService;

    public MessageRestController(MessageService messageService, MessageMapper messageMapper, UserService userService) {
        this.messageService = messageService;
        this.messageMapper = messageMapper;
        this.userService = userService;
    }

    @GetMapping
    @Operation(
            summary = "Get all messages",
            description = "Retrieves a list of all messages in the system (filtered for user)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of messages retrieved successfully")
            }
    )
    @PreAuthorize("hasAnyAuthority('LIST_MESSAGE', 'VIEW_OWN_PROFILE')")
    public ResponseEntity<List<MessageResponse>> getAll(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());

        return ResponseEntity.ok(messageService.getMessages().stream()
                .filter(m -> {
                    boolean isSender = m.getSender() != null && m.getSender().getIdUser().equals(user.getIdUser());
                    boolean isReceiver = m.getReceiver() != null && m.getReceiver().getIdUser().equals(user.getIdUser());
                    return isSender || isReceiver;
                })
                .map(messageMapper::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get message by ID",
            description = "Retrieves a single message's details by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Message found", 
                            content = @Content(schema = @Schema(implementation = MessageResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Message not found", content = @Content)
            }
    )
    @PreAuthorize("hasAnyAuthority('LIST_MESSAGE', 'VIEW_OWN_PROFILE')")
    public ResponseEntity<MessageResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(messageMapper.toResponse(messageService.getMessageById(id)));
    }

    @PostMapping
    @Operation(
            summary = "Send a message",
            description = "Creates a new message with the provided data",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Message created", 
                            content = @Content(schema = @Schema(implementation = MessageResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
            }
    )
    @PreAuthorize("hasAnyAuthority('CREATE_MESSAGE', 'EDIT_OWN_PROFILE')")
    public ResponseEntity<MessageResponse> create(@RequestBody MessageRequest request) {
        Message message = messageMapper.toEntity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageMapper.toResponse(messageService.addMessage(message)));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a message",
            description = "Updates an existing message's details by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Message updated successfully", 
                            content = @Content(schema = @Schema(implementation = MessageResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Message not found", content = @Content)
            }
    )
    @PreAuthorize("hasAuthority('EDIT_MESSAGE')")
    public ResponseEntity<MessageResponse> update(@PathVariable Integer id, @RequestBody MessageRequest request) {
        Message message = messageMapper.toEntity(request);
        message.setId(id);
        return ResponseEntity.ok(messageMapper.toResponse(messageService.updateMessage(message)));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a message",
            description = "Deletes a message from the system by its ID",
            responses = {
                    @ApiResponse(responseCode = "244", description = "Message deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Message not found", content = @Content)
            }
    )
    @PreAuthorize("hasAuthority('DELETE_MESSAGE')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
