/*
package org.goafabric.calleeservice.state;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JavaStateMachine {
    enum State {
        NEW, PROCESSING, COMPLETED
    }

    enum Event {
        START, FINISH, FAIL
    }

    private State currentState = State.NEW;

    private static final Map<State, Map<Event, State>> transitions = Map.of(
            State.NEW, Map.of(Event.START, State.PROCESSING),
            State.PROCESSING, Map.of(Event.FINISH, State.COMPLETED)
    );

    public void sendEvent(Event event) {
        if (transitions.containsKey(currentState) && transitions.get(currentState).containsKey(event)) {
            currentState = transitions.get(currentState).get(event);
            System.out.println("Java State changed to: " + currentState);
        } else {
            System.out.println("Java Invalid transition from " + currentState + " using " + event);
        }
    }

    public State getState() {
        return currentState;
    }

    @Bean
    public CommandLineRunner initJavaState(JavaStateMachine stateMachine) {
        return args -> {
            stateMachine.sendEvent(Event.START);
            stateMachine.sendEvent(Event.FINISH);
        };
    }
}
*/
