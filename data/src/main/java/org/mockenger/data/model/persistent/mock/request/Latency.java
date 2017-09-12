package org.mockenger.data.model.persistent.mock.request;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Dmitry Ryazanov
 */
@Getter
@Builder
public class Latency {
	private final long fixed;
	private final long min;
	private final long max;
}
