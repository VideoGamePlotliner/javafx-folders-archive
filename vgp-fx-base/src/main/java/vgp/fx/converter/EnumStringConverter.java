package vgp.fx.converter;

/**
 * A concrete class that directly extends {@code StringConverterBase} and
 * redefines that class's {@link #fromString} method.
 * <p>
 * This class's implementation of said method tolerates {@code null} parameters
 * and relies on {@link Enum#valueOf(Class, String)} when dealing with
 * non-{@code null} parameters.
 * <p>
 * Also noteworthy is that this class also implements {@code CloneableBase} from
 * the module {@code vgp.base}. Subclasses should redefine its {@code clone}
 * method in order to ensure proper cloneability according to that interface's
 * specifications.
 * 
 * @author [...]
 * @version 6.1
 * @param <T> identical to {@code T} in superclass
 * @since 1.3
 */
public class EnumStringConverter<T extends Enum<T>> extends StringConverterBase<T> {

	/**
	 * The {@link Class} for the type {@code T}.
	 * <p>
	 * This field's value must not be {@code null}.
	 */
	private final Class<T> enumType;

	/**
	 * Constructs a new {@code EnumStringConverter} with the specified enum type.
	 * 
	 * @param enumType the {@link Class} for the type {@code T}
	 * 
	 * @throws NullPointerException     if {@code enumType} is {@code null}
	 * 
	 * @throws IllegalArgumentException if {@code enumType} is not, in fact, an
	 *                                  {@linkplain Class#isEnum() enum type}
	 */
	public EnumStringConverter(Class<T> enumType) {
		super();
		if (enumType == null) {
			throw new NullPointerException("Null parameter");
		}
		if (!enumType.isEnum()) {
			throw new IllegalArgumentException("Non-enum parameter");
		}
		this.enumType = enumType;
	}

	/**
	 * Returns the enum type of this {@link EnumStringConverter}.
	 * 
	 * @return the {@link Class} for the type {@code T}
	 */
	public Class<T> getEnumType() {
		return enumType;
	}

	/**
	 * @return {@code null} if {@code string} is {@code null} or merely whitespace
	 *         characters; otherwise, the corresponding enum constant
	 * 
	 * @throws EnumConstantNotPresentException if {@code string} is not {@code null}
	 *                                         and is not merely whitespace
	 *                                         characters and does not correspond to
	 *                                         any enum constant for this string
	 *                                         converter's enum type
	 */
	@Override
	public T fromString(String string) {
		if (string == null)
			return null;
		String trim = string.trim();
		return (trim.length() == 0) ? null : Enum.valueOf(enumType, string);
	}

	@Override
	public EnumStringConverter<T> clone() {
		return new EnumStringConverter<>(enumType);
	}

}
