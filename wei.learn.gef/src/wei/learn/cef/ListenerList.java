package wei.learn.cef;


import java.lang.reflect.Array;

public class ListenerList<T> {
	private T[] listeners;
	private Class<T> elementType;

	public ListenerList(Class<T> elementType) {
		this.elementType = elementType;
		this.listeners = (T[]) Array.newInstance(elementType, 0);
	}

	public void add(T listener) {
		Object[] newListeners = (Object[]) Array.newInstance(this.elementType,
				this.listeners.length + 1);
		System.arraycopy(this.listeners, 0, newListeners, 0,
				this.listeners.length);
		newListeners[newListeners.length - 1] = listener;
		this.listeners = (T[]) newListeners;
	}

	public void remove(T listener) {
		if (this.listeners.length != 0) {
			int index = -1;

			for (int newListeners = 0; newListeners < this.listeners.length; ++newListeners) {
				if (listener == this.listeners[newListeners]) {
					index = newListeners;
					break;
				}
			}

			if (index != -1) {
				if (this.listeners.length == 1) {
					this.listeners = (T[]) Array.newInstance(
							this.elementType, 0);
				} else {
					Object[] arg3 = (Object[]) Array.newInstance(
							this.elementType, this.listeners.length - 1);
					System.arraycopy(this.listeners, 0, arg3, 0, index);
					System.arraycopy(this.listeners, index + 1, arg3, index,
							this.listeners.length - index - 1);
					this.listeners = (T[]) arg3;
				}
			}
		}
	}

	public T[] getListeners() {
		return this.listeners;
	}
}