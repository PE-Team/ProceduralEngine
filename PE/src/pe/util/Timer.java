package pe.util;

import pe.engine.main.PE;

/**
 * Represents a Timer in seconds. After constructing it, the method
 * <code>start</code> must be called for all non-static methods in
 * <code>Timer</code> to work, with the exception of setter methods.
 * <p>
 * The Timer may be used to get how long it has been since the last call of
 * <code>getDelta</code>. The Timer may be also used to execute functions only
 * once per second by using <code>secondPassed</code> in an <code>if</code>
 * statement. The Timer can also be set to have a custom <code>delay</code>. If
 * set, <code>delayPassed</code> may be used to determine whether an amount of
 * time has passed equal to or greater than <code>delay</code>.
 * 
 * @author Ethan Penn
 *
 * @see #delayPassed()
 * @see #getCurrentTime()
 * @see #getDelta()
 * @see #secondPassed()
 * @see #start()
 * @see #delay
 * 
 * @since 1.0
 */
public class Timer {

	/**
	 * Represents the time that <code>start</code> was called in seconds.
	 * 
	 * @see #start()
	 * 
	 * @since 1.0
	 */
	private float startTime;

	/**
	 * Represents the time since either <code>start</code> or
	 * <code>getDelta</code> was called in seconds.
	 * 
	 * @see #getDelta()
	 * @see #start()
	 * 
	 * @since 1.0
	 */
	private float lastDelta;

	/**
	 * Represents the time since either <code>start</code> was called or since
	 * <code>secondPassed</code> returned <code>true</code> in seconds.
	 * 
	 * @see #secondPassed()
	 * @see #start()
	 * 
	 * @since 1.0
	 */
	private float lastSecond;

	/**
	 * Represents the time since either <code>start</code> was called or since
	 * <code>delayPassed</code> returned <code>true</code> in seconds.
	 * 
	 * @see #delayPassed()
	 * @see #start()
	 * 
	 * @since 1.0
	 */
	private float lastDelay;

	/**
	 * Represents the time required since <code>lastDelay</code> for
	 * <code>delayPassed</code> to return <code>true</code>.
	 * 
	 * @see #delayPassed()
	 * @see #lastDelay
	 * 
	 * @since 1.0
	 */
	private float delay;

	/**
	 * Default Class Constructor. Calling <code>start</code> after constructing
	 * a <code>Timer</code> object is required for all non-static methods to
	 * operate correctly.
	 * 
	 * @see #start()
	 * 
	 * @since 1.0
	 */
	public Timer() {

	}

	/**
	 * Class Constructor which sets <code>delay</code> to be equal to the
	 * parameter <code>delay</code>. Calling <code>start</code> after
	 * constructing a <code>Timer</code> object is required for all non-static
	 * methods to operate correctly.
	 * 
	 * @param delay
	 *            The delay in seconds required for <code>delayPassed</code> to
	 *            return <code>true</code>.
	 * 
	 * @see #delayPassed()
	 * @see #start()
	 * @see #delay
	 * 
	 * @since 1.0
	 */
	public Timer(float delay) {
		this.delay = delay;
	}

	/**
	 * Returns <code>true</code> if an amount of time equal to or greater than
	 * <code>delay</code> has passed since the last time <code>lastDelay</code>
	 * was updated. If it returns <code>true</code>, then <code>lastDelay</code>
	 * is automatically set to <code>getCurrentTime</code>.
	 * 
	 * @return <code>true</code> if an amount of time of at least
	 *         <code>delay</code> in seconds has passed since
	 *         <code>lastDelay</code>; <code>false</code> otherwise.
	 * 
	 * @exception NullPointerException
	 *                if <code>start</code> is not called before this method.
	 * 
	 * @see #getCurrentTime()
	 * @see #delay
	 * @see #lastDelay
	 * 
	 * @since 1.0
	 */
	public boolean delayPassed() {
		float result = getCurrentTime() - lastDelay;
		if (result > delay) {
			lastDelay = getCurrentTime();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the current time in seconds as a <code>float</code>.
	 * 
	 * @return The current time in seconds.
	 * 
	 * @since 1.0
	 */
	public static float getCurrentTime() {
		return System.nanoTime() / PE.NANO;
	}

	/**
	 * Returns the amount of time in seconds between <code>getCurrentTime</code>
	 * and <code>lastDelta</code>, and then sets <code>lastDelta</code> to be
	 * equal to <code>getCurrentTime</code>.
	 * 
	 * @return The time in seconds since the last call of <code>getDelta</code>.
	 * 
	 * @exception NullPointerException
	 *                if <code>start</code> is not called before this method.
	 * 
	 * @see #getCurrentTime()
	 * @see #lastDelta
	 * 
	 * @since 1.0
	 */
	public float getDelta() {
		float result = getCurrentTime() - lastDelta;
		lastDelta = getCurrentTime();
		return result;
	}

	/**
	 * Returns the time when the Timer was started, which is equal to the value
	 * of <code>startTime</code>.
	 * 
	 * @return The time <code>start</code> was called in seconds.
	 * 
	 * @exception NullPointerException
	 *                if <code>start</code> is not called before this method.
	 * 
	 * @see #startTime
	 * 
	 * @since 1.0
	 */
	public float getStart() {
		return startTime;
	}

	/**
	 * Returns the amount of time that has passed between
	 * <code>getCurrentTime</code> and <code>startTime</code> in seconds. This
	 * is also equal to the time since <code>start</code> was called.
	 * 
	 * @return The time since the Timer was started
	 * 
	 * @exception NullPointerException
	 *                if <code>start</code> is not called before this method.
	 *
	 * 
	 * @see #getCurrentTime()
	 * @see #start()
	 * @see #startTime
	 * 
	 * @since 1.0
	 */
	public float getTime() {
		return getCurrentTime() - startTime;
	}

	/**
	 * Returns whether a second has passed since the Timer has started or if a
	 * second has passed since the last time this function returned
	 * <code>true</code>.
	 * 
	 * @return <code>true</code> if a second has passed since <code>start</code>
	 *         was called or <code>secondPassed</code> returned true.
	 * 
	 * @exception NullPointerException
	 *                if <code>start</code> is not called before this method.
	 * 
	 * @see #getCurrentTime()
	 * @see #start()
	 * @see #lastSecond
	 * 
	 * @since 1.0
	 */
	public boolean secondPassed() {
		float result = getCurrentTime() - lastSecond;
		if (result > 1) {
			lastSecond = getCurrentTime();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets the duration of time in seconds needed before
	 * <code>delayPassed</code> would return true either after starting the
	 * Timer or after the last time <code>delayPassed</code> returned
	 * <code>true</code>.
	 * 
	 * @param delay
	 * 
	 * @see #delayPassed()
	 * @see #start()
	 * @see #delay
	 * 
	 * @since 1.0
	 */
	public void setDelay(float delay) {
		this.delay = delay;
	}

	/**
	 * This method must be called before using any non-static methods within
	 * this class except setter methods. Not calling this method will throw a
	 * <code>NullPointerException</code> if any of the non-static and non-setter
	 * methods are called.Starts the timer by setting <code>startTime</code>,
	 * <code>lastDelta</code>, <code>lastSecond</code>, and
	 * <code>lastDelay</code>, and will be initially equal to one another.
	 * <code>lastDelta</code>, <code>lastSecond</code>, and
	 * <code>lastDelay</code> are initially set to be equal to
	 * <code>startTime</code> which is set to be <code>getCurrentTime</code>.
	 * 
	 * @see #getCurrentTime()
	 * @see #lastDelay
	 * @see #lastDelta
	 * @see #lastSecond
	 * @see #startTime
	 * 
	 * @since 1.0
	 */
	public void start() {
		startTime = getCurrentTime();
		lastDelta = startTime;
		lastSecond = startTime;
		lastDelay = startTime;
	}
}
