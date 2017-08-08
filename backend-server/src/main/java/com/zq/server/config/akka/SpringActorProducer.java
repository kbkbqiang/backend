package com.zq.server.config.akka;

import org.springframework.context.ApplicationContext;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;

public class SpringActorProducer implements IndirectActorProducer {

	final private ApplicationContext applicationContext;
	final private String actorBeanName;

	public SpringActorProducer(ApplicationContext applicationContext, String actorBeanName) {
		this.applicationContext = applicationContext;
		this.actorBeanName = actorBeanName;
	}

	@Override
	public Actor produce() {
		return (Actor) applicationContext.getBean(actorBeanName);
	}

	@Override
	public Class<? extends Actor> actorClass() {
		return (Class<? extends Actor>) applicationContext.getType(actorBeanName);
	}
}
