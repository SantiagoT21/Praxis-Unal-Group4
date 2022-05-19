#######################################################
# config variables
#######################################################

variable "aws_region" {
  type        = string
  description = "AWS default region (where to deploy your resources)"
}

#######################################################
# EC2 variables
#######################################################

variable "instance_type" {
  type        = string
  description = "EC2 instance type"
}

variable "ec2_tags" {
  description = "EC2 resource tags"
  type        = map(string)
}

variable "ami_id" {
  type        = string
  description = "EC2 AMI ID"
}

variable "subnet_id" {
  type        = string
  description = "Subnet to deploy your resources"
}

variable "public_ip" {
  type        = bool
  description = "Do you need public ip (true->yes,false->no)"
}

variable "key_pair_name" {
  type        = string
  description = "Key pair name"
}

variable "user_data" {
  type        = string
  description = "user data path"
}

##############################################
# Security group variables
##############################################

variable "sg_name" {
  type        = string
  description = "Security group name"
}

variable "sg_description" {
  type        = string
  description = "Security group description"
}

variable "vpc_id" {
  type        = string
  description = "VPC ID"
}

variable "sg_tags" {
  description = "SG resource tags"
  type        = map(string)
}