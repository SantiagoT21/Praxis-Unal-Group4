output "public_ip" {
  description = "public ip address"
  value       = module.deploy_ec2.public_ip_address
}