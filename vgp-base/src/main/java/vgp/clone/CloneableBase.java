package vgp.clone;

/**
 * An interface that directly extends {@code Cloneable} and redefines the
 * {@code clone} method in the {@code Object} class.
 * <p>
 * There are multiple reasons for said redefinition.
 * <ol>
 * <li>First of all, the {@code clone} method is {@code protected} by
 * default, meaning that the method is not required to be usable outside
 * its class.
 * <li>Second, unless a subclass implements {@code Cloneable}, the
 * default implementation throws {@code CloneNotSupportedException}.
 * This means that, if a subclass simply overrides the method by
 * declaring it {@code public} without changing the implementation, then
 * all calls to the {@code clone} method will throw that exception. This
 * could easily happen if the programmer forgets to add {@code implements
 * Cloneable} to the class header.
 * <li>Third, the default return type is {@code Object}, which means
 * that the caller of even a <em>working</em> {@code clone} method will
 * often have to cast from an {@code Object} to the subclass type.
 * <li>Lastly, because the default return type is {@code Object}, the
 * method's return <em>value</em> is not guaranteed to itself be {@code
 * Cloneable}.
 * </ol>
 * <p>
 * Now, all of these inconveniences can be simultaneously avoided
 * by implementing an interface that redefines the {@code clone} method
 * in a certain manner. This interface does just that: by exploiting
 * interfaces' ability to override {@code Object} methods (provided that
 * they are declared {@code abstract}), this interface declares that its
 * {@code clone} must be {@code public} (which all interface methods are
 * by default), never throw {@code CloneNotSupportedException}, and have
 * a return type that is <em>itself</em> {@code Cloneable} and is not
 * merely {@code Object}.
 * 
 * @author [...]
 * @version 1.1
 * @since 1.1
 */
public interface CloneableBase extends Cloneable {

	/*
	 * Overriding methods
	 */

	/**
	 * {@inheritDoc}
	 */
	CloneableBase clone();

}
