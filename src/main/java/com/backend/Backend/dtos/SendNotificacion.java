package com.backend.Backend.dtos;

import lombok.Data;

@Data
public class SendNotificacion {
    private Long fromUser;
    private Long toUser;
    private String message;
}
