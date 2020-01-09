package com.raresopariuc.licenta.payload;

public class ApiResponse {
    private Boolean success;
    private String message;
    private String objectId;

    public ApiResponse(Boolean success, String message, String objectId) {
        this.success = success;
        this.message = message;
        this.objectId = objectId;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}