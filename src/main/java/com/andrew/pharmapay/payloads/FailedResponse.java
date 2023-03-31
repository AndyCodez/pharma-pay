package com.andrew.pharmapay.payloads;

import java.util.List;

public class FailedResponse {
    private List<String> errorMessages;

    public FailedResponse(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }
}
