
package com.mycompany.fundtransfer_service.common.utils;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.ToString;

public final class JsonUtils {

    private JsonUtils() {}

    public static Gson build() {
        return new Gson().newBuilder().setPrettyPrinting().create();
    }

    public static String toJson(String errorMsg, int errorCode) {
        final ErrorInfo info = new ErrorInfo(errorMsg, errorCode);
        return build().toJson(info);
    }

    @Getter
    @ToString
    private static class ErrorInfo {

        private final int errorCode;
        private final String errorMessage;

        ErrorInfo(String errMessage, int errCode) {
            this.errorCode = errCode;
            this.errorMessage = errMessage;
        }
    }
}
