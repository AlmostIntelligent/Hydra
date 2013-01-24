package org.gethydrated.hydra.actors.scheduling;

/**
 * Timer queue implemented as hash wheel. Each {@link TimerTask} is scheduled
 * in the appropriate bucket.
 * <p/>
 * On every tick, the {@link HashedTimingWheel} will check if there are any
 * {@link TimerTask}s ready to be executed. The {@link TimerTask}s are stored
 * in an ordered set for each time slot.
 * <p/>
 * The accuracy of the {@link HashedTimingWheel} based timer depends on the amount
 * of time slots in the wheel.
 * <p/>
 * For more information, see George Varghese and Tony Lauck's paper at
 * <a href="http://www.cs.columbia.edu/~nahum/w6998/papers/sosp87-timing-wheels.pdf">
 *     http://www.cs.columbia.edu/~nahum/w6998/papers/sosp87-timing-wheels.pdf</a>
 */
public class HashedTimingWheel implements Runnable {


    @Override
    public void run() {

    }
}
