package com.codesync.codesync.Dto;

import lombok.Data;

@Data
public class CursorMessage {
    private String clientId; // A unique ID for the user sending the update
    private int lineNumber;
    private int column;
}
