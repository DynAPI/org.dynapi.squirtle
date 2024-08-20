package org.dynapi.squirtle.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

public class CloneUtils {
    /**
     * clones an instance with the copy-constructor of the class. <br>
     * note: mostly used for internal classes
     * @param instance instance to clone
     * @return clone
     */
    public static <T> T copyConstructorClone(T instance) {
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) instance.getClass();
        Constructor<T> copyConstructor;
        try {
            copyConstructor = clazz.getConstructor(clazz);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("no copy-constructor available to copy", e);
        }
        try {
            return copyConstructor.newInstance(instance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Copy execution failed with " + e.getClass().getName(), e);
        }
    }

    /**
     * deeper copyConstructorClone of a list
     * @param collection collection to clone
     * @return cloned collection
     * @see CloneUtils#copyConstructorClone(Object)
     */
    public static <C extends Collection<T>, T> C copyConstructorCloneCollection(C collection) {
        @SuppressWarnings("unchecked")
        Class<C> clazz = (Class<C>) collection.getClass();
        List<T> values = collection.stream().map(CloneUtils::copyConstructorClone).toList();
        Constructor<C> collectionConstructor;
        try {
            collectionConstructor = clazz.getConstructor(Collection.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("failed to get " + clazz.getName() + " constructor", e);
        }
        try {
            return collectionConstructor.newInstance(values);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Copy execution failed with " + e.getClass().getName(), e);
        }
    }

    /**
     * like {@link CloneUtils#copyConstructorClone(Object)} this method attempts to clone an instance with the copy-constructor of the class,
     * but when this fails the original value is returned.
     * @param instance instance to clone
     * @return clone or original
     * @see CloneUtils#copyConstructorClone(Object)
     */
    public static <T> T copyConstructorCloneNoFail(T instance) {
        try {
            return copyConstructorClone(instance);
        } catch (RuntimeException e) {
            return instance;
        }
    }
}
