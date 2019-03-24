package EventSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Allows classes to define events and have their parent classes
 * execute code when that specific event occurs.
 *
 * @author Nick Gordon
 * @since 4/10/2018
 */
public class EventBus
{
    private static Map<String, ArrayList<EventSubscriber>> events = new HashMap<>();

   public static <TSubscriber, TParamType> void subscribe(TSubscriber subscriber, String key, EventCallable<TParamType> action) {
       if (!events.containsKey(key)) {
           events.put(key, new ArrayList<>());
       }

       ArrayList<EventSubscriber> eventSubscribers = events.get(key);
       eventSubscribers.add(new EventSubscriber(subscriber, action));
   }

    public static <TParam> void broadcast(String key, TParam param) {
       if (events.containsKey(key)) {
           ArrayList<EventSubscriber> eventSubscribers = events.get(key);

           for (EventSubscriber subscriber : eventSubscribers)
           {
               subscriber.action.setData(param);
               subscriber.action.call();
           }
       }
    }
}
