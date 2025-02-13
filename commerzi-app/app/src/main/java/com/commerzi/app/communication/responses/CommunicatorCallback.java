package com.commerzi.app.communication.responses;

public class CommunicatorCallback<A> {
    public interface OnSuccessListener<B> {
        void onSuccess(B response);
    }

    public interface OnFailureListener<C> {
        void onFailure(C response);
    }

    private final OnSuccessListener<A> onSuccessListener;
    private final OnFailureListener<A> onFailureListener;

    public CommunicatorCallback(OnSuccessListener<A> onSuccessListener, OnFailureListener<A> onFailureListener) {
        this.onSuccessListener = onSuccessListener;
        this.onFailureListener = onFailureListener;
    }
    public void onSuccess(A response) {
        onSuccessListener.onSuccess(response);
    }

    public void onFailure(A response) {
        onFailureListener.onFailure(response);
    }
}
