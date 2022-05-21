resource "aws_eip" "eip_jenkins_server" {
    instance = aws_instance.group4_jenkins_server.id
    vpc=true
    tags = var.eip_tags
}