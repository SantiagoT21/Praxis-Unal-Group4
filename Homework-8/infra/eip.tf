resource "aws_eip" "eip_jenkins_server" {
    instance = aws_instance.group4-ec2.id
    vpc=true
    tags = var.eip_tags
}