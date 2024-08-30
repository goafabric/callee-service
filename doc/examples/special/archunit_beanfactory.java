static class DoNotIncludeAOTClasses implements ImportOption {

        @Override
        public boolean includes(Location location) {
            Pattern beanFactoryRegistrationsPatterns = Pattern.compile(".*_BeanFactoryRegistrations.*");
            return !location.matches(beanFactoryRegistrationsPatterns);
        }
    }