module com.mechanitis.demo.sense.user {
    requires com.mechanitis.demo.sense.service;
    requires com.mechanitis.demo.sense.flow;

    // TODO: split the test application into its own module
    requires com.mechanitis.demo.sense.service.test;
}
