package EventSystem;

public class EventSubscriber<TSubscriber, TParamType> {
    public EventSubscriber(TSubscriber subscriber, EventCallable<TParamType> action) {
        this.subscriber = subscriber;
        this.action = action;
    }

    public TSubscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(TSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    private TSubscriber subscriber;

    public EventCallable<TParamType> action;
}
