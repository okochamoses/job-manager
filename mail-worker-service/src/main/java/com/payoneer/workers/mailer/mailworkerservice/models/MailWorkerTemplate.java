package com.payoneer.workers.mailer.mailworkerservice.models;

import lombok.Data;

@Data
public class MailWorkerTemplate{
	private String sender;
	private boolean isHtml;
	private String recipient;
	private String message;
}
