package com.backend.Backend.dtos;

import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
public class ReceivedNotificacion {
    private Long id;
    private String mensaje;
    private Long fromUser;
    private Instant date;
    private Date seenDate;
}
