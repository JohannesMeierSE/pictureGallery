package picturegallery.persistency;

/*-
 * BEGIN-LICENSE
 * picturegallery
 * %%
 * Copyright (C) 2016 - 2022 Johannes Meier
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * END-LICENSE
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Loading priority: 1. number of requests descending, 2. time of last request (last time is more important)
 * @author Johannes Meier
 *
 * @param <K> type of the key
 * @param <V> type of the value
 */
// https://commons.apache.org/proper/commons-collections/apidocs/org/apache/commons/collections4/map/LRUMap.html
public abstract class ObjectCache<K, V> { // hier: (RealPicture -> Image)
	/**
	 * Will be used for listeners which want to be notified if the loading finished.
	 * @author Johannes Meier
	 *
	 * @param <K>
	 * @param <V>
	 */
	public interface CallBack<K, V> {
		public void loaded(K key, V value);
	}

	public interface AlternativeWorker {
		public boolean hasStillWork();
		public void doSomeWork();
	}

	class Tripel {
		public final K key;
		public final List<CallBack<K, V>> callbacks;
		public final WrappedInteger count;

		public Tripel(K key) {
			super();
			this.key = key;
			this.callbacks = new ArrayList<>();
			this.count = new WrappedInteger(1);
		}
	}
	class Double {
		public final V value;
		public final WrappedInteger count;

		public Double(V value, int count) {
			super();
			this.value = value;
			this.count = new WrappedInteger(count);
		}
	}
	class WrappedInteger {
		private int value;

		public WrappedInteger(int value) {
			super();
			this.value = value;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		public void increment() {
			this.value++;
		}
	}

	protected final Object sync = new Object();

	protected final Map<K, Double> content; // Relevanz speichern, um das Entfernen besser steuern zu können! => wird erstmal ignoriert!
	protected final Queue<K> contentSorted; // keys of the content-map in sorted order: (ignored: 1. count descending) 2. time (from oldest to newest)
	protected final List<Tripel> loading; // order is not relevant
	protected final List<Tripel> requested; // first element will be loaded next
	protected final int maxSize;

	private Thread thread;
	private final AtomicBoolean stopped;

	protected AlternativeWorker alternativeWorker;

	public ObjectCache() {
		this(20, false);
	}
	public ObjectCache(int size) {
		this(size, true);
	}
	private ObjectCache(int size, boolean freezeSize) {
		super();
		maxSize = size;
		content = new HashMap<>(maxSize * 2);
		contentSorted = new LinkedList<>();
		loading = new ArrayList<>(2);
		requested = new ArrayList<>(maxSize);

		stopped = new AtomicBoolean(true);
		thread = new Thread() {
			@Override
			public void run() {
				while (!isInterrupted() && !stopped.get()) {
					// wait, until there are requested pictures to load
					while (isRequestedEmpty() && !stopped.get()) {
						if (alternativeWorker != null && alternativeWorker.hasStillWork()) {
							// instead of "waiting", do some other work!
							alternativeWorker.doSomeWork();
						} else {
							try {
								synchronized (sync) {
									sync.wait();
								}
							} catch (InterruptedException e) {
							}
						}
					}
					// loading was stopped (currently, no requests available)
					if (stopped.get()) {
						System.out.println("Loading thread: ready and stopped");
						return;
					}

					// load the next element
					Tripel next = null;
					synchronized (sync) {
						next = requested.remove(0);
						loading.add(0, next);
					}
					V loadedValue = load(next.key);
					if (loadedValue == null) {
						// ignore this picture:
						synchronized (sync) {
							loading.remove(next);
						}
					} else {
						// handle the loaded value
						synchronized (sync) {
							// Grenze berücksichtigen!! und Elemente wieder rauslöschen!
							while (freezeSize && content.size() >= maxSize) {
								K keyToRemove = contentSorted.remove();
								content.remove(keyToRemove);
							}
							// save new key
							content.put(next.key, new Double(loadedValue, next.callbacks.size()));
							contentSorted.add(next.key);
							// key is not loading anymore
							loading.remove(next);
						}
						// notification for the listeners, that this picture was loaded successfully
						for (CallBack<K, V> call : next.callbacks) {
							call.loaded(next.key, loadedValue);
						}
					}
				}
				// there were still requests for loading ...
				System.out.println("Loading thread: working, but stopped");
			}
		};
	}

	public void start() {
		stopped.set(false);
		thread.start();
	}

	public void stop() {
		stopped.set(true);
		thread.interrupt();
		synchronized (sync) {
			content.clear();
			contentSorted.clear();
			loading.clear();
			requested.clear();

			sync.notifyAll(); // => activate the thread, if there was nothing to load!
		}
		// https://stackoverflow.com/questions/5690309/garbage-collector-in-java-set-an-object-null
		System.gc();
	}

	protected abstract V load(K key);

	/**
	 * Checks, if there are requested pictures to load in a synchronized way!
	 * @return
	 */
	private boolean isRequestedEmpty() {
		boolean result = true;
		synchronized (sync) {
			result = requested.isEmpty();
		}
		return result;
	}

	public boolean isLoaded(K key) {
		synchronized (sync) {
			return content.containsKey(key);
		}
	}

	public boolean isLoading(K key) {
		synchronized (sync) {
			return contains(key, loading) != null;
		}
	}

	public boolean isRequested(K key) {
		synchronized (sync) {
			return contains(key, requested) != null;
		}
	}

	public boolean isLoadedOrLoading(K key) {
		synchronized (sync) {
			return isLoaded(key) || isLoading(key);
		}
	}

	private Tripel contains(K key, List<Tripel> list) {
		for (Tripel k : list) {
			if (k.key.equals(key)) {
				return k;
			}
		}
		return null;
	}

	private int indexOf(K key, List<Tripel> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).key.equals(key)) {
				return i;
			}
		}
		return -1;
	}

	public void remove(K key) {
		synchronized (sync) {
			Double found = content.remove(key);
			contentSorted.remove(key);
			if (found == null) {
				int index = indexOf(key, requested);
				if (index >= 0) {
					requested.remove(index);
				} else {
					// the ObjectCache is currently loading the element to remove => do not interrupt loading => reduces possible errors
				}
			}
		}
	}

	public void request(K key, CallBack<K, V> callback) {
		if (key == null) {
			throw new IllegalArgumentException("key is null!");
		}

		Double value = null;
		synchronized (sync) {
			// already contained?
			value = content.get(key);
			if (value == null) {

				// currently loading?
				Tripel exist = contains(key, loading);
				if (exist != null) {
					if (callback != null) {
						exist.callbacks.add(callback);
					}
					return;
				}

				// request!
				int oldIndex = indexOf(key, requested);
				if (oldIndex >= 0) {
					exist = requested.get(oldIndex);
				}
				if (exist == null) {
					exist = new Tripel(key);
				} else {
					exist.count.increment();
				}
				if (callback != null) {
					exist.callbacks.add(callback);
				}
				boolean wasEmpty = requested.isEmpty();
				int newIndex = 0;
				while (newIndex < requested.size() && requested.get(newIndex).count.getValue() > exist.count.getValue()) {
					newIndex++;
				}
				requested.add(newIndex, exist);
				if (wasEmpty) {
					// start the loading thread again
					sync.notifyAll();
				}
				return;
			} else {
				// move the requested and available element to "the end"
				contentSorted.remove(key);
				contentSorted.add(key);
			}
		}
		if (value != null) {
			value.count.increment();
			if (callback != null) {
				callback.loaded(key, value.value);
			}
		}
	}

	public void setAlternativeWorker(AlternativeWorker alternativeWorker) {
		synchronized (sync) {
			this.alternativeWorker = alternativeWorker;

			/* wakes the loading thread up to use this worker!
			 * If the loading thread is currently loading, than nothing will happen!
			 */
			sync.notifyAll();
		}
	}
}
