output "public_ip_address" {
  description = "public ip address"
  value       = aws_instance.group4-ec2.public_ip
}