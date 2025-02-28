package com.aionl.slf4j.filters;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class VeteranRewardsFilter extends Filter<ILoggingEvent>
{
	@Override
	public FilterReply decide(ILoggingEvent loggingEvent)
	{
		String message = loggingEvent.getMessage();

		return message.startsWith("[VETERANREWARD]") ? FilterReply.ACCEPT : FilterReply.DENY;
	}
}
