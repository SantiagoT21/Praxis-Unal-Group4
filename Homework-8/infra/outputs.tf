output "public_ip_address" {
  description = "public ip address"
  value       = aws_eip.eip_jenkins_server.public_ip
}