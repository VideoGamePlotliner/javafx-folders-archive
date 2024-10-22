package vgp.fx.listenerhandler;

import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ArrayChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.collections.WeakMapChangeListener;
import javafx.collections.WeakSetChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.event.WeakEventHandler;
import vgp.fx.application.ApplicationBase;

/**
 * A means to ensure that all listeners, handlers, and filters have been
 * detached by the end of the {@link ApplicationBase#stop()} method.
 * 
 * @author (to be added)
 * @version 6.18
 * @since 6.7
 */
public final class ListenerAndHandlerBindings {

    private interface CanCheckIfAnyFieldIsNull {
        boolean isAnyFieldOfThisObjectNull();
    }

    private interface CanCheckIfWeak {
        boolean isThisObjectWeak();
    }

    private interface ListenerBinding extends CanCheckIfAnyFieldIsNull, CanCheckIfWeak {
    }

    private static final class InvalidationListenerBinding implements ListenerBinding {
        private final Observable observable;
        private final InvalidationListener listener;

        private InvalidationListenerBinding(Observable observable, InvalidationListener listener) {
            super();
            this.observable = observable;
            this.listener = listener;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((observable == null) ? 0 : observable.hashCode());
            result = prime * result + ((listener == null) ? 0 : listener.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            InvalidationListenerBinding other = (InvalidationListenerBinding) obj;
            if (observable == null) {
                if (other.observable != null)
                    return false;
            } else if (!observable.equals(other.observable))
                return false;
            if (listener == null) {
                if (other.listener != null)
                    return false;
            } else if (!listener.equals(other.listener))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "InvalidationListenerBinding [observable=" + observable + ", listener=" + listener + "]";
        }

        @Override
        public boolean isAnyFieldOfThisObjectNull() {
            return observable == null || listener == null;
        }

        @Override
        public boolean isThisObjectWeak() {
            return listener instanceof WeakInvalidationListener;
        }

    }

    private static final class ChangeListenerBinding<T> implements ListenerBinding {
        private final ObservableValue<T> observableValue;
        private final ChangeListener<? super T> listener;

        private ChangeListenerBinding(ObservableValue<T> observableValue, ChangeListener<? super T> listener) {
            super();
            this.observableValue = observableValue;
            this.listener = listener;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((observableValue == null) ? 0 : observableValue.hashCode());
            result = prime * result + ((listener == null) ? 0 : listener.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ChangeListenerBinding<?> other = (ChangeListenerBinding<?>) obj;
            if (observableValue == null) {
                if (other.observableValue != null)
                    return false;
            } else if (!observableValue.equals(other.observableValue))
                return false;
            if (listener == null) {
                if (other.listener != null)
                    return false;
            } else if (!listener.equals(other.listener))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "ChangeListenerBinding [observableValue=" + observableValue + ", listener=" + listener + "]";
        }

        @Override
        public boolean isAnyFieldOfThisObjectNull() {
            return observableValue == null || listener == null;
        }

        @Override
        public boolean isThisObjectWeak() {
            return listener instanceof WeakChangeListener<?>;
        }

    }

    private static final class ListChangeListenerBinding<E> implements ListenerBinding {
        private final ObservableList<E> observableList;
        private final ListChangeListener<? super E> listener;

        private ListChangeListenerBinding(ObservableList<E> observableList, ListChangeListener<? super E> listener) {
            super();
            this.observableList = observableList;
            this.listener = listener;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((observableList == null) ? 0 : observableList.hashCode());
            result = prime * result + ((listener == null) ? 0 : listener.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ListChangeListenerBinding<?> other = (ListChangeListenerBinding<?>) obj;
            if (observableList == null) {
                if (other.observableList != null)
                    return false;
            } else if (!observableList.equals(other.observableList))
                return false;
            if (listener == null) {
                if (other.listener != null)
                    return false;
            } else if (!listener.equals(other.listener))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "ListChangeListenerBinding [observableList=" + observableList + ", listener=" + listener + "]";
        }

        @Override
        public boolean isAnyFieldOfThisObjectNull() {
            return observableList == null || listener == null;
        }

        @Override
        public boolean isThisObjectWeak() {
            return listener instanceof WeakListChangeListener<?>;
        }

    }

    private static final class SetChangeListenerBinding<E> implements ListenerBinding {
        private final ObservableSet<E> observableSet;
        private final SetChangeListener<? super E> listener;

        private SetChangeListenerBinding(ObservableSet<E> observableSet, SetChangeListener<? super E> listener) {
            super();
            this.observableSet = observableSet;
            this.listener = listener;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((observableSet == null) ? 0 : observableSet.hashCode());
            result = prime * result + ((listener == null) ? 0 : listener.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SetChangeListenerBinding<?> other = (SetChangeListenerBinding<?>) obj;
            if (observableSet == null) {
                if (other.observableSet != null)
                    return false;
            } else if (!observableSet.equals(other.observableSet))
                return false;
            if (listener == null) {
                if (other.listener != null)
                    return false;
            } else if (!listener.equals(other.listener))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "SetChangeListenerBinding [observableSet=" + observableSet + ", listener=" + listener + "]";
        }

        @Override
        public boolean isAnyFieldOfThisObjectNull() {
            return observableSet == null || listener == null;
        }

        @Override
        public boolean isThisObjectWeak() {
            return listener instanceof WeakSetChangeListener<?>;
        }

    }

    private static final class MapChangeListenerBinding<K, V> implements ListenerBinding {
        private final ObservableMap<K, V> observableMap;
        private final MapChangeListener<? super K, ? super V> listener;

        private MapChangeListenerBinding(ObservableMap<K, V> observableMap,
                MapChangeListener<? super K, ? super V> listener) {
            super();
            this.observableMap = observableMap;
            this.listener = listener;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((observableMap == null) ? 0 : observableMap.hashCode());
            result = prime * result + ((listener == null) ? 0 : listener.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            MapChangeListenerBinding<?, ?> other = (MapChangeListenerBinding<?, ?>) obj;
            if (observableMap == null) {
                if (other.observableMap != null)
                    return false;
            } else if (!observableMap.equals(other.observableMap))
                return false;
            if (listener == null) {
                if (other.listener != null)
                    return false;
            } else if (!listener.equals(other.listener))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "MapChangeListenerBinding [observableMap=" + observableMap + ", listener=" + listener + "]";
        }

        @Override
        public boolean isAnyFieldOfThisObjectNull() {
            return observableMap == null || listener == null;
        }

        @Override
        public boolean isThisObjectWeak() {
            return listener instanceof WeakMapChangeListener<?, ?>;
        }

    }

    private static final class ArrayChangeListenerBinding<T extends ObservableArray<T>>
            implements ListenerBinding {
        private final T observableArray;
        private final ArrayChangeListener<T> listener;

        private ArrayChangeListenerBinding(T observableArray, ArrayChangeListener<T> listener) {
            super();
            this.observableArray = observableArray;
            this.listener = listener;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((observableArray == null) ? 0 : observableArray.hashCode());
            result = prime * result + ((listener == null) ? 0 : listener.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ArrayChangeListenerBinding<?> other = (ArrayChangeListenerBinding<?>) obj;
            if (observableArray == null) {
                if (other.observableArray != null)
                    return false;
            } else if (!observableArray.equals(other.observableArray))
                return false;
            if (listener == null) {
                if (other.listener != null)
                    return false;
            } else if (!listener.equals(other.listener))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "ArrayChangeListenerBinding [observableArray=" + observableArray + ", listener=" + listener + "]";
        }

        @Override
        public boolean isAnyFieldOfThisObjectNull() {
            return observableArray == null || listener == null;
        }

        @Override
        public boolean isThisObjectWeak() {
            return false;
        }

    }

    // Used for both event handlers and event filters
    private static final class EventHandlerFilterBinding<E extends Event>
            implements CanCheckIfAnyFieldIsNull, CanCheckIfWeak {
        private final EventTarget eventTarget;
        private final EventType<E> eventType;
        private final EventHandler<? super E> eventHandler;
        private final boolean isFilter;

        private EventHandlerFilterBinding(EventTarget eventTarget, EventType<E> eventType,
                EventHandler<? super E> eventHandler, boolean isFilter) {
            super();
            this.eventTarget = eventTarget;
            this.eventType = eventType;
            this.eventHandler = eventHandler;
            this.isFilter = isFilter;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((eventTarget == null) ? 0 : eventTarget.hashCode());
            result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
            result = prime * result + ((eventHandler == null) ? 0 : eventHandler.hashCode());
            result = prime * result + (isFilter ? 1231 : 1237);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            EventHandlerFilterBinding<?> other = (EventHandlerFilterBinding<?>) obj;
            if (eventTarget == null) {
                if (other.eventTarget != null)
                    return false;
            } else if (!eventTarget.equals(other.eventTarget))
                return false;
            if (eventType == null) {
                if (other.eventType != null)
                    return false;
            } else if (!eventType.equals(other.eventType))
                return false;
            if (eventHandler == null) {
                if (other.eventHandler != null)
                    return false;
            } else if (!eventHandler.equals(other.eventHandler))
                return false;
            if (isFilter != other.isFilter)
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "EventHandlerFilterBinding [eventTarget=" + eventTarget + ", eventType=" + eventType
                    + ", eventHandler="
                    + eventHandler + ", isFilter=" + isFilter + "]";
        }

        @Override
        public boolean isAnyFieldOfThisObjectNull() {
            return eventTarget == null || eventType == null || eventHandler == null;
        }

        @Override
        public boolean isThisObjectWeak() {
            return eventHandler instanceof WeakEventHandler<?>;
        }

    }

    // Memory leaks happen when listeners and handlers aren't removed.
    private static final ArrayList<InvalidationListenerBinding> INVALIDATION_LISTENER_BINDINGS = new ArrayList<>();
    private static final ArrayList<ChangeListenerBinding<?>> CHANGE_LISTENER_BINDINGS = new ArrayList<>();
    private static final ArrayList<ListChangeListenerBinding<?>> LIST_CHANGE_LISTENER_BINDINGS = new ArrayList<>();
    private static final ArrayList<SetChangeListenerBinding<?>> SET_CHANGE_LISTENER_BINDINGS = new ArrayList<>();
    private static final ArrayList<MapChangeListenerBinding<?, ?>> MAP_CHANGE_LISTENER_BINDINGS = new ArrayList<>();
    private static final ArrayList<ArrayChangeListenerBinding<?>> ARRAY_CHANGE_LISTENER_BINDINGS = new ArrayList<>();
    private static final ArrayList<EventHandlerFilterBinding<?>> EVENT_HANDLER_BINDINGS = new ArrayList<>();
    private static final ArrayList<EventHandlerFilterBinding<?>> EVENT_FILTER_BINDINGS = new ArrayList<>();

    private static final ArrayList<ObjectProperty<EventHandler<ActionEvent>>> ACTION_EVENT_HANDLER_PROPERTIES = new ArrayList<>();

    private ListenerAndHandlerBindings() throws Exception {
        throw new Exception("Do not use this constructor");
    }

    public static void outputListenerBindings() {

        outputNumbersOfBindings();

        INVALIDATION_LISTENER_BINDINGS.forEach(listenerBindingChecker());
        System.out.println();
        CHANGE_LISTENER_BINDINGS.forEach(listenerBindingChecker());
        System.out.println();
        LIST_CHANGE_LISTENER_BINDINGS.forEach(listenerBindingChecker());
        System.out.println();
        SET_CHANGE_LISTENER_BINDINGS.forEach(listenerBindingChecker());
        System.out.println();
        MAP_CHANGE_LISTENER_BINDINGS.forEach(listenerBindingChecker());
        System.out.println();
        ARRAY_CHANGE_LISTENER_BINDINGS.forEach(listenerBindingChecker());
        System.out.println();
        EVENT_HANDLER_BINDINGS.forEach(eventHandlerFilterBindingChecker(false));
        System.out.println();
        EVENT_FILTER_BINDINGS.forEach(eventHandlerFilterBindingChecker(true));
        System.out.println();
        ACTION_EVENT_HANDLER_PROPERTIES.forEach(eventHandlerPropertyChecker());
        System.out.println();
    }

    private static <E extends Event> Consumer<ObjectProperty<EventHandler<E>>> eventHandlerPropertyChecker() {
        return eventHandlerProperty -> {
            System.out.println(eventHandlerProperty);
            if (eventHandlerProperty == null) {
                System.out.println("Warning: null property");
            } else {
                final Object bean = eventHandlerProperty.getBean();
                final String name = eventHandlerProperty.getName();
                final EventHandler<E> value = eventHandlerProperty.get();
                if (bean == null) {
                    System.out.println("Warning: property bean is null");
                } else if (!(bean instanceof EventTarget)) {
                    System.out.println("Warning: property bean is not a target");
                } else if (name == null) {
                    System.out.println("Warning: property name is null");
                } else if (!name.equals("onAction")) {
                    System.out.println("Warning: property name is incorrect");
                } else if (value == null) {
                    System.out.println("Warning: property value is null");
                } else if (!(value instanceof WeakEventHandler<?>)) {
                    System.out.println("Wait. Why has " + eventHandlerProperty
                            + " not been removed yet? Its value is not a weak handler.");
                }
            }
        };
    }

    private static <T extends ListenerBinding> Consumer<T> listenerBindingChecker() {
        return binding -> {
            System.out.println(binding);
            if (binding == null) {
                System.out.println("Warning: null binding");
            } else if (binding.isAnyFieldOfThisObjectNull()) {
                System.out.println("Warning: one or more null fields in binding");
            } else if (!binding.isThisObjectWeak()) {
                System.out.println("Wait. Why has " + binding + " not been removed yet? It's not a weak listener.");
            }
        };
    }

    private static <E extends Event> Consumer<EventHandlerFilterBinding<E>> eventHandlerFilterBindingChecker(
            final boolean correctValueFor_isFilter) {
        return binding -> {
            System.out.println(binding);
            if (binding == null) {
                System.out.println("Warning: null binding");
            } else if (binding.isAnyFieldOfThisObjectNull()) {
                System.out.println("Warning: one or more null fields in binding");
            } else if (binding.isFilter ^ correctValueFor_isFilter) {
                System.out.println("Warning: incorrect isFilter value for binding");
            } else if (!binding.isThisObjectWeak()) {
                System.out.println("Wait. Why has " + binding + " not been removed yet? It's not a weak "
                        + (binding.isFilter ? "filter" : "handler") + ".");
            }
        };
    }

    private static void outputNumbersOfBindings() {

        final int numListenerBindings = INVALIDATION_LISTENER_BINDINGS.size() + CHANGE_LISTENER_BINDINGS.size()
                + LIST_CHANGE_LISTENER_BINDINGS.size() + SET_CHANGE_LISTENER_BINDINGS.size()
                + MAP_CHANGE_LISTENER_BINDINGS.size() + ARRAY_CHANGE_LISTENER_BINDINGS.size();

        final int numHandlerBindings = EVENT_HANDLER_BINDINGS.size();
        final int numFilterBindings = EVENT_FILTER_BINDINGS.size();
        final int numOnActionBindings = ACTION_EVENT_HANDLER_PROPERTIES.size();

        System.out.println("There are currently "
                + numListenerBindings + " listener bindings, "
                + numHandlerBindings + " handler bindings, and "
                + numFilterBindings + " filter bindings, and "
                + numOnActionBindings + " 'onAction' bindings.");
    }

    /**
     * Use this method to fix the fact that, since {@code addListener} is often
     * overloaded, {@code addListener(this)} can become ambiguous.
     * 
     * @param observable the object to attach the given listener to
     * @param listener   a listener to call when the given object becomes invalid
     * 
     * @see Observable#addListener(InvalidationListener)
     */
    public static void addInvalidationListener(Observable observable, InvalidationListener listener) {
        if (observable == null) {
            return;
        }
        if (listener == null) {
            return;
        }
        observable.addListener(listener);
        INVALIDATION_LISTENER_BINDINGS.add(new InvalidationListenerBinding(observable, listener));
    }

    /**
     * Use this method to fix the fact that, since {@code removeListener} is often
     * overloaded, {@code removeListener(this)} can become ambiguous.
     * 
     * @param observable the object to detach the given listener from
     * @param listener   a listener called when the given object became invalid
     * 
     * @see Observable#removeListener(InvalidationListener)
     */
    public static void removeInvalidationListener(Observable observable, InvalidationListener listener) {
        if (observable == null) {
            return;
        }
        if (listener == null) {
            return;
        }
        observable.removeListener(listener);
        INVALIDATION_LISTENER_BINDINGS.remove(new InvalidationListenerBinding(observable, listener));
    }

    /**
     * Use this method to fix the fact that, since {@code addListener} is often
     * overloaded, {@code addListener(this)} can become ambiguous.
     * 
     * @param <T>             the type of the observable object's
     *                        {@link ObservableValue#getValue() value}
     * 
     * @param observableValue the object to attach the given listener to
     * @param listener        a listener to call when the given object's value
     *                        changes
     * 
     * @see ObservableValue#addListener(ChangeListener)
     */
    public static <T> void addChangeListener(ObservableValue<T> observableValue, ChangeListener<? super T> listener) {
        if (observableValue == null) {
            return;
        }
        if (listener == null) {
            return;
        }
        observableValue.addListener(listener);
        CHANGE_LISTENER_BINDINGS.add(new ChangeListenerBinding<T>(observableValue, listener));
    }

    /**
     * Use this method to fix the fact that, since {@code removeListener} is often
     * overloaded, {@code removeListener(this)} can become ambiguous.
     * 
     * @param <T>             the type of the observable object's
     *                        {@link ObservableValue#getValue() value}
     * 
     * @param observableValue the object to detach the given listener from
     * @param listener        a listener called when the given object's value
     *                        changed
     * 
     * @see ObservableValue#removeListener(ChangeListener)
     */
    public static <T> void removeChangeListener(ObservableValue<T> observableValue,
            ChangeListener<? super T> listener) {
        if (observableValue == null) {
            return;
        }
        if (listener == null) {
            return;
        }
        observableValue.removeListener(listener);
        CHANGE_LISTENER_BINDINGS.remove(new ChangeListenerBinding<T>(observableValue, listener));
    }

    /**
     * Use this method to fix the fact that, since {@code addListener} is often
     * overloaded, {@code addListener(this)} can become ambiguous.
     * 
     * @param <E>            the type of the given list's elements
     * 
     * @param observableList the list to attach the given listener to
     * @param listener       a listener to call when the given list's contents
     *                       change
     * 
     * @see ObservableList#addListener(ListChangeListener)
     */
    public static <E> void addListChangeListener(ObservableList<E> observableList,
            ListChangeListener<? super E> listener) {
        if (observableList == null) {
            return;
        }
        if (listener == null) {
            return;
        }
        observableList.addListener(listener);
        LIST_CHANGE_LISTENER_BINDINGS.add(new ListChangeListenerBinding<E>(observableList, listener));
    }

    /**
     * Use this method to fix the fact that, since {@code removeListener} is often
     * overloaded, {@code removeListener(this)} can become ambiguous.
     * 
     * @param <E>            the type of the given list's elements
     * 
     * @param observableList the list to detach the given listener from
     * @param listener       a listener called when the given list's contents
     *                       changed
     * 
     * @see ObservableList#removeListener(ListChangeListener)
     */
    public static <E> void removeListChangeListener(ObservableList<E> observableList,
            ListChangeListener<? super E> listener) {
        if (observableList == null) {
            return;
        }
        if (listener == null) {
            return;
        }
        observableList.removeListener(listener);
        LIST_CHANGE_LISTENER_BINDINGS.remove(new ListChangeListenerBinding<E>(observableList, listener));
    }

    /**
     * Use this method to fix the fact that, since {@code addListener} is often
     * overloaded, {@code addListener(this)} can become ambiguous.
     * 
     * @param <E>           the type of the given set's elements
     * 
     * @param observableSet the set to attach the given listener to
     * @param listener      a listener to call when the given set's contents change
     * 
     * @see ObservableSet#addListener(SetChangeListener)
     */
    public static <E> void addSetChangeListener(ObservableSet<E> observableSet, SetChangeListener<? super E> listener) {
        if (observableSet == null) {
            return;
        }
        if (listener == null) {
            return;
        }
        observableSet.addListener(listener);
        SET_CHANGE_LISTENER_BINDINGS.add(new SetChangeListenerBinding<E>(observableSet, listener));
    }

    /**
     * Use this method to fix the fact that, since {@code removeListener} is often
     * overloaded, {@code removeListener(this)} can become ambiguous.
     * 
     * @param <E>           the type of the given set's elements
     * 
     * @param observableSet the set to detach the given listener from
     * @param listener      a listener called when the given set's contents changed
     * 
     * @see ObservableSet#removeListener(SetChangeListener)
     */
    public static <E> void removeSetChangeListener(ObservableSet<E> observableSet,
            SetChangeListener<? super E> listener) {
        if (observableSet == null) {
            return;
        }
        if (listener == null) {
            return;
        }
        observableSet.removeListener(listener);
        SET_CHANGE_LISTENER_BINDINGS.remove(new SetChangeListenerBinding<E>(observableSet, listener));
    }

    /**
     * Use this method to fix the fact that, since {@code addListener} is often
     * overloaded, {@code addListener(this)} can become ambiguous.
     * 
     * @param <K>           the type of the given map's keys
     * @param <V>           the type of the given map's values
     * 
     * @param observableMap the map to attach the given listener to
     * @param listener      a listener to call when the given map's contents change
     * 
     * @see ObservableMap#addListener(MapChangeListener)
     */
    public static <K, V> void addMapChangeListener(ObservableMap<K, V> observableMap,
            MapChangeListener<? super K, ? super V> listener) {
        if (observableMap == null) {
            return;
        }
        if (listener == null) {
            return;
        }
        observableMap.addListener(listener);
        MAP_CHANGE_LISTENER_BINDINGS.add(new MapChangeListenerBinding<K, V>(observableMap, listener));
    }

    /**
     * Use this method to fix the fact that, since {@code removeListener} is often
     * overloaded, {@code removeListener(this)} can become ambiguous.
     * 
     * @param <K>           the type of the given map's keys
     * @param <V>           the type of the given map's values
     * 
     * @param observableMap the map to detach the given listener from
     * @param listener      a listener called when the given map's contents changed
     * 
     * @see ObservableMap#removeListener(MapChangeListener)
     */
    public static <K, V> void removeMapChangeListener(ObservableMap<K, V> observableMap,
            MapChangeListener<? super K, ? super V> listener) {
        if (observableMap == null) {
            return;
        }
        if (listener == null) {
            return;
        }
        observableMap.removeListener(listener);
        MAP_CHANGE_LISTENER_BINDINGS.remove(new MapChangeListenerBinding<K, V>(observableMap, listener));
    }

    /**
     * Use this method to fix the fact that, since {@code addListener} is often
     * overloaded, {@code addListener(this)} can become ambiguous.
     * 
     * @param <T>             the type of the given observable array
     * 
     * @param observableArray the object to attach the given listener to
     * @param listener        a listener to call when the given object's contents
     *                        change
     * 
     * @see ObservableArray#addListener(ArrayChangeListener)
     */
    public static <T extends ObservableArray<T>> void addArrayChangeListener(T observableArray,
            ArrayChangeListener<T> listener) {
        if (observableArray == null) {
            return;
        }
        if (listener == null) {
            return;
        }
        observableArray.addListener(listener);
        ARRAY_CHANGE_LISTENER_BINDINGS.add(new ArrayChangeListenerBinding<T>(observableArray, listener));
    }

    /**
     * Use this method to fix the fact that, since {@code removeListener} is often
     * overloaded, {@code removeListener(this)} can become ambiguous.
     * 
     * @param <T>             the type of the given observable array
     * 
     * @param observableArray the object to detach the given listener from
     * @param listener        a listener called when the given object's contents
     *                        changed
     * 
     * @see ObservableArray#removeListener(ArrayChangeListener)
     */
    public static <T extends ObservableArray<T>> void removeArrayChangeListener(T observableArray,
            ArrayChangeListener<T> listener) {
        if (observableArray == null) {
            return;
        }
        if (listener == null) {
            return;
        }
        observableArray.removeListener(listener);
        ARRAY_CHANGE_LISTENER_BINDINGS.remove(new ArrayChangeListenerBinding<T>(observableArray, listener));
    }

    /**
     * Use this method and
     * {@link #removeEventHandler(EventTarget, EventType, EventHandler)} to keep
     * track of which handlers have been removed; if not, memory leaks might happen.
     * 
     * @param <E>          the {@code Event} subclass handled
     * 
     * @param eventTarget  the object to attach the given handler to
     * @param eventType    the type of event that the given target should send to
     *                     the given handler
     * @param eventHandler a handler called when the given target receives an event
     *                     of the given type
     * 
     * @see EventTarget#addEventHandler(EventType, EventHandler)
     */
    public static <E extends Event> void addEventHandler(EventTarget eventTarget, EventType<E> eventType,
            EventHandler<? super E> eventHandler) {
        if (eventTarget == null) {
            return;
        }
        if (eventType == null) {
            return;
        }
        if (eventHandler == null) {
            return;
        }
        eventTarget.addEventHandler(eventType, eventHandler);
        EVENT_HANDLER_BINDINGS.add(new EventHandlerFilterBinding<E>(eventTarget, eventType, eventHandler, false));
    }

    /**
     * Use this method and
     * {@link #addEventHandler(EventTarget, EventType, EventHandler)} to keep track
     * of which handlers have been removed; if not, memory leaks might happen.
     * 
     * @param <E>          the {@code Event} subclass handled
     * 
     * @param eventTarget  the object to attach the given handler to
     * @param eventType    the type of event that the given target should send to
     *                     the given handler
     * @param eventHandler a handler called when the given target receives an event
     *                     of the given type
     * 
     * @see EventTarget#removeEventHandler(EventType, EventHandler)
     */
    public static <E extends Event> void removeEventHandler(EventTarget eventTarget, EventType<E> eventType,
            EventHandler<? super E> eventHandler) {
        if (eventTarget == null) {
            return;
        }
        if (eventType == null) {
            return;
        }
        if (eventHandler == null) {
            return;
        }
        eventTarget.removeEventHandler(eventType, eventHandler);
        EVENT_HANDLER_BINDINGS.remove(new EventHandlerFilterBinding<E>(eventTarget, eventType, eventHandler, false));
    }

    /**
     * Use this method and
     * {@link #removeEventFilter(EventTarget, EventType, EventHandler)} to keep
     * track of which filters have been removed; if not, memory leaks might happen.
     * 
     * @param <E>         the {@code Event} subclass handled
     * 
     * @param eventTarget the object to attach the given filter to
     * @param eventType   the type of event that the given target should send to
     *                    the given filter
     * @param eventFilter a filter called when the given target receives an event
     *                    of the given type
     * 
     * @see EventTarget#addEventFilter(EventType, EventHandler)
     */
    public static <E extends Event> void addEventFilter(EventTarget eventTarget, EventType<E> eventType,
            EventHandler<? super E> eventFilter) {
        if (eventTarget == null) {
            return;
        }
        if (eventType == null) {
            return;
        }
        if (eventFilter == null) {
            return;
        }
        eventTarget.addEventFilter(eventType, eventFilter);
        EVENT_FILTER_BINDINGS.add(new EventHandlerFilterBinding<E>(eventTarget, eventType, eventFilter, true));
    }

    /**
     * Use this method and
     * {@link #addEventFilter(EventTarget, EventType, EventHandler)} to keep track
     * of which filters have been removed; if not, memory leaks might happen.
     * 
     * @param <E>         the {@code Event} subclass handled
     * 
     * @param eventTarget the object to attach the given filter to
     * @param eventType   the type of event that the given target should send to
     *                    the given filter
     * @param eventFilter a filter called when the given target receives an event
     *                    of the given type
     * 
     * @see EventTarget#removeEventFilter(EventType, EventHandler)
     */
    public static <E extends Event> void removeEventFilter(EventTarget eventTarget, EventType<E> eventType,
            EventHandler<? super E> eventFilter) {
        if (eventTarget == null) {
            return;
        }
        if (eventType == null) {
            return;
        }
        if (eventFilter == null) {
            return;
        }
        eventTarget.removeEventFilter(eventType, eventFilter);
        EVENT_FILTER_BINDINGS.remove(new EventHandlerFilterBinding<E>(eventTarget, eventType, eventFilter, true));
    }

    /**
     * Use this method to keep track of which {@link ActionEvent} handlers have been
     * removed; if not, memory leaks might happen.
     * 
     * @param onActionProperty the non-{@code null} property whose bean should be a
     *                         non-{@code null} {@link EventTarget}, whose name
     *                         should be "onAction", and whose value must be kept
     *                         track of
     * @param eventHandler     the new value for the given property
     * 
     * @since 6.9
     */
    public static void setOnAction(final ObjectProperty<EventHandler<ActionEvent>> onActionProperty,
            final EventHandler<ActionEvent> eventHandler) {

        if (onActionProperty != null) {
            final Object bean = onActionProperty.getBean();
            if (bean != null) {
                if (bean instanceof EventTarget) {
                    final String name = onActionProperty.getName();
                    if ("onAction".equals(name)) {
                        if (eventHandler == null) {
                            onActionProperty.set(eventHandler);
                            ACTION_EVENT_HANDLER_PROPERTIES.remove(onActionProperty);
                        } else {
                            ACTION_EVENT_HANDLER_PROPERTIES.add(onActionProperty);
                            onActionProperty.set(eventHandler);
                        }
                    }
                }
            }
        }
    }
}
