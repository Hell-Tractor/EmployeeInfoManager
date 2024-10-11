package com.employeeinfomanager.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReturnObject {
    @JsonIgnore
    @Getter
    private final ReturnNo code;

    @JsonProperty(value = "errMsg")
    private final String errMsg;

    @JsonProperty(value = "data")
    private final Object data;

    @JsonProperty(value = "errNo")
    public int getErrNo() {
        return this.code.getCode();
    }

    public ReturnObject(ReturnNo code, String errMsg, Object data) {
        this.code = code;
        this.errMsg = errMsg;
        this.data = data;
    }

    public ReturnObject() {
        this(ReturnNo.OK);
    }

    public ReturnObject(ReturnNo code) {
        this(code, code.getMessage());
    }

    public ReturnObject(ReturnNo code, String errMsg) {
        this(code, errMsg, null);
    }

    public ReturnObject(ReturnNo code, Object data) {
        this(code, code.getMessage(), data);
    }
}
