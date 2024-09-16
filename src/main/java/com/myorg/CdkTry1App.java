package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;

public class CdkTry1App {
    public static void main(final String[] args) {
        App app = new App();

        new InstanciaEC2(app, "CdkTry1Stack", StackProps.builder()
                .build());

        app.synth();
    }
}
