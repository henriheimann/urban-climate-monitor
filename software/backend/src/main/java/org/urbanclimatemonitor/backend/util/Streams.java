package org.urbanclimatemonitor.backend.util;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@UtilityClass
public class Streams
{
	public static <T> Stream<T> stream(Iterable<T> iterable)
	{
		if (iterable instanceof Collection) {
			return ((Collection<T>) iterable).stream();
		} else {
			return StreamSupport.stream(iterable.spliterator(), false);
		}
	}
}
