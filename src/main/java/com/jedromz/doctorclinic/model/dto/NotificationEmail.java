package com.jedromz.doctorclinic.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEmail {
    private String sender;
    private String subject;
    private String recipient;
    private String body;

}
