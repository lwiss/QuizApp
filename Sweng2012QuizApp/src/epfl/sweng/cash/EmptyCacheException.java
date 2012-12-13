package epfl.sweng.cash;

/**
 * This is an exception that should be thrown when the proxy requests the cache
 * but this last is empty
 * 
 * @author wissem(wissem.allouchi@epfl.ch)
 * 
 */
public class EmptyCacheException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "The cahe is now Empty";
	}

}
