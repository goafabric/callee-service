//package org.goafabric.calleeservice.state;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.statemachine.StateMachine;
//import org.springframework.statemachine.config.EnableStateMachine;
//import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
//import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
//import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
//import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
//import org.springframework.statemachine.listener.StateMachineListener;
//import org.springframework.statemachine.listener.StateMachineListenerAdapter;
//
//import java.util.EnumSet;
//
//@Configuration
//@EnableStateMachine
//public class SpringStateMachine
//        extends EnumStateMachineConfigurerAdapter<SpringStateMachine.State, SpringStateMachine.Event> {
//
//    enum State {
//        NEW, PROCESSING, COMPLETED,
//    }
//
//    enum Event {
//        START, FINISH
//    }
//
//    @Override
//    public void configure(StateMachineConfigurationConfigurer<State, Event> config)
//            throws Exception {
//        config
//                .withConfiguration()
//                .autoStartup(true)
//                .listener(listener());
//    }
//
//    @Override
//    public void configure(StateMachineStateConfigurer<State, Event> states)
//            throws Exception {
//        states
//                .withStates()
//                .initial(State.NEW)
//                .states(EnumSet.allOf(State.class));
//    }
//
//    @Override
//    public void configure(StateMachineTransitionConfigurer<State, Event> transitions)
//            throws Exception {
//        transitions
//                .withExternal()
//                .source(State.NEW).target(State.PROCESSING).event(Event.START)
//                .and()
//                .withExternal()
//                .source(State.PROCESSING).target(State.COMPLETED).event(Event.FINISH);
//    }
//
//    @Bean
//    public StateMachineListener<State, Event> listener() {
//        return new StateMachineListenerAdapter<State, Event>() {
//            @Override
//            public void stateChanged(org.springframework.statemachine.state.State<State, Event> from, org.springframework.statemachine.state.State<State, Event> to) {
//                System.out.println("State change to " + to.getId());
//            }
//        };
//    }
//
//    @Bean
//    public CommandLineRunner initSpringState(StateMachine<State, Event> stateMachine) {
//        return args -> {
//            stateMachine.sendEvent(Event.START);
//            stateMachine.sendEvent(Event.FINISH);
//        };
//    }
//
//}
//
