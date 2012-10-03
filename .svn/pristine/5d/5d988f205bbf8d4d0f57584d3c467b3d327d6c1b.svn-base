package com.cesaco.mobias.cquery;

// TODO: Auto-generated Javadoc
/**
 * The Class Query.
 */
public abstract class Query {

	/** The query. */
	private String query;
	
	/** The guessed time. */
	private long guessedTime;
	
	/**
	 * Instantiates a new query.
	 */
	public Query() {
		query = "empty";
		guessedTime = -1;
	}
	
	/**
	 * Local set query.
	 *
	 * @param qparams the qparams
	 * @return the string
	 * @throws QueryException the query exception
	 */
	protected abstract String localSetQuery(QParams qparams) throws QueryException;
	
	/**
	 * Local get guessed time in milliseconds.
	 *
	 * @return the long
	 */
	protected abstract long localGetGuessedTime();
	
	/**
	 * Sets the query.
	 *
	 * @param qparams the new query
	 * @throws QueryException the query exception
	 */
	public void setQuery(QParams qparams) throws QueryException {
		query = localSetQuery(qparams); 
	}
	
	/**
	 * Gets the query.
	 *
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}
	
	/**
	 * Sets the guessed time.
	 */
	public void setGuessedTime() {
		guessedTime = localGetGuessedTime();
	}
	
	/**
	 * Gets the guessed time.
	 *
	 * @return the guessed time
	 */
	public long getGuessedTime() {
		return guessedTime;
	}
	
}
