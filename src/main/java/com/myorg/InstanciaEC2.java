package com.myorg;

import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.s3.Bucket;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import java.util.List;
import java.util.Map;

public class InstanciaEC2 extends Stack {
    // InstanciaEC2 hereda de Stack
    //
    public InstanciaEC2(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public InstanciaEC2(final Construct scope, final String id, StackProps props) {
        super(scope, id, props);

        // Subnet Configuration: https://docs.aws.amazon.com/cdk/api/v2/java/software/amazon/awscdk/services/ec2/Vpc.Builder.html#natGateways(java.lang.Number)
        Vpc vpc = Vpc.Builder.create(this, "VPC")
                .subnetConfiguration(List.of(SubnetConfiguration.builder()
                        .name("public")  // Se debe hacer el VPC publico para que el Internet
                        .subnetType(SubnetType.PUBLIC) // Pueda acceder a nuestra instancia
                        .build()))
                .build();

        SecurityGroup securityGroup = SecurityGroup.Builder.create(this, "SecurityGroup")
                .vpc(vpc)
                .allowAllOutbound(true) // Permitir acceso
                .build();

        securityGroup.addIngressRule(
                Peer.anyIpv4(),
                Port.tcp(80),
                "Abriendo puerto 80."
        );

        securityGroup.addIngressRule(
                Peer.anyIpv4(),
                Port.tcp(8080),
                "Abriendo puerto 8080."
        );
        IRole labRole = Role.fromRoleArn(this,"LABROLE", "arn:aws:iam::575483117736:role/LabRole");

        Instance instance = Instance.Builder.create(this, "Instance")
                .instanceType(new InstanceType("t2.micro"))
                .machineImage(MachineImage.genericLinux(Map.of("us-east-1", "ami-0fcec0bfbafd2b869")))
                .vpc(vpc)
                .securityGroup(securityGroup)
                .role(labRole)
                .build();


    }
}