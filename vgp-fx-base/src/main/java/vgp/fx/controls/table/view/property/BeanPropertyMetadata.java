package vgp.fx.controls.table.view.property;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentSkipListMap;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import vgp.dispose.Disposable;

/**
 * @version 5.5
 */
public final class BeanPropertyMetadata implements Disposable {

    private boolean isDisposed = false;
    private final LinkedHashSet<Method> getterMethods = new LinkedHashSet<>();
    private final LinkedHashSet<Method> setterMethods = new LinkedHashSet<>();
    private final LinkedHashSet<Method> propertyMethods = new LinkedHashSet<>();
    private final String propertyName;

    private BeanPropertyMetadata(String propertyName) {
        super();
        this.propertyName = Objects.requireNonNull(propertyName);
    }

    @Override
    public void dispose() {
        if (isDisposed()) {
            return;
        }
        isDisposed = true;
        getterMethods.clear();
        setterMethods.clear();
        propertyMethods.clear();
    }

    @Override
    public boolean isDisposed() {
        return isDisposed;
    }

    private void addGetterMethod(Method method) {
        verifyNotDisposed();
        Objects.requireNonNull(method);
        getterMethods.add(method);
    }

    private void addSetterMethod(Method method) {
        verifyNotDisposed();
        Objects.requireNonNull(method);
        setterMethods.add(method);
    }

    private void addPropertyMethod(Method method) {
        verifyNotDisposed();
        Objects.requireNonNull(method);
        propertyMethods.add(method);
    }

    String getPropertyName() {
        verifyNotDisposed();
        return propertyName;
    }

    Class<?> getPropertyType() {
        verifyNotDisposed();
        {
            final Class<?> propertyType_getters = getPropertyTypeFromGetterMethods();
            if (propertyType_getters != null) {
                return propertyType_getters;
            }
        }
        {
            final Class<?> propertyType_setters = getPropertyTypeFromSetterMethods();
            if (propertyType_setters != null) {
                return propertyType_setters;
            }
        }
        return null;
    }

    private Class<?> getPropertyTypeFromGetterMethods() {
        verifyNotDisposed();
        if (getterMethods.isEmpty()) {
            return null;
        } else {
            final LinkedHashSet<Class<?>> returnTypes = new LinkedHashSet<>();
            for (Method getterMethod : getterMethods) {
                returnTypes.add(Objects.requireNonNull(getterMethod.getReturnType()));
            }
            if (returnTypes.size() == 1) {
                return Objects.requireNonNull(returnTypes.iterator().next());
            } else {
                return Objects.requireNonNull(findCommonSubclass(returnTypes));
            }
        }
    }

    private Class<?> getPropertyTypeFromSetterMethods() {
        verifyNotDisposed();
        if (setterMethods.isEmpty()) {
            return null;
        } else {
            final LinkedHashSet<Class<?>> parameterTypes = new LinkedHashSet<>();
            for (Method setterMethod : setterMethods) {
                parameterTypes.add(Objects.requireNonNull(setterMethod.getParameterTypes()[0]));
            }
            if (parameterTypes.size() == 1) {
                return Objects.requireNonNull(parameterTypes.iterator().next());
            } else {
                return Objects.requireNonNull(findCommonSubclass(parameterTypes));
            }
        }
    }

    // For the purposes of this method, assume that subinterfaces count as
    // subclasses and that superinterfaces count as superclasses.
    private static Class<?> findCommonSubclass(LinkedHashSet<Class<?>> classes) {
        for (Class<?> possibleSubclass : classes) {
            if (isCommonSubclass(possibleSubclass, classes)) {
                return possibleSubclass;
            }
        }
        throw new Error("Could not find common subclass");
    }

    // For the purposes of this method, assume that subinterfaces count as
    // subclasses and that superinterfaces count as superclasses.
    private static boolean isCommonSubclass(Class<?> possibleSubclass, LinkedHashSet<Class<?>> classes) {
        for (Class<?> possibleSuperclass : classes) {
            if (!possibleSuperclass.isAssignableFrom(possibleSubclass)) {
                return false;
            }
        }
        return true;
    }

    boolean isReadable() {
        verifyNotDisposed();
        if (!getterMethods.isEmpty()) {
            return true;
        } else {
            for (Method propertyMethod : propertyMethods) {
                Class<?> returnType = Objects.requireNonNull(propertyMethod.getReturnType());
                if (ReadOnlyProperty.class.isAssignableFrom(returnType)) {
                    return true;
                }
            }
            return false;
        }
    }

    boolean isWritable() {
        verifyNotDisposed();
        if (!setterMethods.isEmpty()) {
            return true;
        } else {
            for (Method propertyMethod : propertyMethods) {
                Class<?> returnType = Objects.requireNonNull(propertyMethod.getReturnType());
                if (Property.class.isAssignableFrom(returnType)) {
                    return true;
                }
            }
            return false;
        }
    }

    static void disposeMap(Map<String, BeanPropertyMetadata> backingMap) {
        if (backingMap == null) {
            return;
        }
        backingMap.values().forEach(BeanPropertyMetadata::dispose);
        backingMap.clear();
    }

    static ConcurrentSkipListMap<String, BeanPropertyMetadata> createBackingMap(final Class<?> beanClass) {
        Objects.requireNonNull(beanClass);

        final ConcurrentSkipListMap<String, BeanPropertyMetadata> backingMap = new ConcurrentSkipListMap<>();

        try {
            final Method[] beanClassMethods = Objects.requireNonNull(beanClass.getMethods());

            for (Method beanClassMethod : beanClassMethods) {
                if (beanClassMethod != null) {
                    final String beanClassMethodName = Objects.requireNonNull(beanClassMethod.getName());
                    final Class<?>[] beanClassMethodParameterTypes = Objects
                            .requireNonNull(beanClassMethod.getParameterTypes());
                    final Class<?> beanClassMethodReturnType = Objects.requireNonNull(beanClassMethod.getReturnType());

                    final boolean hasZeroParameters = (beanClassMethodParameterTypes.length == 0);
                    final boolean hasOneParameter = (beanClassMethodParameterTypes.length == 1);

                    final boolean hasBooleanReturnType = (beanClassMethodReturnType.equals(boolean.class)
                            || beanClassMethodReturnType.equals(Boolean.class));
                    final boolean hasVoidReturnType = (beanClassMethodReturnType.equals(void.class)
                            || beanClassMethodReturnType.equals(Void.class));

                    if (hasBooleanReturnType && hasVoidReturnType) {
                        throw new Error("hasBooleanReturnType && hasVoidReturnType");
                    }

                    final boolean isGetterMethod;
                    final boolean isSetterMethod;
                    final boolean isPropertyMethod;
                    final String propertyName;

                    if (beanClassMethodName.matches("is\\w+") && hasZeroParameters && hasBooleanReturnType) {

                        isGetterMethod = true;
                        isSetterMethod = false;
                        isPropertyMethod = false;
                        propertyName = makeFirstCharacterLowerCase(beanClassMethodName.substring(2));

                    } else if (beanClassMethodName.matches("get\\w+") && hasZeroParameters && !hasVoidReturnType) {

                        isGetterMethod = !beanClassMethodName.equals("getClass");
                        isSetterMethod = false;
                        isPropertyMethod = false;
                        propertyName = isGetterMethod ? makeFirstCharacterLowerCase(beanClassMethodName.substring(3))
                                : null;

                    } else if (beanClassMethodName.matches("set\\w+") && hasOneParameter && hasVoidReturnType) {

                        isGetterMethod = false;
                        isSetterMethod = true;
                        isPropertyMethod = false;
                        propertyName = makeFirstCharacterLowerCase(beanClassMethodName.substring(3));

                    } else if (beanClassMethodName.matches("\\w+Property") && hasZeroParameters && !hasVoidReturnType) {

                        isGetterMethod = false;
                        isSetterMethod = false;
                        isPropertyMethod = true;
                        propertyName = beanClassMethodName.substring(0, beanClassMethodName.length() - 8);

                    } else {
                        isGetterMethod = false;
                        isSetterMethod = false;
                        isPropertyMethod = false;
                        propertyName = null;
                    }

                    if (propertyName != null) {
                        if (!backingMap.containsKey(propertyName)) {
                            backingMap.put(propertyName, new BeanPropertyMetadata(propertyName));
                        }
                        final BeanPropertyMetadata beanPropertyMetadata = backingMap.get(propertyName);
                        if (isGetterMethod) {
                            beanPropertyMetadata.addGetterMethod(beanClassMethod);
                        }
                        if (isSetterMethod) {
                            beanPropertyMetadata.addSetterMethod(beanClassMethod);
                        }
                        if (isPropertyMethod) {
                            beanPropertyMetadata.addPropertyMethod(beanClassMethod);
                        }
                    }
                }
            }

            for (Entry<String, BeanPropertyMetadata> entry : backingMap.entrySet()) {
                if (!Objects.equals(entry.getKey(), entry.getValue().getPropertyName())) {
                    throw new Error("!Objects.equals(entry.getKey(), entry.getValue().getPropertyName())");
                }
            }

        } catch (Throwable e) {
            disposeMap(backingMap);
            throw e;
        }

        return backingMap;
    }

    // Return the given string -- except with its first character made lowercase.
    // If the given string is null or empty, just return the given string.
    private static String makeFirstCharacterLowerCase(String string) {
        if (string == null) {
            return string;
        } else if (string.isEmpty()) {
            return string;
        } else {
            return string.substring(0, 1).toLowerCase() + string.substring(1);
        }
    }

    @Override
    public String toString() {
        verifyNotDisposed();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("BeanPropertyMetadata [propertyName=");
        stringBuilder.append(propertyName);
        stringBuilder.append(", propertyType=");
        stringBuilder.append(getPropertyType());
        stringBuilder.append(", readable=");
        stringBuilder.append(isReadable());
        stringBuilder.append(", writable=");
        stringBuilder.append(isWritable());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public int hashCode() {
        verifyNotDisposed();
        return Objects.hash(getterMethods, setterMethods, propertyMethods, propertyName);
    }

    @Override
    public boolean equals(Object obj) {
        verifyNotDisposed();
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (!(obj instanceof BeanPropertyMetadata)) {
            return false;
        } else {
            BeanPropertyMetadata other = (BeanPropertyMetadata) obj;
            other.verifyNotDisposed();
            return this.getterMethods.equals(other.getterMethods)
                    && this.setterMethods.equals(other.setterMethods)
                    && this.propertyMethods.equals(other.propertyMethods)
                    && this.propertyName.equals(other.propertyName);
        }
    }

}