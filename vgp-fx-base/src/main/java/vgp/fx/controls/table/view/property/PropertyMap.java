package vgp.fx.controls.table.view.property;

import java.time.LocalDate;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentSkipListMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.paint.Color;
import vgp.dispose.Disposable;
import vgp.fx.objects.ColoredDate;

// Unmodifiable after construction -- except for disposal.
// Despite the name, not actually a subtype of java.util.Map.
/**
 * @version 5.5
 */
final class PropertyMap implements Disposable {

    private final Class<?> beanClass;
    private boolean isDisposed = false;
    private final ConcurrentSkipListMap<String, BeanPropertyMetadata> backingMap;

    public PropertyMap(final Class<?> beanClass) {
        super();
        try {
            this.beanClass = Objects.requireNonNull(beanClass);
            this.backingMap = Objects.requireNonNull(BeanPropertyMetadata.createBackingMap(beanClass));
        } catch (Throwable e) {
            dispose();
            throw e;
        }
    }

    final Class<?> getBeanClass() {
        verifyNotDisposed();
        return beanClass;
    }

    @Override
    public void dispose() {
        if (isDisposed()) {
            return;
        }
        isDisposed = true;
        BeanPropertyMetadata.disposeMap(backingMap);
    }

    @Override
    public boolean isDisposed() {
        return isDisposed;
    }

    @Override
    public String toString() {
        verifyNotDisposed();
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PropertyMap [backingMap=");
        if (!backingMap.isEmpty()) {
            for (Entry<String, BeanPropertyMetadata> entry : backingMap.entrySet()) {
                stringBuilder.append(System.lineSeparator());
                stringBuilder.append('\t');
                stringBuilder.append(entry);
            }
            stringBuilder.append(System.lineSeparator());
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public int hashCode() {
        verifyNotDisposed();
        return backingMap.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        verifyNotDisposed();
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (!(obj instanceof PropertyMap)) {
            return false;
        } else {
            PropertyMap other = (PropertyMap) obj;
            other.verifyNotDisposed();
            return this.backingMap.equals(other.backingMap);
        }
    }

    final ObservableList<BeanPropertyMetadata> getMetadataList() {
        verifyNotDisposed();
        return FXCollections.observableArrayList(backingMap.values());
    }

    /**
     * The entry point for this class to be run from the command line.
     * 
     * @param args the string array passed from the command line
     */
    public static void main(String[] args) {
        final PropertyMap propertyMap1 = new PropertyMap(ColoredDate.class);
        final PropertyMap propertyMap2 = new PropertyMap(ColoredDate.class);

        System.out.println(propertyMap1.hashCode());
        System.out.println(propertyMap2.hashCode());
        System.out.println();

        System.out.println(propertyMap1.equals(propertyMap2));
        System.out.println(propertyMap2.equals(propertyMap1));
        System.out.println();

        System.out.println(propertyMap1);
        System.out.println(propertyMap2);
        System.out.println();

        System.out.println(new PropertyMap(Color.class));
        System.out.println();

        System.out.println(new PropertyMap(LocalDate.class));
        System.out.println();

        System.out.println(new PropertyMap(TreeItem.class));
        System.out.println();

        System.out.println(new PropertyMap(Scene.class));
        System.out.println();

        System.out.println(new PropertyMap(Parent.class));
        System.out.println();
    }
}
