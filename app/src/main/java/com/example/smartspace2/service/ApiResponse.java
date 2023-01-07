package com.example.smartspace2.service;

import retrofit2.Call;

public class ApiResponse {
    private boolean ok;
    private Call<?> call;
    private Throwable throwable;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Call<?> getCall() {
        return call;
    }

    public void setCall(Call<?> call) {
        this.call = call;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
