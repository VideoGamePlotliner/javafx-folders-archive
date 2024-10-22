package vgp.fx.converter;

import javafx.util.StringConverter;
import javafx.util.converter.*;
import vgp.clone.CloneableBase;

/**
 * An abstract class that directly extends {@code StringConverter} and redefines
 * that class's {@link #toString(Object)} method.
 * <p>
 * Subclasses should redefine the {@code fromString} method in order to complete
 * their concrete implementations of the {@code StringConverter} specifications.
 * <p>
 * Also noteworthy is that this class also implements {@code CloneableBase} from
 * the module {@code vgp.base}. Subclasses should redefine its {@code clone}
 * method in order to ensure proper cloneability according to that interface's
 * specifications. The version of {@code clone} declared in <em>this</em> class
 * is left {@code abstract}: otherwise, its return value would have to implement
 * {@code fromString}, which is not desirable for our purposes.
 * 
 * @author [...]
 * @version 6.1
 * @param <T> identical to {@code T} in superclass
 * @since 1.2
 */
public abstract class StringConverterBase<T> extends StringConverter<T>
		implements CloneableBase {

	/*
	 * Constructors
	 */

	/**
	 * Constructs a new {@code StringConverterBase}
	 * with the default characteristics.
	 */
	public StringConverterBase() {
		super();
	}

	/*
	 * Overriding methods
	 */

	/**
	 * Parse the string parameter.
	 * <p>
	 * <strong>Implementation Note:</strong>
	 * <p>
	 * A common implementation for subclasses of {@link StringConverter} is
	 * 
	 * <pre>
	 * if (string == null)
	 * 	return null;
	 * String trim = string.trim();
	 * return (trim.length() == 0) ? null : XXXX.valueOf(string);
	 * </pre>
	 * <p>
	 * where the {@code XXXX} is a stand-in for the first word in the subclass's
	 * name.
	 * <p>
	 * That implementation is used for all of the following classes:
	 * <ul>
	 * <li>{@link BooleanStringConverter}
	 * <li>{@link ByteStringConverter}
	 * <li>{@link DoubleStringConverter}
	 * <li>{@link FloatStringConverter}
	 * <li>{@link IntegerStringConverter}
	 * <li>{@link ShortStringConverter}
	 * </ul>
	 * <p>
	 * For {@link BigDecimalStringConverter}, the implementation is
	 * 
	 * <pre>
	 * if (string == null)
	 * 	return null;
	 * String trim = string.trim();
	 * return (trim.length() == 0) ? null : new BigDecimal(string); // NOT BigDecimal.valueOf(string)
	 * </pre>
	 * <p>
	 * For {@link BigIntegerStringConverter}, the implementation is
	 * 
	 * <pre>
	 * if (string == null)
	 * 	return null;
	 * String trim = string.trim();
	 * return (trim.length() == 0) ? null : new BigInteger(string); // NOT BigInteger.valueOf(string)
	 * </pre>
	 * <p>
	 * For {@link CharacterStringConverter}, the implementation is
	 * 
	 * <pre>
	 * if (string == null)
	 * 	return null;
	 * String trim = string.trim();
	 * return (trim.length() == 0) ? null : Character.valueOf(string.charAt(0)); // NOT Character.valueOf(string)
	 * </pre>
	 * <p>
	 * For {@link DefaultStringConverter}, the implementation is
	 * 
	 * <pre>
	 * return string;
	 * </pre>
	 * 
	 * @param string the text to parse
	 * 
	 * @return an object extracted from the parameter
	 */
	@Override
	public abstract T fromString(String string);

	/**
	 * Convert the parameter into a string.
	 * <p>
	 * <strong>Implementation Note:</strong>
	 * <p>
	 * A common implementation for subclasses of {@link StringConverter} is
	 * 
	 * <pre>
	 * return object == null ? "" : object.toString();
	 * </pre>
	 * <p>
	 * That implementation is used for {@code StringConverterBase} and for
	 * all of the following classes:
	 * <ul>
	 * <li>{@link BigDecimalStringConverter}
	 * <li>{@link BigIntegerStringConverter}
	 * <li>{@link BooleanStringConverter}
	 * <li>{@link ByteStringConverter}
	 * <li>{@link CharacterStringConverter}
	 * <li>{@link DoubleStringConverter}
	 * <li>{@link FloatStringConverter}
	 * <li>{@link IntegerStringConverter}
	 * <li>{@link ShortStringConverter}
	 * </ul>
	 * <p>
	 * For {@link DefaultStringConverter}, the implementation is
	 * 
	 * <pre>
	 * return object == null ? "" : object;
	 * </pre>
	 * 
	 * @param object the object to turn into a string
	 * 
	 * @return if {@code object} is {@code null}, the empty string;
	 *         otherwise, {@code object.toString()}
	 */
	@Override
	public String toString(T object) {
		return object == null ? "" : object.toString();
	}

	@Override
	public abstract StringConverterBase<T> clone();

}
