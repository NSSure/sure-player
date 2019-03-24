package EventSystem;

import java.util.concurrent.Callable;

public abstract class EventCallable<T> implements Callable<Void> {
    private T data;

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return this.data;
    }

    public abstract Void call();
}
